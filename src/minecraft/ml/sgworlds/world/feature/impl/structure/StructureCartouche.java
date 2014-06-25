package ml.sgworlds.world.feature.impl.structure;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IStructureProvider;
import ml.sgworlds.world.gen.structure.ScatteredStructureStart;

public class StructureCartouche extends WorldFeature implements IStructureProvider {

	public StructureCartouche(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public boolean willProvideStructureFor(World world, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StructureStart getStructureStart(World world, Random rand, int chunkX, int chunkZ) {
		return new ScatteredStructureStart(world, rand, chunkX, chunkZ, null); // TODO
	}

	@Override
	public StructureStrata getStrata() {
		return StructureStrata.Aboveground;
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
