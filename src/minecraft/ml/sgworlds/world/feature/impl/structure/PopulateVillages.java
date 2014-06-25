package ml.sgworlds.world.feature.impl.structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	public int size = 0;
	public int distance = 32;
	
	public PopulateVillages(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public PopulateVillages(FeatureProvider provider, IWorldData worldData, Random rnd) {
		super(provider, worldData);
		this.size = rnd.nextInt(4);
		this.distance = rnd.nextInt(17) + 16;
		createVGen();
	}
	
	public PopulateVillages(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData);
		this.size = tag.getInteger("size");
		this.distance = tag.getInteger("distance");
		createVGen();
	}
	
	private void createVGen() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("size", Integer.toString(size));
		map.put("distance", Integer.toString(distance));
		
		this.vgen = new MapGenVillage(map);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		tag.setInteger("size", size);
		tag.setInteger("distance", distance);
	}
	
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
