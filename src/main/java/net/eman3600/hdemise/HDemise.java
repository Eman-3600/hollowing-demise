package net.eman3600.hdemise;

import net.eman3600.hdemise.init.ModCallbacks;
import net.eman3600.hdemise.init.ModItems;
import net.eman3600.hdemise.init.ModMessages;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDemise implements ModInitializer {
	public static final String MODID = "hdemise";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {


		ModItems.registerAll();
		ModCallbacks.registerCallbacks();
		ModMessages.registerC2SPackets();
	}
}