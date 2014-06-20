package ml.sgworlds;

import ml.core.network.PacketHandler;
import ml.sgworlds.block.BlockEngraved;
import ml.sgworlds.block.tile.TileEntityEngraved;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	public static SGWorldsConfig config;

	public static void registerBlocks() {
		GameRegistry.registerBlock(new BlockEngraved(), "Engraved");
	}

	// Items //

	public static void registerItems() {

	}

	// TileEntities //
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityEngraved.class, "TEEngraved");
	}

	// Packets //
	public static void registerPackets() {
		PacketHandler pkh = new PacketHandler();
		NetworkRegistry.instance().registerChannel(pkh, SGWorlds.netChannel);

		// Packets
		pkh.addHandler(PacketWorldData.class);
		pkh.addHandler(PacketRegisterDimensions.class);
	}

	// Recipes //
	public static void registerRecipes() {

	}
	
}
