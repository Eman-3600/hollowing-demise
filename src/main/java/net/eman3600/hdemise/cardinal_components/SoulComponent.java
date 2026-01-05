package net.eman3600.hdemise.cardinal_components;

import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.cca.ModEntityComponents;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.item.XPCoreItem;
import net.eman3600.hdemise.item.augment.AugmentItem;
import net.eman3600.hdemise.item.augment.FocusAugment;
import net.eman3600.hdemise.networking.s2c.SoulEventPayload;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.util.RayHelper;
import net.eman3600.hdemise.util.inventory.SoulInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class SoulComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {

    public static final int SOUL_PER_VESSEL = 80;
    public static final int SOUL_PER_XP = 10;
    // Rate of Soul Loss in Ghost Form is
    // SOUL_DECAY_AMOUNT / SOUL_DECAY_TICKS
    public static final int SOUL_DECAY_TICKS = 10;
    public static final int SOUL_DECAY_AMOUNT = 1;
    public static final int BURN_SOUL_PER_TICK = 1;
    public static final int EXHAUSTION_THRESHOLD = 0;
    public static final int FOCUS_DELAY = 8;
    public static final int VANISH_TICKS = 20;
    public static final int VANISH_DELAY = 5;
    public static final int REVEAL_TICKS = 8;
    public static final int VANISH_RATE = 2;
    public static final int WARNING_TICKS = 4;
    public static final int CURE_TICKS = 60;
    public static final float REGEN_REQUIREMENT = 100f;
    public static final int SOLAR_SOUL_TICKS = 10;
    public static final int SUN_MULTIPLIER = 4;
    public static final double JET_SPEED_CAP = .8;
    public static final double JET_ACCELERATION = 0.16;
    public static final int LUNGE_COOLDOWN_TICKS = 12;
    public static final double LUNGE_SPEED = .75;
    public static final double LUNGE_HEIGHT = .5;
    public static final int TOP_UP_COOLDOWN = 7800;

    @Environment(EnvType.CLIENT)
    public static final int SUN_TICKS = 15;
    @Environment(EnvType.CLIENT)
    public int sunTicks;
    @Environment(EnvType.CLIENT)
    public static final int CURE_RENDER_TICKS = 60;
    @Environment(EnvType.CLIENT)
    public int cureRenderTicks;

    private boolean ghostMode = false;
    private int soul = 0;
    private int soulDecay = 0;
    private boolean isDirty = false;

    private SoulType soulType;

    private boolean focusing = false;
    private int focusTime = 0;
    private boolean vanishing = false;
    private int vanishTime = 0;
    private boolean curing = false;
    private int cureTime = 0;

    private int warningTicks = 0;
    private boolean jetEnabled = false;
    private boolean jetting = false;
    private boolean jetJammed = false;
    private boolean lunging = false;
    private int lungeCooldown = 0;
    private double lungeGravity = 0d;

    private float hollowHp = 0;
    private int hollowSoul = 0;
    private boolean hollowTopped = true;
    private int topUpCooldown =0;

    private float regenTime = 0f;
    private float solarSoulTime = 0f;

    private final PlayerEntity player;

    private final SoulInventory inventory;

    public SoulComponent(PlayerEntity player) {
        this.player = player;
        this.soulType = ModSoulTypes.MORTAL;
        inventory = new SoulInventory(this);
    }

    public SoulInventory getInventory() {
        return inventory;
    }

    public void validateSoulStack() {
        ItemStack stack = inventory.getStack(0);

        if (!stack.isEmpty() && stack.getItem() instanceof SoulItem item && item.getSoulType() == this.soulType) {
            SoulItem.saveStats(player, stack);
        } else {
            if (!player.giveItemStack(inventory.removeStack(0))) {
                ItemScatterer.spawn(player.getEntityWorld(), player.getX(), player.getY(), player.getZ(), stack);
            }

            replaceSoulStack();
        }
    }

    public void topUp() {
        player.setHealth(player.getMaxHealth());
        HungerManager manager = player.getHungerManager();
        manager.setFoodLevel(20);
        manager.setSaturationLevel(20f);
        setSoul(getMaxSoul());
        forEachAugment((stack, p) -> {
            if (stack.getItem() instanceof AugmentItem item) {
                item.onTopUp(p, stack);
            }
        });
    }

    public void setHollowTopped(boolean topped) {
        this.hollowTopped = topped;
        markDirty();
    }

    public boolean isHollowTopped() {
        return hollowTopped;
    }

    public float getHollowHp() {
        return hollowHp;
    }

    public int getHollowSoul() {
        return hollowSoul;
    }

    public void saveHollowStats() {
        this.hollowHp = player.getHealth();
        this.hollowSoul = getSoul();
        this.hollowTopped = false;
        markDirty();
    }

    public void loadHollowStats() {
        player.setHealth(this.hollowHp);
        setSoul(this.hollowSoul);
    }

    public void replaceSoulStack() {
        ItemStack stack = this.soulType.getDefaultSoulStack();
        SoulItem.saveStats(player, stack);
        inventory.setStack(0, stack);
    }

    public void applySoulStack(ItemStack stack) {
        SoulItem.loadStats(player, stack, true);
    }

    public void setSoulType(SoulType soulType) {
        this.soulType.removeAttributes(this.player);
        if (!this.soulType.usesHunger() && soulType.usesHunger()) {
            HungerManager manager = player.getHungerManager();
            manager.setFoodLevel(20);
            manager.setSaturationLevel(5f);
        }
        this.soulType = soulType;
        this.inventory.scatterAugments(this.player);
        this.soulType.applyAttributes(this.player);
        resetSoul();
    }

    public SoulType getSoulType() {
        return this.soulType;
    }

    public void onDeath() {

        setHollowTopped(true);
        this.topUpCooldown = 0;

        if (this.soulType != ModSoulTypes.HOLLOW && this.soulType != ModSoulTypes.NEGATIVE) {
            SoulItem.resetStats(inventory.getStack(0));
            XPCoreItem.extractToWorld(player);
            this.setSoulType(ModSoulTypes.HOLLOW);
        } else {
            reloadAttributes();
            resetSoul();
        }

        setSoul(getMaxSoul() - (SOUL_PER_VESSEL * 2));
        validateSoulStack();

    }

    public boolean isSoulless() {
        return this.soulType == ModSoulTypes.HOLLOW;
    }

    public boolean usesSoul() {
        return this.soulType.usesSoul();
    }

    public boolean usesHunger() {
        return this.soulType.usesHunger();
    }

    public boolean hasExperience() {
        return this.soulType.hasExperience();
    }

    public boolean isGhost() {
        return ghostMode;
    }

    public boolean isDrowningImmune() {
        return ghostMode || isUndead() || soulType == ModSoulTypes.CONSTRUCT;
    }

    public int getSoul() {
        return soul;
    }

    public int getMaxSoul() {
        return usesSoul() ? (SOUL_PER_VESSEL * Math.max(1, (int)player.getAttributeValue(ModAttributes.MAX_SOUL))) : 0;
    }

    public float getRegenRate() {
        return (float) player.getAttributeValue(ModAttributes.REGEN);
    }

    public int getFocusRequirement() {
        return soulType.getFocusTicks() * soulType.getFocusRate();
    }

    public boolean canFocus() {
        return ((soul >= getFocusRequirement() && player.isOnGround()) || player.isCreative()) && usesSoul() && !isGhost() && !vanishing && !curing;
    }

    public boolean canVanish() {
        return (soul > 0 || player.isCreative() || !usesSoul()) && getSoulType().canVanish() && !player.hasStatusEffect(ModStatusEffects.BLOCKED) && !focusing && !curing;
    }

    public boolean canCure() {
        return isSoulless() && !focusing && !vanishing;
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
        if (soul < this.soul) {
            this.solarSoulTime = 0;
        }
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

    public boolean isJetting() {
        return this.jetting;
    }

    public boolean isLunging() {
        return lunging && lungeCooldown > 0;
    }

    public void setJetting(boolean jetting) {
        this.jetting = jetting;
        markDirty();
    }

    public void enableJet(boolean jetEnabled) {
        this.jetEnabled = jetEnabled;
        markDirty();

        if (!jetEnabled && jetting) {
            setJetting(false);
        }

        player.sendMessage(Text.translatable(jetEnabled ? "soul_type.hdemise.construct.enable_jet" : "soul_type.hdemise.construct.disable_jet"), true);
    }

    public void jamJet() {
        this.jetJammed = true;
        this.jetEnabled = false;
        this.jetting = false;

        warnSoul();

        markDirty();
    }

    public boolean isJetJammed() {
        return jetJammed;
    }

    public boolean isJetEnabled() {
        return this.jetEnabled;
    }

    public boolean isLungeAvailable() {
        return !lunging && lungeCooldown <= 0;
    }

    public double getLungeGravity() {
        return lungeGravity;
    }

    public void lunge() {
        Vec3d vel = RayHelper.rayZVector(player.getYaw(), 0).multiply(LUNGE_SPEED).add(0, LUNGE_HEIGHT, 0);

        player.setVelocity(vel);
        player.velocityDirty = true;
        player.currentExplosionImpactPos = player.getEntityPos().add(0, -2, 0);
        player.setIgnoreFallDamageFromCurrentExplosion(true);


        if (!player.getEntityWorld().isClient()) {
            lunging = true;
            lungeCooldown = LUNGE_COOLDOWN_TICKS;
            lungeGravity = player.getFinalGravity() * 1.5;
            player.setOnGround(false);
            // ModStatusEffect.reduceDuration(player, ModStatusEffects.RAGE, ModStatusEffect.RAGE_REDUCTION_ON_LUNGE);
            markDirty();

            player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1, .8f + .4f * player.getRandom().nextFloat());

            ((ServerPlayerEntity)player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        }
    }

    public boolean isUndead() {
        return this.soulType.isUndead();
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

    public boolean canTopUp() {
        return this.topUpCooldown <= 0;
    }

    public int getTopUpCooldown() {
        return topUpCooldown;
    }

    public void startTopUpCooldown() {
        this.topUpCooldown = TOP_UP_COOLDOWN;
        markDirty();
    }

    public int getTopUpDisplayPixels() {
        return (int)((float)(TOP_UP_COOLDOWN - this.topUpCooldown)/TOP_UP_COOLDOWN * 7);
    }

    public boolean lockedMovement() {
        return this.focusing || this.vanishing || this.curing;
    }

    public boolean lockedInteraction() {
        return (isGhost() || this.focusing || this.vanishing || this.curing) && !player.isCreative();
    }

    public boolean shouldHideInteraction() {
        return isGhost() && !player.isCreative();
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

        setSoul(getSoul());
        this.soulDecay = SOUL_DECAY_TICKS;
        this.vanishing = false;
        this.vanishTime = 0;
        this.curing = false;
        this.cureTime = 0;
        this.regenTime = 0f;
        this.solarSoulTime = 0f;
        this.jetting = false;
        this.jetEnabled = false;
        this.jetJammed = false;

        player.clearStatusEffects();

        setFocusing(false);
        setGhost(hasSolarSickness(true) && soulType.canVanish());

        if (!hasExperience()) {

            player.experienceLevel = 0;
            player.experienceProgress = 0;
            player.totalExperience = 0;
        }

        if (!usesHunger()) {
            player.getHungerManager().setFoodLevel(20);
            player.getHungerManager().setSaturationLevel(0f);
        }

        markDirty();

        updateAbilities(true);
    }

    public void reloadAttributes() {
        this.soulType.removeAttributes(this.player);
        this.soulType.applyAttributes(this.player);

        forEachAugment((stack, player) -> {
            if (stack.getItem() instanceof AugmentItem item) {
                item.onReload(player, stack);
            }
        });
    }

    public boolean hasAugment(Item item) {
        for (int i = 1; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAugment(TagKey<Item> tag) {
        for (int i = 1; i < inventory.size(); i++) {
            if (inventory.getStack(i).isIn(tag)) {
                return true;
            }
        }
        return false;
    }

    public void forEachAugment(BiConsumer<ItemStack, PlayerEntity> function) {
        for (int i = 1; i < inventory.size(); i++) {
            function.accept(inventory.getStack(i), player);
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
            Box box = player.getBoundingBox().expand(.3);
            for (int i = 0; i < 5; i++) {
                player.getEntityWorld().addParticleClient(ParticleTypes.END_ROD.getType(),
                        box.minX + random.nextFloat() * (box.maxX - box.minX),
                        player.getY(),
                        box.minZ + random.nextFloat() * (box.maxZ - box.minZ),
                        0,
                        random.nextFloat() * .2f + .4f,
                        0);
            }
        }

        if (isVanishing()) {
            Random random = player.getRandom();
            Box box = player.getBoundingBox();
            if (isGhost()) {

                for (int i = 0; i < 10; i++) {
                    player.getEntityWorld().addParticleClient(ParticleTypes.SMOKE.getType(),
                            player.getX() + (random.nextFloat() - .5f) * .2f,
                            box.minY + random.nextFloat() * (box.maxY - box.minY),
                            player.getZ() + (random.nextFloat() - .5f) * .2f,
                            (random.nextFloat() - .5f),
                            (random.nextFloat() - .5f) * .5f,
                            (random.nextFloat() - .5f));
                }
            } else {

                for (int i = 0; i < 10; i++) {
                    player.getEntityWorld().addParticleClient(ParticleTypes.REVERSE_PORTAL.getType(),
                            player.getX() + (random.nextFloat() - .5f) * .2f,
                            box.minY + random.nextFloat() * (box.maxY - box.minY),
                            player.getZ() + (random.nextFloat() - .5f) * .2f,
                            (random.nextFloat() - .5f) * 4f,
                            (random.nextFloat() - .5f) * 2f,
                            (random.nextFloat() - .5f) * 4f);
                }
            }
        }

        if (isCuring()) {
            Random random = player.getRandom();
            Box box = player.getBoundingBox().expand(.3);
            for (int i = 0; i < 5; i++) {
                player.getEntityWorld().addParticleClient(ParticleTypes.TOTEM_OF_UNDYING.getType(),
                        box.minX + random.nextFloat() * (box.maxX - box.minX),
                        player.getY(),
                        box.minZ + random.nextFloat() * (box.maxZ - box.minZ),
                        0,
                        random.nextFloat() * .4f + .8f,
                        0);
            }
//            player.getEntityWorld().addParticleClient(ParticleTypes.TOTEM_OF_UNDYING.getType(),
//                    player.getX() + (random.nextFloat() - .5f) * .3f,
//                    player.getY() + 0.75f,
//                    player.getZ() + (random.nextFloat() - .5f) * .3f,
//                    (random.nextFloat() - .5f),
//                    (random.nextFloat() - .5f),
//                    (random.nextFloat() - .5f));

            if (cureRenderTicks < CURE_RENDER_TICKS)
                cureRenderTicks++;
        } else if (cureRenderTicks > 0) {
            cureRenderTicks--;
        }

        if (isJetting()) {


            moveWithJet(true);
        }
    }

    private void moveWithJet(boolean isClient) {

        if (player.isGliding() || player.isInSwimmingPose()) {
            Vec3d forward = RayHelper.rayZVector(player.getYaw(), player.getPitch());

            if (isClient) {
                Random random = player.getRandom();
                Box box = player.getBoundingBox();
                Vec3d backward = forward.normalize().multiply(-random.nextFloat() * .4f - .8f);
                for (int i = 0; i < 5; i++) {

                    player.getEntityWorld().addParticleClient(ParticleTypes.SOUL_FIRE_FLAME.getType(),
                            box.minX + random.nextFloat() * (box.maxX - box.minX) + backward.x * 2,
                            box.minY + random.nextFloat() * (box.maxY - box.minY) + backward.y * 2,
                            box.minZ + random.nextFloat() * (box.maxZ - box.minZ) + backward.z * 2,
                            backward.x,
                            backward.y,
                            backward.z);

                    player.getEntityWorld().addParticleClient(ParticleTypes.SMOKE.getType(),
                            box.minX + random.nextFloat() * (box.maxX - box.minX) + backward.x * 2,
                            box.minY + random.nextFloat() * (box.maxY - box.minY) + backward.y * 2,
                            box.minZ + random.nextFloat() * (box.maxZ - box.minZ) + backward.z * 2,
                            backward.x,
                            backward.y,
                            backward.z);
                }
            }

            Vec3d velocity = player.getVelocity().add(forward.multiply(JET_ACCELERATION));

            if (velocity.lengthSquared() > 16) {

                velocity = velocity.normalize().multiply(4);
            }

            player.setVelocity(velocity);
            player.velocityDirty = true;

        } else {


            if (isClient) {
                Random random = player.getRandom();
                Box box = player.getBoundingBox();
                for (int i = 0; i < ((player.isSneaking()) ? 1 : 3); i++) {

                    player.getEntityWorld().addParticleClient(ParticleTypes.SOUL_FIRE_FLAME.getType(),
                            box.minX + random.nextFloat() * (box.maxX - box.minX),
                            player.getY(),
                            box.minZ + random.nextFloat() * (box.maxZ - box.minZ),
                            (random.nextFloat() - .5f) * .1f,
                            -random.nextFloat() * .4f - .8f,
                            (random.nextFloat() - .5f) * .1f);

                    player.getEntityWorld().addParticleClient(ParticleTypes.SMOKE.getType(),
                            box.minX + random.nextFloat() * (box.maxX - box.minX),
                            player.getY(),
                            box.minZ + random.nextFloat() * (box.maxZ - box.minZ),
                            (random.nextFloat() - .5f) * .3f,
                            -random.nextFloat() * .4f - .8f,
                            (random.nextFloat() - .5f) * .3f);
                }
            }


            Vec3d velocity = player.getVelocity();
            double velY = velocity.y;

            double targetSpeed = player.isSneaking() ? 0 : JET_SPEED_CAP;

            if (velY < targetSpeed) {
                velY =  Math.min(velY + JET_ACCELERATION, targetSpeed);

                player.setVelocity(velocity.x, velY, velocity.z);
                player.velocityDirty = true;
            }
        }

        player.getEntityWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, .2f, .5f);
    }

    @Override
    public void serverTick() {

        if (shouldFreeze()) {
            player.fallDistance = 0;
        }

        float regenRate = getRegenRate();
        if (regenRate > 0 && player.getHealth() < player.getMaxHealth()) {
            regenTime += regenRate;

            if (regenTime >= REGEN_REQUIREMENT) {
                regenTime -= REGEN_REQUIREMENT;
                player.heal(1);
            }

            markDirty();
        } else if (regenTime > 0) {
            regenTime = 0;
            markDirty();
        }

        if (topUpCooldown > 0) {
            topUpCooldown--;
            markDirty();
        }

        if (this.soulType == ModSoulTypes.CONSTRUCT && getSoul() < getMaxSoul()) {
            solarSoulTime += player.getEntityWorld().isDay() && player.getEntityWorld().isSkyVisibleAllowingSea(BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ())) ? SUN_MULTIPLIER : 1;

            if (solarSoulTime >= SOLAR_SOUL_TICKS) {
                solarSoulTime -= SOLAR_SOUL_TICKS;
                addSoul(1);
            }

            markDirty();
        }

        if (isJetting()) {
            player.onLanding();
            player.setIgnoreFallDamageFromCurrentExplosion(true);

            if (!player.isCreative() && (!player.isSneaking() || player.isInSwimmingPose() || player.isGliding() || player.getRandom().nextInt(8) == 0)) {
                addSoul(player.isInSwimmingPose() || player.isGliding() ? -2 : -1);
            }
            solarSoulTime = 0;
            markDirty();

            moveWithJet(false);

            if (getSoul() <= 0 || player.getVehicle() != null || shouldFreeze()) {
                setJetting(false);
            }

            player.fallDistance = 0;
        }

        if (jetJammed && player.isOnGround()) {
            jetJammed = false;
            if (soulType == ModSoulTypes.CONSTRUCT && player.isAlive()) {
                this.jetEnabled = true;
            }
            markDirty();
        }

        if (lunging && player.isOnGround()) {
            lunging = false;
            markDirty();
        }
        if (lungeCooldown > 0) {
            lungeCooldown--;
            markDirty();
        }

        if (!hasExperience() && (player.totalExperience > 0 || player.experienceLevel > 0)) {

            player.experienceLevel = 0;
            player.experienceProgress = 0;
            player.totalExperience = 0;
        }

        if (!usesHunger()) {

            HungerManager manager = player.getHungerManager();
            if (manager.getFoodLevel() < 20 || manager.getSaturationLevel() > 0) {
                manager.setSaturationLevel(0f);
                manager.setFoodLevel(20);
            }
        }

        if (focusing) {
            focusTime++;

            if (focusTime > 0 && !player.isCreative()) {
                addSoul(-soulType.getFocusRate());
            }

            if (focusTime >= soulType.getFocusTicks()) {
                boolean continueFocusing = this.soulType.onFocus(player, (float)player.getAttributeValue(ModAttributes.FOCUS_POWER));
                forEachAugment((stack, player) -> {
                    if (stack.getItem() instanceof FocusAugment augment) {
                        augment.onFocus(player, stack, (float)player.getAttributeValue(ModAttributes.FOCUS_POWER));
                    }
                });
                sendSoulEvent(SoulEventPayload.SoulEventType.FOCUS);
                setFocusing(canFocus() && continueFocusing);
            } else if (soul <= 0 || !player.isOnGround()) {
                setFocusing(false);
            }
        }

        if (vanishing) {
            vanishTime++;

            if (!player.isCreative() && !isGhost() && vanishTime > 0 && usesSoul()) {
                addSoul(-VANISH_RATE);
            }

            if (soul <= 0 && usesSoul()) {
                vanishing = false;
                vanishTime = 0;
                markDirty();
            } else if (vanishTime >= (isGhost() ? REVEAL_TICKS : VANISH_TICKS)) {
                setGhost(!isGhost());
                sendSoulEvent(isGhost() ? SoulEventPayload.SoulEventType.VANISH : SoulEventPayload.SoulEventType.REAPPEAR);
            }
        }

        if (curing) {
            cureTime--;

            if (cureTime <= 0) {
                setCuring(false, 0);
                setSoulType(ModSoulTypes.MORTAL);
                player.getHungerManager().setSaturationLevel(15f);
                validateSoulStack();
                player.clearStatusEffects();
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 140, 0));
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

            if (isGhost()) {
                soulDecay--;
                if (soulDecay <= 0) {
                    soulDecay = SOUL_DECAY_TICKS;
                    addSoul(-SOUL_DECAY_AMOUNT);
                }
                if (soul <= 0) {
                    setGhost(false);
                    sendSoulEvent(SoulEventPayload.SoulEventType.REAPPEAR);
                }
            }
        }

        if (soul > getMaxSoul() || soul < 0) {
            setSoul(getSoul());
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
        soulType = SoulType.ofSerializable(readView.getString("soul_type", "hdemise:mortal"));
        regenTime = readView.getFloat("regen_time", 0f);
        solarSoulTime = readView.getFloat("solar_soul_time", 0f);
        jetting = readView.getBoolean("jetting", false);
        jetEnabled = readView.getBoolean("jet_enabled", false);
        jetJammed = readView.getBoolean("jet_jammed", false);
        lunging = readView.getBoolean("lunging", false);
        lungeCooldown = readView.getInt("lunge_cooldown", 0);
        lungeGravity = readView.getDouble("lunge_gravity", 0.08d);

        hollowHp = readView.getFloat("hollow_hp", 12);
        hollowSoul = readView.getInt("hollow_soul", 640);
        hollowTopped = readView.getBoolean("hollow_topped", true);
        topUpCooldown = readView.getInt("top_up_cooldown", 0);

        inventory.readData(readView);
    }

    @Override
    public void writeData(WriteView writeView) {
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
        writeView.putString("soul_type", soulType.getSerializable());
        writeView.putFloat("regen_time", regenTime);
        writeView.putFloat("solar_soul_time", solarSoulTime);
        writeView.putBoolean("jetting", jetting);
        writeView.putBoolean("jet_enabled", jetEnabled);
        writeView.putBoolean("jet_jammed", jetJammed);
        writeView.putBoolean("lunging", lunging);
        writeView.putInt("lunge_cooldown", lungeCooldown);
        writeView.putDouble("lunge_gravity", lungeGravity);

        writeView.putFloat("hollow_hp", hollowHp);
        writeView.putInt("hollow_soul", hollowSoul);
        writeView.putBoolean("hollow_topped", hollowTopped);
        writeView.putInt("top_up_cooldown", topUpCooldown);

        inventory.writeData(writeView);
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

        if (!soulType.burnsInDaylight() || curing || (isGhost() && ghostIgnoresSun) || player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
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

        if (player == null) return null;

        return ModEntityComponents.SOUL.getNullable(player);
    }
}
