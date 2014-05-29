package ml.sgworlds.world.feature.impl;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IFeatureLocator;
import ml.sgworlds.api.world.feature.types.IPopulate;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStronghold;

public class FeatureStronghold extends WorldFeature implements IPopulate, IFeatureLocator, ITerrainModifier {

	private MapGenStronghold vgen = new MapGenStronghold();
	
	public FeatureStronghold(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}
	
	@Override
	public void generate(World world, int chunkX, int chunkY, short[] blockIds, byte[] blockMetas) {
		vgen.generate(world.getChunkProvider(), world, chunkX, chunkY, null);
	}

	@Override
	public ChunkPosition locateFeature(World world, String strucId, int x, int y, int z) {
		return "Stronghold".equals(strucId) && vgen != null ? vgen.getNearestInstance(world, x, y, z) : null;
	}

	@Override
	public void populate(World world, Random rand, int chunkX, int chunkY) {
		vgen.generateStructuresInChunk(world, rand, chunkX, chunkY);
	}

	@Override
	public void getSecondaryTypes(List<FeatureType> types) {
		types.add(FeatureType.FEATURE_LOCATOR);
		types.add(FeatureType.TERRAIN_MODIFIFIER);
	}
}
