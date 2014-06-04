package ml.sgworlds.api.world.feature.types;


public interface ITerrainGenerator {

	public void generateTerrain(int chunkX, int chunkY, short[] blockIds, byte[] blockMetas);
	
	/**
	 * Fill the specified block with the correct filler block.
	 */
	public void setFillerBlock(int x, int y, int z, int blockIndex, short[] blockIds, byte[] blockMetas);

}
