package ml.sgworlds.world.feature.impl;

import java.util.List;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IBiomeController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class BiomeControllerNative extends WorldFeature implements IBiomeController {

	private WorldChunkManager wcm;
	
	public BiomeControllerNative(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	@Override
	public void onProviderCreated(WorldProvider wprovider) {
		super.onProviderCreated(wprovider);
		this.wcm = new WorldChunkManager(worldData.getWorldSeed(), WorldType.DEFAULT);
	}
	
	@Override
	public void writeNBTData(NBTTagCompound tag) {}

	@Override
	public List<BiomeGenBase> getBiomesForSpawn() {
		return wcm.getBiomesToSpawnIn();
	}

	@Override
	public BiomeGenBase getBiomeAt(int x, int z) {
		return wcm.getBiomeGenAt(x, z);
	}

	@Override
	public BiomeGenBase[] getBiomesAt(BiomeGenBase[] reuseArray, int x, int z, int width, int length, boolean cacheFlag) {
		return wcm.getBiomeGenAt(reuseArray, x, z, width, length, cacheFlag);
	}

	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] reuseArray, int x, int z, int width, int length) {
		return wcm.getBiomesForGeneration(reuseArray, x, z, width, length);
	}

	@Override
	public float[] getRainfall(float[] reuseArray, int x, int z, int width, int length) {
		return wcm.getRainfall(reuseArray, x, z, width, length);
	}

	@Override
	public float[] getTemperatures(float[] reuseArray, int x, int z, int width, int length) {
		return wcm.getTemperatures(reuseArray, x, z, width, length);
	}

	@Override
	public void cleanCache() {
		wcm.cleanupCache();
	}

}
