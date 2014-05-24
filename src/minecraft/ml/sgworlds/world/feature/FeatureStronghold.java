package ml.sgworlds.world.feature;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.WorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;
import ml.sgworlds.api.world.feature.earth.IFeatureLocator;
import ml.sgworlds.api.world.feature.earth.IPopulate;
import ml.sgworlds.api.world.feature.earth.ITerrainModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStronghold;

public class FeatureStronghold extends WorldFeatureProvider {

	public FeatureStronghold() {
		super("Strongholds", WorldFeatureType.CHUNK_POPULATOR);
	}

	@Override
	public IWorldFeature generateRandomFeature(Random rand) {
		return new Feature();
	}

	@Override
	public IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag) {
		return new Feature();
	}
	
	private class Feature extends WorldFeature implements IPopulate, IFeatureLocator, ITerrainModifier {

		private MapGenStronghold vgen = new MapGenStronghold();

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
		public void getSecondaryTypes(List<WorldFeatureType> types) {
			types.add(WorldFeatureType.FEATURE_LOCATOR);
			types.add(WorldFeatureType.TERRAIN_MODIFIFIER);
		}
		
	}
}
