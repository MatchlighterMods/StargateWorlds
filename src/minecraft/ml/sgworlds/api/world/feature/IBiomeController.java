package ml.sgworlds.api.world.feature;

import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

import ml.sgworlds.api.world.IWorldFeature;

public interface IBiomeController extends IWorldFeature {

	public List<BiomeGenBase> getBiomesForSpawn();
	
	public BiomeGenBase getBiomeAt(int x, int z);
	
	public BiomeGenBase[] getBiomesAt(BiomeGenBase[] reuseArray, int x, int z, int width, int length, boolean cacheFlag);
	
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] reuseArray, int x, int z, int width, int length);
	
	public float[] getRainfall(float[] reuseArray, int x, int z, int width, int length);
	
	public float[] getTemperatures(float[] reuseArray, int x, int z, int width, int length);
	
	public void cleanCache();
}
