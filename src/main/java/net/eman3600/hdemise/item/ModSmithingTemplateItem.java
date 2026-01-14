package net.eman3600.hdemise.item;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModSmithingTemplateItem extends SmithingTemplateItem {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;

    private static final Text POLTERIUM_UPGRADE_APPLIES_TO_TEXT = Text.translatable(
                    Util.createTranslationKey("item", Identifier.of(MODID, "polterium_upgrade_template.applies_to"))
            )
            .formatted(DESCRIPTION_FORMATTING);
    private static final Text POLTERIUM_UPGRADE_INGREDIENTS_TEXT = Text.translatable(
                    Util.createTranslationKey("item", Identifier.of(MODID, "polterium_upgrade_template.ingredients"))
            )
            .formatted(DESCRIPTION_FORMATTING);
    private static final Text POLTERIUM_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", Identifier.of(MODID, "polterium_upgrade_template.base_slot_description"))
    );
    private static final Text POLTERIUM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", Identifier.of(MODID, "polterium_upgrade_template.additions_slot_description"))
    );


    public ModSmithingTemplateItem(Text appliesToText, Text ingredientsText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, Settings settings) {
        super(appliesToText, ingredientsText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, settings);
    }


    public static ModSmithingTemplateItem createPolteriumUpgrade(Item.Settings settings) {
        return new ModSmithingTemplateItem(
                POLTERIUM_UPGRADE_APPLIES_TO_TEXT,
                POLTERIUM_UPGRADE_INGREDIENTS_TEXT,
                POLTERIUM_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT,
                POLTERIUM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getPolteriumUpgradeEmptyBaseSlotTextures(),
                getPolteriumUpgradeEmptyAdditionsSlotTextures(),
                settings
        );
    }

    private static List<Identifier> getPolteriumUpgradeEmptyBaseSlotTextures() {
        return List.of();
    }

    private static List<Identifier> getPolteriumUpgradeEmptyAdditionsSlotTextures() {
        return List.of(Identifier.ofVanilla("container/slot/ingot"));
    }
}
