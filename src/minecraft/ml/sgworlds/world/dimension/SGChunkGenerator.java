package ml.sgworlds.world.dimension;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IFeatureLocator;
import ml.sgworlds.api.world.feature.types.IPopulate;
import ml.sgworlds.api.world.feature.types.IStructureProvider.StructureStrata;
import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import ml.sgworlds.world.gen.structure.MapGenStructureExternal;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class SGChunkGenerator implements IChunkProvider {

	private IWorldData worldData;
	private World worldObj;

	private Random rand;
	private NoiseGeneratorOctaves noiseGen4;
	private double[] stoneNoise = new double[256];
	private BiomeGenBase[] biomesForGeneration;

	protected MapGenStructureExternal calloutStructureGenUndg = new MapGenStructureExternal(StructureStrata.Underground);
	protected MapGenStructureExternal calloutStructureGenAbvg = new MapGenStructureExternal(StructureStrata.Aboveground);
	protected MapGenStructureExternal calloutStructureGenSky = new MapGenStructureExternal(StructureStrata.Sky);
	
	public SGChunkGenerator(World world, IWorldData worldData) {
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

		ITerrainGenerator terrainGenerator = ((ITerrainGenerator)worldData.getFeature(FeatureType.TERRAIN_GENERATOR));

		terrainGenerator.generateTerrain(chunkX, chunkZ, blockIds, blockMetas);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
		this.replaceBlocksForBiome(chunkX, chunkZ, blockIds, blockMetas, this.biomesForGeneration);

		calloutStructureGenUndg.generate(this, worldObj, chunkX, chunkZ, null);
		calloutStructureGenAbvg.generate(this, worldObj, chunkX, chunkZ, null);
		calloutStructureGenSky.generate(this, worldObj, chunkX, chunkZ, null);
		
		for (WorldFeature ft : worldData.getFeatures(FeatureType.TERRAIN_MODIFIFIER)) {
			((ITerrainModifier)ft).generate(worldObj, chunkX, chunkZ, terrainGenerator, blockIds, blockMetas);
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
				float temperature = biomegenbase.getFloatTemperature();

				boolean isDesert = biomegenbase == BiomeGenBase.desert;
				double depthBase = isDesert ? 10.0D : 3.0D;
				int surfaceDepth = (int)(this.stoneNoise[z + x * 16] / 3.0D + depthBase + this.rand.nextDouble() * 0.25D);
				int fillCounter = -1;
				short topBlock = biomegenbase.topBlock;
				short fillBlock = biomegenbase.fillerBlock;

				for (int y = worldObj.getActualHeight()-1; y >= 0; --y) {
					int blIndex = y<<8 | z<<4 | x;

					if (y <= 0 + this.rand.nextInt(5)) {
						blockIds[blIndex] = (short)Block.bedrock.blockID;
					} else {
						short curBlock = blockIds[blIndex];

						if (curBlock == 0) {
							fillCounter = -1;
						} else if (curBlock == Block.stone.blockID) {
							if (fillCounter == -1) {
								if (surfaceDepth <= 0) {
									topBlock = 0;
									fillBlock = (short)Block.stone.blockID;
								} else if (y >= b0 - 4 && y <= b0 + 1) {
									topBlock = biomegenbase.topBlock;
									fillBlock = biomegenbase.fillerBlock;
								}

								if (y < b0 && topBlock == 0) {
									if (temperature < 0.15F) {
										topBlock = (short)Block.ice.blockID;
									} else {
										topBlock = (short)Block.waterStill.blockID;
									}
								}

								fillCounter = surfaceDepth;

								if (y >= b0 - 1) {
									blockIds[blIndex] = topBlock;
								} else {
									blockIds[blIndex] = fillBlock;
								}
							} else if (fillCounter > 0) {
								--fillCounter;
								blockIds[blIndex] = fillBlock;

								if (fillCounter == 0 && fillBlock == Block.sand.blockID) {
									fillCounter = (isDesert ? 12 : 0) + this.rand.nextInt(4);
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

		calloutStructureGenUndg.generateStructuresInChunk(worldObj, rand, chunkX, chunkZ);
		calloutStructureGenAbvg.generateStructuresInChunk(worldObj, rand, chunkX, chunkZ);
		calloutStructureGenSky.generateStructuresInChunk(worldObj, rand, chunkX, chunkZ);
		
		for (WorldFeature ft : worldData.getFeatures(FeatureType.CHUNK_POPULATOR)) {
			((IPopulate)ft).populate(worldObj, rand, chunkX, chunkZ);
		}

		int k1;
		int l1;
		int i2;

		// TODO Move

		biomegenbase.decorate(this.worldObj, this.rand, k, l);
		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		k += 8;
		l += 8;

		boolean doGen = TerrainGen.populate(ichunkprovider, worldObj, rand, chunkX, chunkZ, flag, ICE);
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
	public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(i, j);
		return biomegenbase == null ? null : biomegenbase.getSpawnableList(enumcreaturetype);
	}

	@Override
	public ChunkPosition findClosestStructure(World world, String s, int x, int y, int z) {
		ChunkPosition closest = null;
		long dist = -1;
		for (WorldFeature loc : worldData.getFeatures(FeatureType.FEATURE_LOCATOR)) {
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
		calloutStructureGenUndg.generate(this, worldObj, chunkX, chunkZ, null);
		calloutStructureGenAbvg.generate(this, worldObj, chunkX, chunkZ, null);
		calloutStructureGenSky.generate(this, worldObj, chunkX, chunkZ, null);
	}

	@Override
	public void saveExtraData() {}

}
