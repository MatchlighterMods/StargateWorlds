package ml.sgworlds.dimension;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.feature.IBiomeController;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

public class SGChunkManager extends WorldChunkManager {

	private final SGWorldData worlData;
	
	public SGChunkManager(SGWorldData worldData) {
		this.worlData = worldData;
	}
	
	private IBiomeController getBiomeController() {
		return ((IBiomeController)worlData.getFeature(FeatureType.BIOME_CONTROLLER));
	}
	
	@Override
	public List getBiomesToSpawnIn() {
		return getBiomeController().getBiomesForSpawn();
	}
	
	@Override
	public BiomeGenBase getBiomeGenAt(int x, int z) {
		return getBiomeController().getBiomeAt(x, z);
	}
	
	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] reuseArray, int x, int z, int width, int length, boolean cacheFlag) {
		IntCache.resetIntCache();
		if (reuseArray == null || reuseArray.length < width*length) {
			reuseArray = new BiomeGenBase[width*length];
		}
		return getBiomeController().getBiomesAt(reuseArray, x, z, width, length, cacheFlag);
	}
	
	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] reuseArray, int x, int z, int width, int length) {
		IntCache.resetIntCache();
		if (reuseArray == null || reuseArray.length < width*length) {
			reuseArray = new BiomeGenBase[width*length];
		}
		return getBiomeController().getBiomesForGeneration(reuseArray, x, z, width, length);
	}
	
	@Override
	public float[] getRainfall(float[] reuseArray, int x, int z, int width, int length) {
		IntCache.resetIntCache();
		if (reuseArray == null || reuseArray.length < width*length) {
			reuseArray = new float[width*length];
		}
		return getBiomeController().getRainfall(reuseArray, x, z, width, length);
	}
	
	@Override
	public float[] getTemperatures(float[] reuseArray, int x, int z, int width, int length) {
		IntCache.resetIntCache();
		if (reuseArray == null || reuseArray.length < width*length) {
			reuseArray = new float[width*length];
		}
		return getBiomeController().getTemperatures(reuseArray, x, z, width, length);
	}
	
	@Override
	public boolean areBiomesViable(int par1, int par2, int par3, List par4List) {
		IntCache.resetIntCache();
		int l = par1 - par3 >> 2;
		int i1 = par2 - par3 >> 2;
		int j1 = par1 + par3 >> 2;
		int k1 = par2 + par3 >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		BiomeGenBase[] biomes = this.getBiomesForGeneration(null, l, i1, l1, i2);

		for (int j2 = 0; j2 < l1 * i2; ++j2) {
			if (!par4List.contains(biomes[j2])) {
				return false;
			}
		}

		return true;
	}

	public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random) {
		IntCache.resetIntCache();
		int l = par1 - par3 >> 2;
		int i1 = par2 - par3 >> 2;
		int j1 = par1 + par3 >> 2;
		int k1 = par2 + par3 >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		BiomeGenBase[] biomes = this.getBiomesForGeneration(null, l, i1, l1, i2);
		ChunkPosition chunkposition = null;
		int j2 = 0;

		for (int k2 = 0; k2 < l1 * i2; ++k2) {
			int l2 = l + k2 % l1 << 2;
			int i3 = i1 + k2 / l1 << 2;

			if (par4List.contains(biomes[k2]) && (chunkposition == null || par5Random.nextInt(j2 + 1) == 0)) {
				chunkposition = new ChunkPosition(l2, 0, i3);
				++j2;
			}
		}

		return chunkposition;
	}
	
	@Override
	public void cleanupCache() {
		getBiomeController().cleanCache();
	}
}
