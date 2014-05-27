package ml.sgworlds;

import ml.sgworlds.dimension.SGWorldManager;
import ml.sgworlds.dimension.SGWorldProvider;
import ml.sgworlds.network.ServerConnectionHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid="sgworlds", name="Stargate Worlds", dependencies="required-after:MLCore")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class SGWorlds {
		
		@SidedProxy(serverSide="ml.sgworlds.CommonProxy", clientSide="ml.sgworlds.client.ClientProxy")
		public static CommonProxy proxy;
		
		@Instance("sgworlds")
		public static SGWorlds instance;
		
		public static CreativeTabs sgwTab = new SGWorldsCreativeTab();
		
		public static String netChannel = "sgworlds";

		@EventHandler
		public void PreInit(FMLPreInitializationEvent evt){
			Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
			Registry.config = (SGWorldsConfig)new SGWorldsConfig(cfg).load();
		}
		
		@EventHandler
		public void Init(FMLInitializationEvent evt){
			
			Registry.registerTileEntities();
			Registry.registerBlocks();
			Registry.registerItems();
			Registry.registerRecipes();
			
			NetworkRegistry.instance().registerGuiHandler(instance, proxy);
			Registry.registerPackets();
			
			proxy.load();
			
			DimensionManager.registerProviderType(Registry.config.worldProviderId, SGWorldProvider.class, false);
			
			NetworkRegistry.instance().registerConnectionHandler(new ServerConnectionHandler());
			
		}
		
		@EventHandler
		public void PostInit(FMLPostInitializationEvent e) {

		}
		
		@EventHandler
		public void serverStart(FMLServerStartingEvent evt) {
			SGWorldManager.loadData();
			SGWorldManager.instance.registerDimensions();
		}
		
//		@EventHandler
//		public void serverStopping(FMLServerStoppingEvent evt) {
//			if (SGWorldManager.instance != null) {
//				SGWorldManager.instance.unregisterDimensions();
//			}
//			SGWorldManager.instance = null;
//		}
}
