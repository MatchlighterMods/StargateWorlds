package ml.sgworlds.world.feature;

import static net.minecraft.world.biome.BiomeGenBase.forest;
import static net.minecraft.world.biome.BiomeGenBase.forestHills;
import static net.minecraft.world.biome.BiomeGenBase.jungle;
import static net.minecraft.world.biome.BiomeGenBase.jungleHills;
import static net.minecraft.world.biome.BiomeGenBase.plains;
import static net.minecraft.world.biome.BiomeGenBase.taiga;
import static net.minecraft.world.biome.BiomeGenBase.taigaHills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.IBiomeController;
import ml.sgworlds.api.world.feature.prefab.BaseBiomeController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerSwampRivers;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;

public class BiomeControllerSized extends BaseBiomeController implements IBiomeController {

	public List<BiomeGenBase> allowedBiomes;
	private GenLayer genBiomes;
	private GenLayer biomeIndexLayer;
	private BiomeCache biomeCache;
	private List biomesToSpawnIn = new ArrayList<BiomeGenBase>(Arrays.asList(forest, plains, taiga, taigaHills, forestHills, jungle, jungleHills));;
	protected int zoomFactor;

	public BiomeControllerSized(FeatureProvider provider, IWorldData worldData, int zoom, List<BiomeGenBase> biomes) {
		super(provider, worldData);
		this.zoomFactor = zoom;

		this.biomeCache = new BiomeCache(worldData.getWorldProvider().worldChunkMgr);
		this.allowedBiomes = biomes;

		GenLayer[] agenlayer = initializeAllBiomeGenerators(worldData.getWorldProvider().getSeed(), WorldType.DEFAULT);
		this.genBiomes = agenlayer[0];
		this.biomeIndexLayer = agenlayer[1];
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readNBTData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BiomeGenBase> getBiomesForSpawn() {
		return biomesToSpawnIn;
	}

	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] reuseArray, int x, int z, int width, int length) {
		IntCache.resetIntCache();

		if (reuseArray == null || reuseArray.length < width * length) {
			reuseArray = new BiomeGenBase[width * length];
		}

		int[] aint = this.genBiomes.getInts(x, z, width, length);

		for (int i1 = 0; i1 < width * length; ++i1) {
			reuseArray[i1] = BiomeGenBase.biomeList[aint[i1]];
		}

		return reuseArray;
	}

	private GenLayer[] initializeAllBiomeGenerators(long worldSeed, WorldType par2WorldType) {
		GenLayerIsland genlayerisland = new GenLayerIsland(1L);
		GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
		GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
		GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
		genlayeraddisland = new GenLayerAddIsland(2L, genlayerzoom);
		GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayeraddisland);
		genlayerzoom = new GenLayerZoom(2002L, genlayeraddsnow);
		genlayeraddisland = new GenLayerAddIsland(3L, genlayerzoom);
		genlayerzoom = new GenLayerZoom(2003L, genlayeraddisland);
		genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
		GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);

		GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayeraddmushroomisland, 0);
		GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
		genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, zoomFactor + 1);
		GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
		GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);

		GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayeraddmushroomisland, 0);
		GenLayerBiome genlayerbiome = new GenLayerBiome(200L, genlayer1, par2WorldType);
		genlayer1 = GenLayerZoom.magnify(1000L, genlayerbiome, 2);
		Object object = new GenLayerHills(1000L, genlayer1);

		for (int j = 0; j < zoomFactor; ++j) {
			object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

			if (j == 0) {
				object = new GenLayerAddIsland(3L, (GenLayer)object);
			}

			if (j == 1) {
				object = new GenLayerShore(1000L, (GenLayer)object);
			}

			if (j == 1) {
				object = new GenLayerSwampRivers(1000L, (GenLayer)object);
			}
		}

		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
		GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
		GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
		genlayerrivermix.initWorldGenSeed(worldSeed);
		genlayervoronoizoom.initWorldGenSeed(worldSeed);
		return new GenLayer[] {genlayerrivermix, genlayervoronoizoom, genlayerrivermix};
	}

}
