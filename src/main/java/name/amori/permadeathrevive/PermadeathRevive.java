package name.amori.permadeathrevive;

import name.amori.permadeathrevive.item.ModItemGroups;
import name.amori.permadeathrevive.item.ModItems;
import name.amori.permadeathrevive.network.ModNetwork;
import name.amori.permadeathrevive.network.ServerPacketHandler;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermadeathRevive implements ModInitializer {
	public static final String MOD_ID = "permadeath-revive";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModNetwork.registerPayloads();

		ServerPacketHandler.register();

		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
