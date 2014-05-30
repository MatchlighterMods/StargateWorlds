package ml.sgworlds;

import java.io.File;

import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.SGWFeatures;
import ml.sgworlds.network.ServerConnectionHandler;
import ml.sgworlds.world.GenEventHandler;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import ml.sgworlds.world.feature.FeatureManager;
import ml.sgworlds.world.feature.impl.BiomeControllerNative;
import ml.sgworlds.world.feature.impl.BiomeControllerSingle;
import ml.sgworlds.world.feature.impl.BiomeControllerSized;
import ml.sgworlds.world.feature.impl.MoonDefault;
import ml.sgworlds.world.feature.impl.PopulateNaquadah;
import ml.sgworlds.world.feature.impl.StarsDefault;
import ml.sgworlds.world.feature.impl.StarsEnd;
import ml.sgworlds.world.feature.impl.StarsTwinkle;
import ml.sgworlds.world.feature.impl.SunDefault;
import ml.sgworlds.world.feature.impl.TerrainDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid="sgworlds", name="Stargate Worlds", dependencies="required-after:MLCore; required-after:StargateTech2")
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
			
			GenEventHandler geh = new GenEventHandler();
			MinecraftForge.ORE_GEN_BUS.register(geh);
			MinecraftForge.TERRAIN_GEN_BUS.register(geh);
			
			MinecraftForge.EVENT_BUS.register(this);
		}
		
		@EventHandler
		public void PostInit(FMLPostInitializationEvent e) {

		}
		
		@EventHandler
		public void serverStart(FMLServerAboutToStartEvent evt) {
			SGWorldManager.loadData();
			SGWorldManager.instance.registerDimensions();
		}
		
		@EventHandler
		public void serverStopped(FMLServerStoppedEvent evt) {
			if (SGWorldManager.instance != null) {
				SGWorldManager.instance.unregisterDimensions();
			}
			SGWorldManager.instance = null;
		}
		
		@ForgeSubscribe
		public void worldSaved(WorldEvent.Save evt) {
			SGWorldManager.instance.saveData();
		}
		
		public void registerFeatures() {
			FeatureManager fm = FeatureManager.instance;
			//Suns
			fm.registerFeature(SGWFeatures.SUN_NORMAL.name(), FeatureType.SUN, SunDefault.class);
			
			//Moons
			fm.registerFeature(SGWFeatures.MOON_NORMAL.name(), FeatureType.MOON, MoonDefault.class);
			
			//Stars
			fm.registerFeature(SGWFeatures.STARS_NORMAL.name(), FeatureType.STARS, StarsDefault.class);
			fm.registerFeature(SGWFeatures.STARS_END.name(), FeatureType.STARS, StarsEnd.class, 4, false);
			fm.registerFeature(SGWFeatures.STARS_TWINKLE.name(), FeatureType.STARS, StarsTwinkle.class);
			
			//Biome Controllers
			fm.registerFeature(SGWFeatures.BIOME_NATIVE.name(), FeatureType.BIOME_CONTROLLER, BiomeControllerNative.class);
			fm.registerFeature(SGWFeatures.BIOME_SINGLE.name(), FeatureType.BIOME_CONTROLLER, BiomeControllerSingle.class);
			fm.registerFeature(SGWFeatures.BIOME_SIZED.name(), FeatureType.BIOME_CONTROLLER, BiomeControllerSized.class);
			
			//Terrain Gens
			fm.registerFeature(SGWFeatures.TERRAIN_NORMAL.name(), FeatureType.TERRAIN_GENERATOR, TerrainDefault.class);
			
			//Populators
			fm.registerFeature(SGWFeatures.POPULATE_ORE_NAQUADAH.name(), FeatureType.CHUNK_POPULATOR, PopulateNaquadah.class, 6, true);
		}
		
		public static File getSaveFile(String flName) {
			flName = StringUtils.strip(flName, "/");
			flName = flName.replace("/", File.separator);
			return new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + flName + ".dat");
		}
}
