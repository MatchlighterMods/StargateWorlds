package ml.sgworlds;

import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.SGWFeatures;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.network.ServerConnectionHandler;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import ml.sgworlds.world.feature.FeatureManager;
import ml.sgworlds.world.feature.impl.BiomeControllerNative;
import ml.sgworlds.world.feature.impl.BiomeControllerSingle;
import ml.sgworlds.world.feature.impl.BiomeControllerSized;
import ml.sgworlds.world.feature.impl.MoonDefault;
import ml.sgworlds.world.feature.impl.StarsDefault;
import ml.sgworlds.world.feature.impl.StarsEnd;
import ml.sgworlds.world.feature.impl.StarsTwinkle;
import ml.sgworlds.world.feature.impl.SunDefault;
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
			
			registerFeatures();
			
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
		
		private void registerFeature(SGWFeatures ident, FeatureType type, Class<? extends WorldFeature> featureClass) {
			FeatureManager.instance.registerFeature(ident.toString(), type, featureClass);
		}
		
		public void registerFeatures() {
			FeatureManager fm = FeatureManager.instance;
			//Suns
			registerFeature(SGWFeatures.SUN_NORMAL, FeatureType.SUN, SunDefault.class);
			
			//Moons
			registerFeature(SGWFeatures.MOON_NORMAL, FeatureType.MOON, MoonDefault.class);
			
			//Stars
			registerFeature(SGWFeatures.STARS_NORMAL, FeatureType.STARS, StarsDefault.class);
			registerFeature(SGWFeatures.STARS_END, FeatureType.STARS, StarsEnd.class);
			registerFeature(SGWFeatures.STARS_TWINKLE, FeatureType.STARS, StarsTwinkle.class);
			
			//Biome Controllers
			registerFeature(SGWFeatures.BIOME_NATIVE, FeatureType.BIOME_CONTROLLER, BiomeControllerNative.class);
			fm.registerFeatureProvider(BiomeControllerSingle.provider);
			fm.registerFeatureProvider(BiomeControllerSized.provider);
			
		}
}
