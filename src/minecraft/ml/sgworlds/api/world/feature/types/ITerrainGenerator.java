package ml.sgworlds.api.world.feature.types;


public interface ITerrainGenerator {

	public void generateTerrain(int chunkX, int chunkY, short[] blockIds, byte[] blockMetas);

}
