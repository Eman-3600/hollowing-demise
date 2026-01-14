package net.eman3600.hdemise.item;

import net.eman3600.hdemise.init.basics.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;

public class FellerItem extends Item {
    public FellerItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(settings.tool(material, ModTags.Blocks.FELLER_MINEABLE, attackDamage, attackSpeed, 0));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult result = super.useOnBlock(context);

        if (result == ActionResult.PASS) {
            result = Items.DIAMOND_AXE.useOnBlock(context);
        }
        if (result == ActionResult.PASS) {
            result = Items.DIAMOND_HOE.useOnBlock(context);
        }

        return result;
    }
}
