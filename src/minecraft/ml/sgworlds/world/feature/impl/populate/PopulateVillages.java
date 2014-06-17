package ml.sgworlds.world.feature.impl.populate;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IPopulate;
import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;

public class PopulateVillages extends WorldFeature implements IPopulate, ITerrainModifier {

	private MapGenVillage vgen = new MapGenVillage();
	
	public PopulateVillages(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}
	
	@Override
	public void generate(World world, int chunkX, int chunkY, ITerrainGenerator terrainGenerator, short[] blockIds, byte[] blockMetas) {
		vgen.generate(world.getChunkProvider(), world, chunkX, chunkY, null);
	}

	@Override
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		vgen.generateStructuresInChunk(world, rand, chunkX, chunkZ);
	}

	@Override
	public void getSecondaryTypes(List<FeatureType> types) {
		types.add(FeatureType.TERRAIN_MODIFIFIER);
	}
}
