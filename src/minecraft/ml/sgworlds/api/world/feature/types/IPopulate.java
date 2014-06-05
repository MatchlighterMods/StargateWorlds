package ml.sgworlds.api.world.feature.types;

import java.util.Random;

import net.minecraft.world.World;

public interface IPopulate {

	public void populate(World world, Random rand, int chunkX, int chunkZ);
	
}
