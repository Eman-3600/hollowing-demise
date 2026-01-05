package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.mixin_interfaces.LootTableAccess;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements LootTableAccess {
    @Shadow @Final private List<LootPool> pools;

    @Override
    public List<LootPool> hdemise$getPools() {
        return this.pools;
    }
}
