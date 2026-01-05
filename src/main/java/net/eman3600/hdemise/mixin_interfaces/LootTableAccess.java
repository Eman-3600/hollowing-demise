package net.eman3600.hdemise.mixin_interfaces;

import net.minecraft.loot.LootPool;

import java.util.List;

public interface LootTableAccess {
    List<LootPool> hdemise$getPools();
}
