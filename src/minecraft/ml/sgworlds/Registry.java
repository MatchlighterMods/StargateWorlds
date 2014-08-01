package ml.sgworlds;

import ml.core.block.BlockDelegator;
import ml.core.block.DelegateBlock;
import ml.core.block.ItemBlockDelegate;
import ml.core.item.DelegateItem;
import ml.core.item.ItemDelegator;
import ml.core.network.PacketHandler;
import ml.sgworlds.block.DelegateEngraved;
import ml.sgworlds.item.DelegateJournal;
import ml.sgworlds.item.DelegateTablet;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import stargatetech2.api.StargateTechAPI;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Registry {

	public static CreativeTabs creativeTab = new SGWorldsCreativeTab();
	public static SGWorldsConfig config;
	
	// Blocks //
	public static BlockDelegator<DelegateBlock> delegatorDecorative;
	public static DelegateEngraved blockEngraved;
	
	public static void registerBlocks() {
		delegatorDecorative = new BlockDelegator<DelegateBlock>(config.decorativeBlockId, Material.rock, new DelegateBlock());
		delegatorDecorative.setCreativeTab(Registry.creativeTab);
		GameRegistry.registerBlock(delegatorDecorative, ItemBlockDelegate.class, "delegateDecorative");
		
		blockEngraved = new DelegateEngraved();
		delegatorDecorative.addSubBlock(0, blockEngraved);
	}

	// Items //
	public static ItemDelegator<DelegateItem> delegatorItem;
	public static DelegateTablet itemTablet;
	public static DelegateJournal itemBookJournal;

	public static void registerItems() {
		delegatorItem = new ItemDelegator<DelegateItem>(config.stargateWorldsItemId-256, new DelegateItem());
		delegatorItem.setCreativeTab(creativeTab);
		GameRegistry.registerItem(delegatorItem, "delegatingI");
		
		itemTablet = new DelegateTablet();
		delegatorItem.addSubItem(1, itemTablet);
		
		itemBookJournal = new DelegateJournal();
		delegatorItem.addSubItem(3, itemBookJournal);
	}

	// TileEntities //
	public static void registerTileEntities() {
		
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
		GameRegistry.addShapelessRecipe(itemBookJournal.createStack(1), Item.book, StargateTechAPI.api().getStackManager().get("naquadahPlate", 1));
	}

}
