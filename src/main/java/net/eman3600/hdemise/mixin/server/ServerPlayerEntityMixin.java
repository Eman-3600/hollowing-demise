package net.eman3600.hdemise.mixin.server;

import com.mojang.authlib.GameProfile;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.mixin_interfaces.ServerPlayerEntityAccess;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerEntityAccess {
    @Shadow private int syncedExperience;

    protected ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public void hdemise$markXPDirty() {
        this.syncedExperience = -1;
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void hdemise$onDeath(DamageSource damageSource, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this);

        ItemStack stack = sc.getInventory().removeStack(0);

        if (stack.getItem() instanceof SoulItem item) {
            sc.getInventory().setStack(0, item.breakSoul());
        }

        sc.markDirty();
    }
}
