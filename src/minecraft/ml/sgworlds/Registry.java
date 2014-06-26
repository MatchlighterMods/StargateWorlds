package ml.sgworlds;

import ml.core.block.BlockDelegator;
import ml.core.block.Delegate;
import ml.core.block.ItemBlockDelegate;
import ml.core.network.PacketHandler;
import ml.sgworlds.block.DelegateEngraved;
import ml.sgworlds.block.tile.TileEntityEngraved;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	public static CreativeTabs creativeTab = new SGWorldsCreativeTab();
	public static SGWorldsConfig config;
	
	// Blocks //
	public static BlockDelegator<Delegate> blockDelegate;
	public static DelegateEngraved delegateEngraved;
	
	public static void registerBlocks() {
		blockDelegate = new BlockDelegator<Delegate>(config.engravedBlockId, Material.rock, new Delegate());
		blockDelegate.setCreativeTab(Registry.creativeTab);
		GameRegistry.registerBlock(blockDelegate, ItemBlockDelegate.class, "delegating");
		
		delegateEngraved = new DelegateEngraved();
		blockDelegate.addSubBlock(1, delegateEngraved);
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
