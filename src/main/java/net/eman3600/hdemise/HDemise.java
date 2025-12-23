package net.eman3600.hdemise;

import net.eman3600.hdemise.init.basics.ModBlocks;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.init.basics.ModItemGroups;
import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.eman3600.hdemise.init.event.ModCallbacks;
import net.eman3600.hdemise.init.event.ModMessages;
import net.eman3600.hdemise.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

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
		ModCallbacks.registerCallbacks();
		ModMessages.registerPackets();
		ModMessages.registerC2SReceivers();
		ModWorldGeneration.generateWorldGen();
	}
}