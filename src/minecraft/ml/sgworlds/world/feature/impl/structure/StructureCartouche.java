package ml.sgworlds.world.feature.impl.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ml.core.world.structure.StructureHelper;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IStructureProvider;
import ml.sgworlds.world.gen.structure.ComponentCartouche;
import ml.sgworlds.world.gen.structure.ScatteredStructureStart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureCartouche extends WorldFeature implements IStructureProvider {

	public static final List<BiomeGenBase> validBiomes = Arrays.asList(new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desertHills});
	
	public int minSeparation = 8, maxSeparation = 16; 

	public StructureCartouche(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	public StructureCartouche(FeatureProvider provider, IWorldData worldData, int minSeparation, int maxSeparation) {
		this(provider, worldData);
		this.minSeparation = minSeparation;
		this.maxSeparation = maxSeparation;
	}
	
	@Override
	public boolean willProvideStructureFor(World world, int chunkX, int chunkZ) {
		return canSpawnStructureAtCoords(world, chunkX, chunkZ, minSeparation, maxSeparation);
	}

	@Override
	public StructureStart getStructureStart(World world, Random rand, int chunkX, int chunkZ) {
		return createStructureStart(world, rand, chunkX, chunkZ);
	}

	@Override
	public StructureStrata getStrata() {
		return StructureStrata.Underground;
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

	public static boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ, int minDist, int maxDist) {
		return StructureHelper.shouldGenerateAt(world, chunkX, chunkZ, minDist, maxDist) &&
				validBiomes.contains(world.getWorldChunkManager().getBiomeGenAt(chunkX * 16 + 8, chunkZ * 16 + 8));
	}
	
	public static StructureStart createStructureStart(World world, Random rand, int chunkX, int chunkZ) {
		return new ScatteredStructureStart(world, rand, chunkX, chunkZ, new ComponentCartouche(new ChunkCoordinates(chunkX*16+8, 40, chunkZ*16+8), rand.nextInt(4)));
	}
}
