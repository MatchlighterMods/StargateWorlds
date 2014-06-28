package ml.sgworlds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ml.core.world.WorldGenHandler;
import ml.sgworlds.api.RegisterEvent;
import ml.sgworlds.api.SGWorldsAPI;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.SGWFeature;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.prefab.BaseWeatherController;
import ml.sgworlds.network.ServerConnectionHandler;
import ml.sgworlds.world.GenEventHandler;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import ml.sgworlds.world.feature.FeatureManager;
import ml.sgworlds.world.feature.impl.BiomeControllerNative;
import ml.sgworlds.world.feature.impl.BiomeControllerSingle;
import ml.sgworlds.world.feature.impl.BiomeControllerSized;
import ml.sgworlds.world.feature.impl.FeatureCaves;
import ml.sgworlds.world.feature.impl.FeatureRavines;
import ml.sgworlds.world.feature.impl.LightingNormal;
import ml.sgworlds.world.feature.impl.TerrainDefault;
import ml.sgworlds.world.feature.impl.atmosphere.CloudColorNormal;
import ml.sgworlds.world.feature.impl.atmosphere.FogColorNormal;
import ml.sgworlds.world.feature.impl.atmosphere.MoonDefault;
import ml.sgworlds.world.feature.impl.atmosphere.SkyColorNormal;
import ml.sgworlds.world.feature.impl.atmosphere.StarsDefault;
import ml.sgworlds.world.feature.impl.atmosphere.StarsEnd;
import ml.sgworlds.world.feature.impl.atmosphere.StarsTwinkle;
import ml.sgworlds.world.feature.impl.atmosphere.SunDefault;
import ml.sgworlds.world.feature.impl.atmosphere.WeatherClear;
import ml.sgworlds.world.feature.impl.atmosphere.WeatherRainySnowy;
import ml.sgworlds.world.feature.impl.atmosphere.WeatherStormy;
import ml.sgworlds.world.feature.impl.atmosphere.WeatherThunder;
import ml.sgworlds.world.feature.impl.populate.PopulateDungeons;
import ml.sgworlds.world.feature.impl.populate.PopulateLavaLakes;
import ml.sgworlds.world.feature.impl.populate.PopulateNaquadah;
import ml.sgworlds.world.feature.impl.populate.PopulateWaterLakes;
import ml.sgworlds.world.feature.impl.structure.PopulateStrongholds;
import ml.sgworlds.world.feature.impl.structure.PopulateVillages;
import ml.sgworlds.world.feature.impl.structure.StructureCartouche;
import ml.sgworlds.world.gen.structure.ComponentCartouche;
import ml.sgworlds.world.gen.structure.OverworldStructureGen;
import ml.sgworlds.world.gen.structure.ScatteredStructureStart;
import ml.sgworlds.world.gen.structure.city.CityStructureStart;
import ml.sgworlds.world.gen.temples.TempleLibrary;
import ml.sgworlds.world.gen.temples.TemplePillars;
import ml.sgworlds.world.gen.temples.TemplePyramid;
import ml.sgworlds.world.gen.temples.TempleRuins;
import ml.sgworlds.world.gen.temples.TempleUnderwater;
import ml.sgworlds.world.prefab.WorldTest;
import ml.sgworlds.world.prefab.abydos.WorldAbydos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
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
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "sgworlds", name = "Stargate Worlds", dependencies = "required-after:MLCore; required-after:StargateTech2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class SGWorlds {

	@SidedProxy(serverSide = "ml.sgworlds.CommonProxy", clientSide = "ml.sgworlds.client.ClientProxy")
	public static CommonProxy proxy;

	@Instance("sgworlds")
	public static SGWorlds instance;

	public static String netChannel = "sgworlds";
	
	private static List<Class<? extends WorldFeature>> builtIns = new ArrayList<Class<? extends WorldFeature>>();
	
	static {
	}

	@EventHandler
	public void PreInit(FMLPreInitializationEvent evt) {
		Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
		Registry.config = (SGWorldsConfig) new SGWorldsConfig(cfg).load();

		Registry.registerTileEntities();
		Registry.registerBlocks();
		Registry.registerItems();
		Registry.registerRecipes();

		DimensionManager.registerProviderType(Registry.config.worldProviderId, SGWorldProvider.class, false);

		NetworkRegistry.instance().registerConnectionHandler( new ServerConnectionHandler());
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		Registry.registerPackets();

		GenEventHandler geh = new GenEventHandler();
		MinecraftForge.ORE_GEN_BUS.register(geh);
		MinecraftForge.TERRAIN_GEN_BUS.register(geh);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new EventListener());
	}

	@EventHandler
	public void Init(FMLInitializationEvent evt) {
		
		proxy.load();

		APIImplementation.initAPI();
		SGWorldsAPI api = SGWorldsAPI.getSGWorldsAPI();

		// Features
		registerFeatures();
		MinecraftForge.EVENT_BUS.post(new RegisterEvent.RegisterFeatures(FeatureManager.instance));
		
		// Gate Temples
		api.registerGateTemple(new TempleLibrary());
		api.registerGateTemple(new TemplePillars());
		api.registerGateTemple(new TemplePyramid());
		api.registerGateTemple(new TempleRuins());
		//api.registerGateTemple(new TempleUnderground());
		api.registerGateTemple(new TempleUnderwater());
		MinecraftForge.EVENT_BUS.post(new RegisterEvent.RegisterGateTemples());
		
		// Static Worlds
		api.registerStaticWorld(new WorldTest());
		api.registerStaticWorld(new WorldAbydos());
		MinecraftForge.EVENT_BUS.post(new RegisterEvent.RegisterStaticWorlds());
		
		// Structure Gens
		MapGenStructureIO.func_143034_b(CityStructureStart.class, "CityStart");
		
		MapGenStructureIO.func_143034_b(ScatteredStructureStart.class, "ScatStart");
		MapGenStructureIO.func_143031_a(ComponentCartouche.class, "SGWCartouche");
		
		WorldGenHandler.instance.registerGenerator("SGWorlds", new OverworldStructureGen());
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

	@EventHandler
	public void handleIMC(IMCEvent evt) {

	}

	@ForgeSubscribe
	public void worldSaved(WorldEvent.Save evt) {
		SGWorldManager.instance.saveData();
	}
	
	private void registerFeatures() {
		FeatureManager fm = FeatureManager.instance;
		// Suns
		fm.registerFeature(SGWFeature.SUN_NORMAL.name(), FeatureType.SUN, SunDefault.class);

		// Moons
		fm.registerFeature(SGWFeature.MOON_NORMAL.name(), FeatureType.MOON, MoonDefault.class);

		// Stars
		fm.registerFeature(SGWFeature.STARS_NORMAL.name(), FeatureType.STARS, StarsDefault.class);
		fm.registerFeature(SGWFeature.STARS_END.name(), FeatureType.STARS, StarsEnd.class, 4, false);
		fm.registerFeature(SGWFeature.STARS_TWINKLE.name(), FeatureType.STARS, StarsTwinkle.class);

		// Biome Controllers
		fm.registerFeature(SGWFeature.BIOME_NATIVE.name(), FeatureType.BIOME_CONTROLLER, BiomeControllerNative.class);
		fm.registerFeature(SGWFeature.BIOME_SINGLE.name(), FeatureType.BIOME_CONTROLLER, BiomeControllerSingle.class);
		fm.registerFeature(SGWFeature.BIOME_SIZED.name(), FeatureType.BIOME_CONTROLLER, BiomeControllerSized.class);

		// Terrain Gens
		fm.registerFeature(SGWFeature.TERRAIN_NORMAL.name(), FeatureType.TERRAIN_GENERATOR, TerrainDefault.class);
		
		// Terrain Mods
		fm.registerFeature(SGWFeature.MOD_CAVES.name(), FeatureType.TERRAIN_MODIFIFIER, FeatureCaves.class, 80, true);
		fm.registerFeature(SGWFeature.MOD_RAVINES.name(), FeatureType.TERRAIN_MODIFIFIER, FeatureRavines.class, 50, true);

		// Populators
		fm.registerFeature(SGWFeature.POPULATE_ORE_NAQUADAH.name(), FeatureType.CHUNK_POPULATOR, PopulateNaquadah.class, 16, true);
		fm.registerFeature(SGWFeature.POPULATE_DUNGEONS.name(), FeatureType.CHUNK_POPULATOR, PopulateDungeons.class, 25, true);
		fm.registerFeature(SGWFeature.POPULATE_STRONGHOLDS.name(), FeatureType.CHUNK_POPULATOR, PopulateStrongholds.class, 5, true);
		fm.registerFeature(SGWFeature.POPULATE_VILLAGES.name(), FeatureType.CHUNK_POPULATOR, PopulateVillages.class, 75, true);
		fm.registerFeature(SGWFeature.POPULATE_LAKES_WATER.name(), FeatureType.CHUNK_POPULATOR, PopulateWaterLakes.class, 50, true);
		fm.registerFeature(SGWFeature.POPULATE_LAKES_LAVA.name(), FeatureType.CHUNK_POPULATOR, PopulateLavaLakes.class, 40, true);

		// Structures
		fm.registerFeature(SGWFeature.STRUCTURE_CARTOUCHE.name(), FeatureType.STRUCTURE_PROVIDER, StructureCartouche.class, 40, true);
		
		// Weather Controllers
		fm.registerFeature(SGWFeature.WEATHER_NORMAL.name(), FeatureType.WEATHER_CONTROLLER, BaseWeatherController.class);
		fm.registerFeature(SGWFeature.WEATHER_CLEAR.name(), FeatureType.WEATHER_CONTROLLER, WeatherClear.class);
		fm.registerFeature(SGWFeature.WEATHER_RAINY.name(), FeatureType.WEATHER_CONTROLLER, WeatherRainySnowy.class);
		fm.registerFeature(SGWFeature.WEATHER_STORMY.name(), FeatureType.WEATHER_CONTROLLER, WeatherStormy.class);
		fm.registerFeature(SGWFeature.WEATHER_THUNDER.name(), FeatureType.WEATHER_CONTROLLER, WeatherThunder.class);

		// Lighting Controllers
		fm.registerFeature(SGWFeature.LIGHTING_NORMAL.name(), FeatureType.LIGHTING_CONTROLLER, LightingNormal.class);

		// Fog Color
		fm.registerFeature(SGWFeature.FOG_COLOR_NORMAL.name(), FeatureType.FOG_COLOR, FogColorNormal.class);

		// Cloud Color
		fm.registerFeature(SGWFeature.CLOUD_COLOR_NORMAL.name(), FeatureType.CLOUD_COLOR, CloudColorNormal.class);

		// SkyColor
		fm.registerFeature(SGWFeature.SKY_COLOR_NORMAL.name(), FeatureType.SKY_COLOR, SkyColorNormal.class);

		
		for (SGWFeature featId : SGWFeature.values()) {
			if (featId.getClass().isAnnotationPresent(SGWFeature.Default.class)) {
				fm.setDefaultFeatureProvider(fm.getFeatureProvider(featId.name()));
			}
		}
	}

	public static File getSaveFile(String flName) {
		flName = StringUtils.strip(flName, "/");
		flName = flName.replace("/", File.separator);
		return new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + flName + ".dat");
	}
}
