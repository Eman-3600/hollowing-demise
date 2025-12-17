package net.eman3600.hdemise.cardinal_components;

import net.eman3600.hdemise.init.ModAttributes;
import net.eman3600.hdemise.init.ModEntityComponents;
import net.eman3600.hdemise.networking.s2c.SoulEventPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import static net.eman3600.hdemise.HDemise.MODID;

public class SoulComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {

    public static final int SOUL_PER_VESSEL = 80;
    public static final int SOUL_PER_XP = 10;
    // Rate of Soul Loss in Ghost Form is
    // SOUL_DECAY_AMOUNT / SOUL_DECAY_TICKS
    public static final int SOUL_DECAY_TICKS = 10;
    public static final int SOUL_DECAY_AMOUNT = 1;
    public static final int BURN_SOUL_PER_TICK = 1;
    public static final int EXHAUSTION_THRESHOLD = 0;
    public static final int FOCUS_LENGTH = 20;
    public static final int FOCUS_DELAY = 8;
    public static final int FOCUS_RATE = 4;
    public static final float FOCUS_HP = 6f;
    public static final int VANISH_TICKS = 20;
    public static final int VANISH_DELAY = 5;
    public static final int REVEAL_TICKS = 8;
    public static final int VANISH_RATE = 2;
    public static final int WARNING_TICKS = 4;
    public static final int CURE_TICKS = 60;

    @Environment(EnvType.CLIENT)
    public static final int SUN_TICKS = 15;
    @Environment(EnvType.CLIENT)
    public int sunTicks;
    @Environment(EnvType.CLIENT)
    public static final int CURE_RENDER_TICKS = 60;
    @Environment(EnvType.CLIENT)
    public int cureRenderTicks;

    /**
     * Determines if, at the given moment, blocks should lack
     * collision for the ghost player.
     */
    public boolean ignoreGhostAbstrusion = false;

    public static final Identifier HP_ATTRIBUTE_ID = Identifier.of(MODID, "soul_hp");
    public static final Identifier SPEED_ATTRIBUTE_ID = Identifier.of(MODID, "soul_speed");
    public static final Identifier MAX_SOUL_ATTRIBUTE_ID = Identifier.of(MODID, "soul_max");

    private boolean demonForm = false;
    private boolean ghostMode = false;
    private int soul = 0;
    private int soulDecay = 0;
    private boolean isDirty = false;

    private boolean focusing = false;
    private int focusTime = 0;
    private boolean vanishing = false;
    private int vanishTime = 0;
    private boolean curing = false;
    private int cureTime = 0;
    private int warningTicks = 0;

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
            setFocusing(false);
            setGhost(false);

            reloadAttributes();
        }

        markDirty();
    }

    public boolean isDemon() {
        return this.demonForm;
    }

    public boolean isGhost() {
        return ghostMode;
    }

    public int getSoul() {
        return soul;
    }

    public int getMaxSoul() {
        return SOUL_PER_VESSEL * Math.max(1, (int)player.getAttributeValue(ModAttributes.MAX_SOUL));
    }

    public int getFocusRequirement() {
        return FOCUS_LENGTH * FOCUS_RATE;
    }

    public boolean canFocus() {
        return ((soul >= getFocusRequirement() && player.isOnGround()) || player.isCreative()) && demonForm && !ghostMode && !vanishing && !curing;
    }

    public boolean canVanish() {
        return (soul > 0 || player.isCreative()) && demonForm && !focusing && !curing;
    }

    public boolean canCure() {
        return demonForm && !focusing && !vanishing;
    }

    public float getSoulVessels() {
        return (float) soul / SOUL_PER_VESSEL;
    }

    public void gainSoulFromXP(int xp) {
        addSoul(xp * SOUL_PER_XP);
    }

    public void addSoul(int gain) {
        setSoul(this.soul + gain);
    }

    public void setSoul(int soul) {
        this.soul = MathHelper.clamp(soul, 0, getMaxSoul());
        markDirty();
    }

    public void warnSoul() {
        this.warningTicks = WARNING_TICKS;
        markDirty();
    }

    public int getWarning() {
        return this.warningTicks;
    }

    public void setFocusing(boolean focusing) {
        this.focusing = focusing;
        focusTime = -FOCUS_DELAY;
        markDirty();
    }

    public void setVanishing(boolean vanishing) {
        this.vanishing = vanishing;
        this.vanishTime = vanishing && !isGhost() ? -VANISH_DELAY : 0;
        if (player.getVehicle() != null) {
            player.stopRiding();
        }
        markDirty();
    }

    public void setCuring(boolean curing, int cureTime) {
        this.curing = curing;
        if (curing) {
            this.cureTime = cureTime;
        }
        if (player.getVehicle() != null) {
            player.stopRiding();
        }
        markDirty();
    }

    public void interruptVanish() {
        this.vanishing = false;
        this.vanishTime = 0;
        this.setSoul(getSoul()/2);
        this.warnSoul();
        markDirty();
    }

    public void interruptCure() {
        this.curing = false;
        this.cureTime = 0;
        this.setSoul(0);
        this.warnSoul();
        markDirty();
    }

    public boolean isVanishing() {
        return vanishing;
    }

    public void setGhost(boolean ghost) {
        this.ghostMode = ghost;
        if (player.getVehicle() != null) {
            player.stopRiding();
        }
        soulDecay = SOUL_DECAY_TICKS;
        vanishing = false;
        vanishTime = 0;
        updateAbilities(true);
        markDirty();
    }

    public boolean isFocusing() {
        return this.focusing;
    }

    public boolean isCuring() {
        return this.curing;
    }

    public boolean lockedMovement() {
        return this.focusing || this.vanishing || this.curing;
    }

    public boolean lockedInteraction() {
        return (this.ghostMode || this.focusing || this.vanishing || this.curing) && !player.isCreative();
    }

    public boolean shouldHideInteraction() {
        return this.ghostMode && !player.isCreative();
    }

    public boolean shouldFreeze() {
        return this.vanishing || this.curing;
    }

    public void updateAbilities(boolean shouldSync) {
        PlayerAbilities abilities = player.getAbilities();
        boolean isFlying = player.getAbilities().flying;
        player.getGameMode().setAbilities(abilities);
        if (isGhost()) {
            abilities.allowFlying = true;
            abilities.flying = isFlying;
            abilities.invulnerable = true;
            abilities.allowModifyWorld = false;
        }
        if (shouldSync) {
            player.sendAbilitiesUpdate();
        }
    }

    public void resetSoul() {
        reloadAttributes();

        this.soul = getMaxSoul()/2;
        this.soulDecay = SOUL_DECAY_TICKS;
        this.vanishing = false;
        this.vanishTime = 0;
        this.curing = false;
        this.cureTime = 0;

        setFocusing(false);
        setGhost(hasSolarSickness(true));

        player.experienceLevel = 0;
        player.experienceProgress = 0;
        player.totalExperience = 0;

        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(0f);

        updateAbilities(true);

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

        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);

        if (maxSoulInstance != null) {

            maxSoulInstance.removeModifier(MAX_SOUL_ATTRIBUTE_ID);

//            if (demonForm) {
//                maxSoulInstance.addTemporaryModifier(new EntityAttributeModifier(MAX_SOUL_ATTRIBUTE_ID, -2, EntityAttributeModifier.Operation.ADD_VALUE));
//            }
        }

        RegistryEntry<EntityAttribute> speed = EntityAttributes.MOVEMENT_SPEED;
        EntityAttributeInstance speedInstance = container.getCustomInstance(speed);

        if (speedInstance != null) {

            speedInstance.removeModifier(SPEED_ATTRIBUTE_ID);

//            if (ghostMode) {
//                speedInstance.addTemporaryModifier(new EntityAttributeModifier(SPEED_ATTRIBUTE_ID, .2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
//            }
        }
    }






    @Override
    public void clientTick() {
        if (hasSolarSickness(true)) {
            if (sunTicks < SUN_TICKS)
                sunTicks++;
        } else if (sunTicks > 0) {
            sunTicks--;
        }

        if (isFocusing()) {
            Random random = player.getRandom();
            player.getEntityWorld().addParticleClient(ParticleTypes.END_ROD.getType(),
                    player.getX() + (random.nextFloat() - .5f) * .3f,
                    player.getY() + 0.75f,
                    player.getZ() + (random.nextFloat() - .5f) * .3f,
                    (random.nextFloat() - .5f),
                    (random.nextFloat() - .5f),
                    (random.nextFloat() - .5f));
        }

        if (isCuring()) {
            Random random = player.getRandom();
            player.getEntityWorld().addParticleClient(ParticleTypes.TOTEM_OF_UNDYING.getType(),
                    player.getX() + (random.nextFloat() - .5f) * .3f,
                    player.getY() + 0.75f,
                    player.getZ() + (random.nextFloat() - .5f) * .3f,
                    (random.nextFloat() - .5f),
                    (random.nextFloat() - .5f),
                    (random.nextFloat() - .5f));

            if (cureRenderTicks < CURE_RENDER_TICKS)
                cureRenderTicks++;
        } else if (cureRenderTicks > 0) {
            cureRenderTicks--;
        }
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

                if (focusTime >= FOCUS_LENGTH) {
                    player.heal(FOCUS_HP);
                    player.setHealth(MathHelper.ceil(player.getHealth()));
                    sendSoulEvent(SoulEventPayload.SoulEventType.FOCUS);
                    setFocusing(canFocus() && player.getHealth() < player.getMaxHealth());
                } else if (soul <= 0 || !player.isOnGround()) {
                    setFocusing(false);
                }
            }

            if (vanishing) {
                vanishTime++;

                if (!player.isCreative() && !ghostMode && vanishTime > 0) {
                    addSoul(-VANISH_RATE);
                }

                if (soul <= 0) {
                    vanishing = false;
                    vanishTime = 0;
                    markDirty();
                } else if (vanishTime >= (ghostMode ? REVEAL_TICKS : VANISH_TICKS)) {
                    setGhost(!ghostMode);
                    sendSoulEvent(ghostMode ? SoulEventPayload.SoulEventType.VANISH : SoulEventPayload.SoulEventType.REAPPEAR);
                }
            }

            if (curing) {
                cureTime--;

                if (cureTime <= 0) {
                    setCuring(false, 0);
                    setForm(false);
                    player.clearStatusEffects();
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 0));
                    sendSoulEvent(SoulEventPayload.SoulEventType.REVIVE);
                }
            }

            if (!player.isCreative()) {

                if (hasSolarSickness(true)) {

                    if (soul <= EXHAUSTION_THRESHOLD) {
                        if (shouldSetOnFire()) {
                            player.setOnFireFor(8f);
                        }
                    } else {
                        addSoul(-BURN_SOUL_PER_TICK);
                    }
                }

                if (ghostMode) {
                    soulDecay--;
                    if (soulDecay <= 0) {
                        soulDecay = SOUL_DECAY_TICKS;
                        addSoul(-SOUL_DECAY_AMOUNT);
                    }
                    if (soul <= 0) {
                        setGhost(false);
                    }
                }
            }

            if (soul > getMaxSoul() || soul < 0) {
                setSoul(getSoul());
            }
        }

        if (warningTicks > 0) {
            warningTicks--;
            markDirty();
        }

        if (this.isDirty) {
            this.isDirty = false;
            ModEntityComponents.SOUL.sync(this.player);
        }
    }

    public void markDirty() {
        this.isDirty = true;
    }

    private void sendSoulEvent(SoulEventPayload.SoulEventType eventType) {
        SoulEventPayload payload = new SoulEventPayload(player.getX(), player.getY(), player.getZ(), player.getRandom().nextFloat(), eventType);

        for (ServerPlayerEntity otherPlayer : PlayerLookup.around((ServerWorld) player.getEntityWorld(), player.getEntityPos(), 16d)) {
            ServerPlayNetworking.send(otherPlayer, payload);
        }
    }

    @Override
    public void readData(ReadView readView) {
        demonForm = readView.getBoolean("demon_form", false);
        ghostMode = readView.getBoolean("ghost_mode", false);
        soul = readView.getInt("soul", 0);
        soulDecay = readView.getInt("soul_decay", SOUL_DECAY_TICKS);
        focusing = readView.getBoolean("focusing", false);
        focusTime = readView.getInt("focus_time", 0);
        vanishing = readView.getBoolean("vanishing", false);
        vanishTime = readView.getInt("vanish_time", 0);
        curing = readView.getBoolean("curing", false);
        cureTime = readView.getInt("cure_time", 0);
        warningTicks = readView.getInt("warning", 0);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("demon_form", demonForm);
        writeView.putBoolean("ghost_mode", ghostMode);
        writeView.putInt("soul", soul);
        writeView.putInt("soul_decay", soulDecay);
        writeView.putBoolean("focusing", focusing);
        writeView.putInt("focus_time", focusTime);
        writeView.putBoolean("vanishing", vanishing);
        writeView.putInt("vanish_time", vanishTime);
        writeView.putBoolean("curing", curing);
        writeView.putInt("cure_time", cureTime);
        writeView.putInt("warning", warningTicks);
    }

    /**
     * Whether the player should be set on fire due to solar sickness
     * @return randomly returns true when the player is in the sun
     */
    public boolean shouldSetOnFire() {

        float f = player.getBrightnessAtEyes();
        return hasSolarSickness(true) && player.getRandom().nextFloat() * 30.0f < (f - 0.4f) * 2.0f;
    }

    /**
     * Determines whether the player should be burning due to sun exposure.
     * @return whether the player should burn
     */
    public boolean hasSolarSickness(boolean ghostIgnoresSun) {

        if (!demonForm || curing || (ghostMode && ghostIgnoresSun) || player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
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
