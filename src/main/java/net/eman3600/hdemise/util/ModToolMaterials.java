package net.eman3600.hdemise.util;

import net.eman3600.hdemise.init.basics.ModTags;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public interface ModToolMaterials {
    ToolMaterial ALMARITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1050, 8f, 3, 15, ModTags.Items.ALMARITE_REPAIR);
}
