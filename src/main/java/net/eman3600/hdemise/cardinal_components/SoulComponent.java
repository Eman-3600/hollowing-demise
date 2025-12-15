package net.eman3600.hdemise.cardinal_components;

import net.eman3600.hdemise.init.ModEntityComponents;
import net.eman3600.hdemise.networking.s2c.FocusSoundPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import static net.eman3600.hdemise.HDemise.MODID;

public class SoulComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {

    public static final int MAX_SOUL = 800;
    public static final int SOUL_PER_XP = 16;
    public static final int SOUL_DECAY_TICKS = 75;
    public static final int BURN_SOUL_PER_TICK = 1;
    public static final int EXHAUSTION_THRESHOLD = 0;
    public static final int FOCUS_LENGTH = 20;
    public static final int FOCUS_DELAY = 8;
    public static final int FOCUS_RATE = 4;
    public static final float FOCUS_HP = 6f;

    public static final Identifier HP_ATTRIBUTE_ID = Identifier.of(MODID, "soul_hp");
    public static final Identifier SPEED_ATTRIBUTE_ID = Identifier.of(MODID, "soul_speed");

    private boolean demonForm = false;
    private boolean ghostMode = false;
    private int soul = 0;
    private int soulDecay = 0;
    private boolean isDirty = false;

    private boolean focusing = false;
    private int focusTime = 0;

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

            player.getHungerManager().setFoodLevel(20);
            player.getHungerManager().setSaturationLevel(5f);

            reloadAttributes();
        }

        setFocusing(false);

        markDirty();
    }

    public boolean isDemon() {
        return this.demonForm;
    }

    public int getSoul() {
        return soul;
    }

    public int getFocusRequirement() {
        return FOCUS_LENGTH * FOCUS_RATE;
    }

    public boolean canFocus() {
        return ((soul >= getFocusRequirement() && player.isOnGround()) || player.isCreative()) && demonForm;
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

    public void setFocusing(boolean focusing) {
        this.focusing = focusing;
        focusTime = -FOCUS_DELAY;
        markDirty();
    }

    public boolean isFocusing() {
        return this.focusing;
    }

    public boolean lockedMovement() {
        return this.focusing;
    }

    public void resetSoul() {
        this.soul = MAX_SOUL/5;
        this.soulDecay = SOUL_DECAY_TICKS;
        markDirty();

        player.experienceLevel = 0;
        player.experienceProgress = 0;
        player.totalExperience = 0;

        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(0f);

        reloadAttributes();

        player.setHealth(player.getMaxHealth());
    }

    public void reloadAttributes() {
        AttributeContainer container = player.getAttributes();

        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);

        if (hpInstance != null) {

            hpInstance.removeModifier(HP_ATTRIBUTE_ID);

            if (demonForm) {
                hpInstance.addTemporaryModifier(new EntityAttributeModifier(HP_ATTRIBUTE_ID, -.4, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }

        RegistryEntry<EntityAttribute> speed = EntityAttributes.MOVEMENT_SPEED;
        EntityAttributeInstance speedInstance = container.getCustomInstance(speed);

        if (speedInstance != null) {

            speedInstance.removeModifier(SPEED_ATTRIBUTE_ID);

            if (ghostMode) {
                speedInstance.addTemporaryModifier(new EntityAttributeModifier(SPEED_ATTRIBUTE_ID, .2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
    }






    @Override
    public void clientTick() {

    }

    @Override
    public void serverTick() {

        if (demonForm) {

            if (player.totalExperience > 0 || player.experienceLevel > 0) {

                player.experienceLevel = 0;
                player.experienceProgress = 0;
                player.totalExperience = 0;
            }

            HungerManager manager = player.getHungerManager();
            if (manager.getFoodLevel() < 20 || manager.getSaturationLevel() > 0) {
                manager.setSaturationLevel(0f);
                manager.setFoodLevel(20);
            }

            if (focusing) {
                focusTime++;

                if (focusTime > 0) {
                    addSoul(-FOCUS_RATE);
                }

                if (soul <= 0 || !player.isOnGround()) {
                    setFocusing(false);
                } else if (focusTime >= FOCUS_LENGTH) {
                    player.heal(FOCUS_HP);
                    playFocusSound();
                    setFocusing(canFocus());
                }
            }

            if (!player.isCreative() && !focusing) {

                soulDecay--;
                if (soulDecay <= 0 && !player.isCreative()) {
                    soulDecay = SOUL_DECAY_TICKS;
                    addSoul(-1);
                }

                if (hasSolarSickness()) {

                    if (soul <= EXHAUSTION_THRESHOLD) {
                        if (shouldSetOnFire()) {
                            player.setOnFireFor(8f);
                        }
                    } else {
                        addSoul(-BURN_SOUL_PER_TICK);
                    }
                }
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

    private void playFocusSound() {
        FocusSoundPayload payload = new FocusSoundPayload(player.getX(), player.getY(), player.getZ());

        for (ServerPlayerEntity otherPlayer : PlayerLookup.around((ServerWorld) player.getEntityWorld(), player.getEntityPos(), 16d)) {
            ServerPlayNetworking.send(otherPlayer, payload);
        }
    }

    @Override
    public void readData(ReadView readView) {
        demonForm = readView.getBoolean("demon_form", false);
        ghostMode = readView.getBoolean("ghost_mode", false);
        soul = readView.getInt("soul", MAX_SOUL/2);
        soulDecay = readView.getInt("soul_decay", SOUL_DECAY_TICKS);
        focusing = readView.getBoolean("focusing", false);
        focusTime = readView.getInt("focus_time", 0);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("demon_form", demonForm);
        writeView.putBoolean("ghost_mode", ghostMode);
        writeView.putInt("soul", soul);
        writeView.putInt("soul_decay", soulDecay);
        writeView.putBoolean("focusing", focusing);
        writeView.putInt("focus_time", focusTime);
    }

    /**
     * Whether the player should be set on fire due to solar sickness
     * @return randomly returns true when the player is in the sun
     */
    public boolean shouldSetOnFire() {

        float f = player.getBrightnessAtEyes();
        return hasSolarSickness() && player.getRandom().nextFloat() * 30.0f < (f - 0.4f) * 2.0f;
    }

    /**
     * Determines whether the player should be burning due to sun exposure.
     * @return whether the player should burn
     */
    public boolean hasSolarSickness() {

        if (!demonForm || player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return false;
        }

        boolean bl;
        BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ());
        bl = player.isTouchingWaterOrRain() || player.inPowderSnow || player.wasInPowderSnow;
        return player.getEntityWorld().isDay() && !bl && player.getEntityWorld().isSkyVisible(blockPos);
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
