package net.eman3600.hdemise;

import net.eman3600.hdemise.init.basics.*;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.init.event.ModCallbacks;
import net.eman3600.hdemise.init.event.ModMessages;
import net.eman3600.hdemise.init.event.ModScreenHandlerTypes;
import net.eman3600.hdemise.integration.command.ModCommands;
import net.eman3600.hdemise.villager.ModTradeOffers;
import net.eman3600.hdemise.villager.ModVillagers;
import net.eman3600.hdemise.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDemise implements ModInitializer {
	public static final String MODID = "hdemise";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {

		ModAttributes.registerAttributes();
		ModSoulTypes.registerAll();

		ModItemGroups.registerItemGroups();
		ModBlocks.registerAll();
		ModBlockEntities.registerAll();
		ModDataComponentTypes.registerAll();
		ModItems.registerAll();
		ModStatusEffects.registerAll();
		ModPotions.registerAll();
		ModCallbacks.registerCallbacks();
		ModMessages.registerPackets();
		ModMessages.registerC2SReceivers();
		ModWorldGeneration.generateWorldGen();

		ModVillagers.registerVillagers();
		ModTradeOffers.registerTradeOffers();

		ModCommands.registerAll();
		ModScreenHandlerTypes.registerAll();

		ModRecipes.registerAll();

		CompostingChanceRegistry.INSTANCE.add(ModItems.SOULROOT_BULB, 0.4f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.SOULROOT_SEEDS, 0.2f);

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, ModItems.SOULROOT_BULB, ModPotions.SOUL_REGEN);
			builder.registerPotionRecipe(ModPotions.SOUL_REGEN, Items.REDSTONE, ModPotions.LONG_SOUL_REGEN);
			builder.registerPotionRecipe(ModPotions.SOUL_REGEN, Items.GLOWSTONE_DUST, ModPotions.STRONG_SOUL_REGEN);

			builder.registerPotionRecipe(ModPotions.SOUL_REGEN, Items.FERMENTED_SPIDER_EYE, ModPotions.CHAINED);
			builder.registerPotionRecipe(ModPotions.LONG_SOUL_REGEN, Items.FERMENTED_SPIDER_EYE, ModPotions.LONG_CHAINED);
			builder.registerPotionRecipe(ModPotions.CHAINED, Items.REDSTONE, ModPotions.LONG_CHAINED);
		});
	}
}