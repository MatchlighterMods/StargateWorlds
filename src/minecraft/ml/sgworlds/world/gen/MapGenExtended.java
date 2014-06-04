package ml.sgworlds.world.gen;

import java.util.Random;

import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class MapGenExtended {
	protected int range = 8;
	protected Random rand = new Random();
	protected World worldObj;

	protected ITerrainGenerator terrainGenerator;

	public void generate(IChunkProvider par1IChunkProvider, World par2World, int chunkX, int chunkZ, ITerrainGenerator terrainGenerator, short[] blockIds, byte[] blockMetas) {
		this.terrainGenerator = terrainGenerator;
		
		int k = this.range;
		this.worldObj = par2World;
		this.rand.setSeed(par2World.getSeed());
		long l = this.rand.nextLong();
		long i1 = this.rand.nextLong();

		for (int altChunkX = chunkX - k; altChunkX <= chunkX + k; ++altChunkX) {
			for (int altChunkZ = chunkZ - k; altChunkZ <= chunkZ + k; ++altChunkZ) {
				long l1 = (long)altChunkX * l;
				long i2 = (long)altChunkZ * i1;
				this.rand.setSeed(l1 ^ i2 ^ par2World.getSeed());
				this.recursiveGenerate(par2World, altChunkX, altChunkZ, chunkX, chunkZ, blockIds, blockMetas);
			}
		}
	}

	/**
	 * Recursively called by generate() (generate) and optionally by itself.
	 */
	protected void recursiveGenerate(World par1World, int altChunkX, int altChunkZ, int chunkX, int chunkZ, short[] blockIds, byte[] blockMetas) {}

	protected void placeFillBlock(int chunkX, int chunkZ, short[] blocksIds, byte[] blockMetas, int index) {

		short blkId = blocksIds[index];
		Block blk = Block.blocksList[blkId];

		if (blk == Block.bedrock) return;
		
		int x = index & 15;
		int y = index >> 8;
		int z = index >> 4 & 15;

		if (terrainGenerator != null) {
			terrainGenerator.setFillerBlock(x, y, z, index, blocksIds, blockMetas);
		} else {
			blocksIds[index] = 1;
			blockMetas[index] = 0;
		}
	}
}
