package ml.sgworlds.world.feature.impl;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import ml.sgworlds.world.gen.MapGenCavesSGW;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FeatureCaves extends WorldFeature implements ITerrainModifier {
	
	MapGenCavesSGW caveGen = new MapGenCavesSGW();

	public FeatureCaves(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void generate(World world, int chunkX, int chunkY, ITerrainGenerator terrainGenerator, short[] blockIds, byte[] blockMetas) {
		caveGen.generate(world.getChunkProvider(), world, chunkX, chunkY, terrainGenerator, blockIds, blockMetas);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
