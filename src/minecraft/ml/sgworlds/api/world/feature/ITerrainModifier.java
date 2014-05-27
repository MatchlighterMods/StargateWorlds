package ml.sgworlds.api.world.feature;

import ml.sgworlds.api.world.IWorldFeature;
import net.minecraft.world.World;

public interface ITerrainModifier extends IWorldFeature {

	public void generate(World world, int chunkX, int chunkY, short[] blockIds, byte[] blockMetas);
	
}
