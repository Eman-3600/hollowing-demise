package net.eman3600.hdemise.cardinal_components;

import com.mojang.serialization.Codec;
import net.eman3600.hdemise.init.cca.ModEntityComponents;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.networking.s2c.TutorialPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TutorialComponent implements AutoSyncedComponent, ServerTickingComponent {

    private static final int TUTORIAL_CHECK_TIME = 600;
    private static final int TUTORIAL_REPEAT_INTERVAL = 400;

    public static final String VANISH_TUTORIAL = "tutorial.hdemise.ghost.vanish";
    public static final String MATERIALIZE_TUTORIAL = "tutorial.hdemise.ghost.materialize";
    public static final String FOCUS_TUTORIAL = "tutorial.hdemise.focus";
    public static final String LUNGE_TUTORIAL = "tutorial.hdemise.lunge";
    public static final String JETPACK_TUTORIAL = "tutorial.hdemise.jetpack";

    private final PlayerEntity player;
    private final Set<String> completedTutorials;
    private String currentTutorial = "";
    private int tutorialCheckTicks = 0;
    private int tutorialRepeatTicks = 0;

    private boolean dirty = false;

    public TutorialComponent(PlayerEntity player) {
        this.player = player;
        completedTutorials = new HashSet<>();
    }


    @Override
    public void serverTick() {

        tutorialCheckTicks++;
        if (tutorialCheckTicks >= TUTORIAL_CHECK_TIME) {
            updateTutorials();
        }

        if (++tutorialRepeatTicks >= TUTORIAL_REPEAT_INTERVAL) {
            displayTutorial(currentTutorial);
        }

        if (dirty) {
            dirty = false;
            ModEntityComponents.TUTORIAL.sync(player);
        }
    }

    public boolean isCurrentTutorial(String tutorial) {
        return tutorial.equals(currentTutorial);
    }

    public static void completeIfActive(PlayerEntity player, String tutorial) {
        TutorialComponent component = TutorialComponent.of(player);

        if (component.isCurrentTutorial(tutorial) && player instanceof ServerPlayerEntity) {
            component.completeTutorial(tutorial);
        }
    }

    public List<String> getAvailableTutorials() {
        ArrayList<String> tutorials = new ArrayList<>();

        SoulComponent sc = SoulComponent.of(player);
        if (sc.getSoulType().hasHealingFocus()) {
            if (sc.getSoul() >= sc.getFocusRequirement() && player.getHealth() <= player.getMaxHealth()/2) {
                tutorials.add(FOCUS_TUTORIAL);
            }
        }
        if (sc.isGhost()) {
            tutorials.add(MATERIALIZE_TUTORIAL);
        } else if (sc.isSoulless() && player.getEntityWorld().isDay() && sc.getSoul() >= 160) {
            tutorials.add(VANISH_TUTORIAL);
        } else if (sc.getSoulType() == ModSoulTypes.CONSTRUCT) {
            tutorials.add(JETPACK_TUTORIAL);
        }

        return tutorials;
    }

    public void updateTutorials() {
        this.tutorialCheckTicks = 0;

        List<String> tutorials = getAvailableTutorials();
        String priority = "";
        for (String tutorial : tutorials) {
            if (!completedTutorials.contains(tutorial)) {
                priority = tutorial;
                break;
            }
        }

        if (!currentTutorial.equals(priority)) {
            startTutorial(priority);
        }
    }

    public void startTutorial(String tutorial) {
        currentTutorial = tutorial;
        tutorialRepeatTicks = 0;

        displayTutorial(tutorial);

        markDirty();
    }

    public void completeTutorial(String tutorial) {
        completedTutorials.add(tutorial);

        if (currentTutorial.equals(tutorial)) {
            updateTutorials();
        }

        markDirty();
    }

    public static void updateTutorials(PlayerEntity player) {
        TutorialComponent.of(player).updateTutorials();
    }

    public static void completeTutorial(PlayerEntity player, String tutorial) {
        TutorialComponent.of(player).completeTutorial(tutorial);
    }

    public void displayTutorial(String tutorial) {
        if (!tutorial.isEmpty() && player instanceof ServerPlayerEntity p) {
            tutorialRepeatTicks = 0;
            TutorialPayload.send(p, tutorial);
        }
    }

    /**
     * Displays a tutorial if it isn't complete
     * @param player the player viewing the tutorial
     * @param tutorial the tutorial translation key
     */
    public static void displayIfIncomplete(PlayerEntity player, String tutorial) {
        TutorialComponent component = of(player);

        if (!component.completedTutorials.contains(tutorial)) {
            component.displayTutorial(tutorial);
        }
    }

    public void markDirty() {
        this.dirty = true;
    }

    public static TutorialComponent of(PlayerEntity player) {
        if (player == null) return null;

        return ModEntityComponents.TUTORIAL.getNullable(player);
    }

    @Override
    public void readData(ReadView readView) {
        tutorialCheckTicks = readView.getInt("check_ticks", 0);
        tutorialRepeatTicks = readView.getInt("repeat_ticks", 0);
        currentTutorial = readView.getString("current", "");
        ReadView.TypedListReadView<String> listReader = readView.getTypedListView("completed", Codec.STRING);

        completedTutorials.clear();
        listReader.stream().forEach(completedTutorials::add);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putInt("check_ticks", tutorialCheckTicks);
        writeView.putInt("repeat_ticks", tutorialRepeatTicks);
        writeView.putString("current", currentTutorial);
        WriteView.ListAppender<String> listWriter = writeView.getListAppender("completed", Codec.STRING);

        completedTutorials.forEach(listWriter::add);
    }
}
