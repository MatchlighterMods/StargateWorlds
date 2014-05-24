package ml.sgworlds.api.world.feature.earth;

import java.util.Random;

import net.minecraft.world.World;
import ml.sgworlds.api.world.WorldFeatureProvider.IWorldFeature;

public interface IPopulate extends IWorldFeature {

	public void populate(World world, Random rand, int chunkX, int chunkY);
	
}
