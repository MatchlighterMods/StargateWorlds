package ml.sgworlds;

import ml.core.block.BlockDelegator;
import ml.core.block.DelegateBlock;
import ml.core.block.ItemBlockDelegate;
import ml.core.item.DelegateItem;
import ml.core.item.ItemDelegator;
import ml.core.network.PacketHandler;
import ml.sgworlds.block.DelegateEngraved;
import ml.sgworlds.block.tile.TileEntityEngraved;
import ml.sgworlds.item.DelegateAncientBook;
import ml.sgworlds.item.DelegateJournal;
import ml.sgworlds.item.DelegateTablet;
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
	public static BlockDelegator<DelegateBlock> delegatorBlock;
	public static DelegateEngraved blockEngraved;
	
	public static void registerBlocks() {
		delegatorBlock = new BlockDelegator<DelegateBlock>(config.stargateWorldsBlockId, Material.rock, new DelegateBlock());
		delegatorBlock.setCreativeTab(Registry.creativeTab);
		GameRegistry.registerBlock(delegatorBlock, ItemBlockDelegate.class, "delegatingB");
		
		blockEngraved = new DelegateEngraved();
		delegatorBlock.addSubBlock(1, blockEngraved);
	}

	// Items //
	public static ItemDelegator<DelegateItem> delegatorItem;
	public static DelegateTablet itemTablet;
	public static DelegateAncientBook itemBookTranslate;
	public static DelegateJournal itemBookJournal;

	public static void registerItems() {
		delegatorItem = new ItemDelegator<DelegateItem>(config.stargateWorldsItemId-256, new DelegateItem());
		delegatorItem.setCreativeTab(creativeTab);
		GameRegistry.registerItem(delegatorItem, "delegatingI");
		
		itemTablet = new DelegateTablet();
		delegatorItem.addSubItem(1, itemTablet);
		
		itemBookTranslate = new DelegateAncientBook();
		delegatorItem.addSubItem(2, itemBookTranslate);
		
		itemBookJournal = new DelegateJournal();
		delegatorItem.addSubItem(3, itemBookJournal);
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
