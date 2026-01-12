package net.eman3600.hdemise.item;

import net.eman3600.hdemise.init.basics.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;

public class HoeveItem extends Item {
    public HoeveItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.tool(material, ModTags.Blocks.HOEVE_MINEABLE, attackDamage, attackSpeed, 0));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult result = super.useOnBlock(context);

        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            if (result == ActionResult.PASS) {
                result = Items.DIAMOND_HOE.useOnBlock(context);
            }
            if (result == ActionResult.PASS) {
                result = Items.DIAMOND_SHOVEL.useOnBlock(context);
            }
        } else {
            if (result == ActionResult.PASS) {
                result = Items.DIAMOND_SHOVEL.useOnBlock(context);
            }
            if (result == ActionResult.PASS) {
                result = Items.DIAMOND_HOE.useOnBlock(context);
            }
        }

        return result;
    }
}
