package ml.sgworlds.world.feature.impl;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import ml.sgworlds.world.gen.MapGenRavineSGW;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FeatureRavines extends WorldFeature implements ITerrainModifier {
	
	MapGenRavineSGW ravineGen = new MapGenRavineSGW();

	public FeatureRavines(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void generate(World world, int chunkX, int chunkY, ITerrainGenerator terrainGenerator, short[] blockIds, byte[] blockMetas) {
		ravineGen.generate(world.getChunkProvider(), world, chunkX, chunkY, terrainGenerator, blockIds, blockMetas);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
