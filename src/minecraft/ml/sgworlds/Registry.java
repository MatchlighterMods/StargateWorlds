package ml.sgworlds;

import ml.core.network.PacketHandler;
import ml.sgworlds.block.BlockEngraved;
import ml.sgworlds.block.tile.TileEntityEngraved;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import net.minecraft.block.Block;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	/**
	 * Takes the place of Blocks.air for quick 1.7.2 conversion
	 */
	public static final Block blockAir = null;
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
