package net.eman3600.hdemise.cardinal_components;

import net.eman3600.hdemise.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class SoulComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {

    public static final int MAX_SOUL = 800;
    public static final int SOUL_PER_XP = 8;
    public static final int SOUL_DECAY_TICKS = 75;
    public static final int SOUL_PER_HUNGER = 4;

    private boolean demonForm = false;
    private int soul = 0;
    private int soulDecay = 0;
    private boolean isDirty = false;

    private final PlayerEntity player;

    public SoulComponent(PlayerEntity player) {
        this.player = player;
    }


    public void setForm(boolean demonForm) {
        this.demonForm = demonForm;

        if (demonForm) {
            resetSoul();
        } else {
            this.soul = 0;

            player.getHungerManager().setFoodLevel(8);
            player.getHungerManager().setSaturationLevel(8f);
        }

        markDirty();
    }

    public boolean isDemon() {
        return this.demonForm;
    }

    public int getSoul() {
        return soul;
    }

    public float getSoulPercentage() {
        return (float) soul / MAX_SOUL;
    }

    public void gainSoulFromXP(int xp) {
        addSoul(xp * SOUL_PER_XP);
    }

    public void addSoul(int gain) {
        setSoul(this.soul + gain);
    }

    public void setSoul(int soul) {
        this.soul = MathHelper.clamp(soul, 0, MAX_SOUL);
        markDirty();
    }

    public void resetSoul() {
        this.soul = MAX_SOUL/2;
        this.soulDecay = SOUL_DECAY_TICKS;
        markDirty();

        player.experienceLevel = 0;
        player.experienceProgress = 0;
        player.totalExperience = 0;

        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(20f);
    }






    @Override
    public void clientTick() {

    }

    @Override
    public void serverTick() {

        if (demonForm) {

            soulDecay--;
            if (soulDecay <= 0 && !player.isCreative()) {
                soulDecay = SOUL_DECAY_TICKS;
                addSoul(-1);
            }

            if (player.totalExperience > 0 || player.experienceLevel > 0) {

                player.experienceLevel = 0;
                player.experienceProgress = 0;
                player.totalExperience = 0;
            }
        }

        if (this.isDirty) {
            this.isDirty = false;
            ModEntityComponents.SOUL.sync(this.player);
        }
    }

    public void markDirty() {
        this.isDirty = true;
    }

    @Override
    public void readData(ReadView readView) {
        demonForm = readView.getBoolean("demon_form", false);
        soul = readView.getInt("soul", MAX_SOUL/2);
        soulDecay = readView.getInt("soul_decay", SOUL_DECAY_TICKS);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("demon_form", demonForm);
        writeView.putInt("soul", soul);
        writeView.putInt("soul_decay", soulDecay);
    }


    /**
     * Gets the soul component of the player.
     * @param player the player entity
     * @return the component, or null if the passed in entity was not a player
     */
    public static SoulComponent of(LivingEntity player) {

        return ModEntityComponents.SOUL.getNullable(player);
    }
}
