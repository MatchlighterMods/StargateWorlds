package ml.sgworlds.api.world.feature;

import ml.sgworlds.api.world.IWorldFeature;

public interface ITerrainGenerator extends IWorldFeature {

	public void generateTerrain(int chunkX, int chunkY, short[] blockIds, byte[] blockMetas);

}
