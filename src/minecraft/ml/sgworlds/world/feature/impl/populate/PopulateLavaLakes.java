package ml.sgworlds.world.feature.impl.populate;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IPopulate;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenLakes;

public class PopulateLavaLakes extends WorldFeature implements IPopulate {

	public PopulateLavaLakes(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(chunkX*16 + 16, chunkZ*16 + 16);

		if (rand.nextInt(8) == 0) {
			int x = chunkX*16 + rand.nextInt(16) + 8;
			int y = rand.nextInt(120) + 8;
			int z = chunkZ*16 + rand.nextInt(16) + 8;
			if (y < 63 || rand.nextInt(10) == 0)
				(new WorldGenLakes(Block.waterStill.blockID)).generate(world, rand, x, y, z);
		}
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

}
