package ml.sgworlds.world.feature.impl.populate;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IPopulate;

public class PopulateDungeons extends WorldFeature implements IPopulate {

	public PopulateDungeons(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		for (int i = 0; i < 8; ++i) {
			int x = chunkX*16 + rand.nextInt(16) + 8;
			int y = rand.nextInt(128);
			int z = chunkZ*16 + rand.nextInt(16) + 8;
			(new WorldGenDungeons()).generate(world, rand, x, y, z);
		}
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
