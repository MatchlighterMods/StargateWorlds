package ml.sgworlds.dimension;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.IWorldFeature;
import ml.sgworlds.api.world.feature.IFeatureLocator;
import ml.sgworlds.api.world.feature.IPopulate;
import ml.sgworlds.api.world.feature.ITerrainGenerator;
import ml.sgworlds.api.world.feature.ITerrainModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class SGChunkGenerator implements IChunkProvider {

	private SGWorldData worldData;
	private World worldObj;

	private Random rand;
	private NoiseGeneratorOctaves noiseGen4;
	private double[] stoneNoise = new double[256];
	private BiomeGenBase[] biomesForGeneration;
	
	public SGChunkGenerator(World world, SGWorldData worldData) {
		this.worldObj = world;
		this.worldData = worldData;
		this.rand = new Random(worldData.getWorldSeed());
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
	}

	@Override
	public boolean chunkExists(int i, int j) {
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		short[] blockIds = new short[16*16*256];
		byte[] blockMetas = new byte[blockIds.length];
		
		((ITerrainGenerator)worldData.getFeature(FeatureType.TERRAIN_GENERATOR)).generateTerrain(chunkX, chunkZ, blockIds, blockMetas);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
		this.replaceBlocksForBiome(chunkX, chunkZ, blockIds, blockMetas, this.biomesForGeneration);
		
		for (IWorldFeature ft : worldData.getFeatures(FeatureType.TERRAIN_MODIFIFIER)) {
			((ITerrainModifier)ft).generate(worldObj, chunkX, chunkZ, blockIds, blockMetas);
		}

		Chunk chunk = new Chunk(this.worldObj, blockIds, blockMetas, chunkX, chunkZ);
		byte[] abyte1 = chunk.getBiomeArray();

		for (int k = 0; k < abyte1.length; ++k) {
			abyte1[k] = (byte)this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void replaceBlocksForBiome(int chunkX, int chunkZ, short[] blockIds, byte[] blockMetas, BiomeGenBase[] biomes) {
		//ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, par1, par2, par3ArrayOfByte, par4ArrayOfBiomeGenBase);
		//MinecraftForge.EVENT_BUS.post(event);
		//if (event.getResult() == Result.DENY) return;

		byte b0 = 63;
		double d0 = 0.03125D;
		this.stoneNoise = this.noiseGen4.generateNoiseOctaves(this.stoneNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);

		for (int z = 0; z < 16; ++z) {
			for (int x = 0; x < 16; ++x) {
				BiomeGenBase biomegenbase = biomes[x + z * 16];
				float temp = biomegenbase.getFloatTemperature();
				int i1 = (int)(this.stoneNoise[z + x * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int j1 = -1;
				short topBlock = biomegenbase.topBlock;
				short fillBlock = biomegenbase.fillerBlock;

				for (int y = 127; y >= 0; --y) {
					int blIndex = (x * 16 + z) * 128 + y;

					if (y <= 0 + this.rand.nextInt(5)) {
						blockIds[blIndex] = (short)Block.bedrock.blockID;
					} else {
						short curBlock = blockIds[blIndex];

						if (curBlock == 0)
						{
							j1 = -1;
						}
						else if (curBlock == Block.stone.blockID)
						{
							if (j1 == -1)
							{
								if (i1 <= 0)
								{
									topBlock = 0;
									fillBlock = (short)Block.stone.blockID;
								}
								else if (y >= b0 - 4 && y <= b0 + 1)
								{
									topBlock = biomegenbase.topBlock;
									fillBlock = biomegenbase.fillerBlock;
								}

								if (y < b0 && topBlock == 0)
								{
									if (temp < 0.15F)
									{
										topBlock = (short)Block.ice.blockID;
									}
									else
									{
										topBlock = (short)Block.waterStill.blockID;
									}
								}

								j1 = i1;

								if (y >= b0 - 1)
								{
									blockIds[blIndex] = topBlock;
								}
								else
								{
									blockIds[blIndex] = fillBlock;
								}
							}
							else if (j1 > 0)
							{
								--j1;
								blockIds[blIndex] = fillBlock;

								if (j1 == 0 && fillBlock == Block.sand.blockID)
								{
									j1 = this.rand.nextInt(4);
									fillBlock = (short)Block.sandStone.blockID;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Chunk loadChunk(int i, int j) {
		return this.provideChunk(i, j);
	}

	@Override
	public void populate(IChunkProvider ichunkprovider, int chunkX, int chunkZ) {
		BlockSand.fallInstantly = true;
		int k = chunkX * 16;
		int l = chunkZ * 16;
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
		this.rand.setSeed(worldData.getWorldSeed());
		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)chunkX * i1 + (long)chunkZ * j1 ^ worldData.getWorldSeed());
		boolean flag = false;

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag));

		for (IWorldFeature ft : worldData.getFeatures(FeatureType.CHUNK_POPULATOR)) {
			((IPopulate)ft).populate(worldObj, rand, chunkX, chunkZ);
		}

		int k1;
		int l1;
		int i2;

		// TODO Move
		if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && this.rand.nextInt(4) == 0
				&& TerrainGen.populate(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag, LAKE)) {
			k1 = k + this.rand.nextInt(16) + 8;
			l1 = this.rand.nextInt(128);
			i2 = l + this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Block.waterStill.blockID)).generate(this.worldObj, this.rand, k1, l1, i2);
		}

		if (TerrainGen.populate(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag, LAVA) && !flag && this.rand.nextInt(8) == 0) {
			k1 = k + this.rand.nextInt(16) + 8;
			l1 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			i2 = l + this.rand.nextInt(16) + 8;

			if (l1 < 63 || this.rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaStill.blockID)).generate(this.worldObj, this.rand, k1, l1, i2);
			}
		}

		boolean doGen = TerrainGen.populate(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag, DUNGEON);
		for (k1 = 0; doGen && k1 < 8; ++k1) {
			l1 = k + this.rand.nextInt(16) + 8;
			i2 = this.rand.nextInt(128);
			int j2 = l + this.rand.nextInt(16) + 8;
			(new WorldGenDungeons()).generate(this.worldObj, this.rand, l1, i2, j2);
		}

		biomegenbase.decorate(this.worldObj, this.rand, k, l);
		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		k += 8;
		l += 8;

		doGen = TerrainGen.populate(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag, ICE);
		for (k1 = 0; doGen && k1 < 16; ++k1) {
			for (l1 = 0; l1 < 16; ++l1) {
				i2 = this.worldObj.getPrecipitationHeight(k + k1, l + l1);

				if (this.worldObj.isBlockFreezable(k1 + k, i2 - 1, l1 + l)) {
					this.worldObj.setBlock(k1 + k, i2 - 1, l1 + l, Block.ice.blockID, 0, 2);
				}

				if (this.worldObj.canSnowAt(k1 + k, i2, l1 + l)) {
					this.worldObj.setBlock(k1 + k, i2, l1 + l, Block.snow.blockID, 0, 2);
				}
			}
		}

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag));

		BlockSand.fallInstantly = false;
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "SGWorldSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i,
			int j, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkPosition findClosestStructure(World world, String s, int x, int y, int z) {
		ChunkPosition closest = null;
		long dist = -1;
		for (IWorldFeature loc : worldData.getFeatures(FeatureType.FEATURE_LOCATOR)) {
			ChunkPosition cp = ((IFeatureLocator)loc).locateFeature(world, s, x, y, z);
			if (cp == null) continue;

			int dx = cp.x-x, dy=cp.y-y, dz=cp.z-z;
			long nd = dx*dx + dy*dy + dz*dz;
			if (dist < 0 || nd < dist) {
				closest = cp;
				dist = nd;
			}
		}
		return closest;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveExtraData() {}

}
