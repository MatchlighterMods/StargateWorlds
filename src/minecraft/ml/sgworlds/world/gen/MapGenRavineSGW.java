package ml.sgworlds.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MapGenRavineSGW extends MapGenExtended {
	private float[] field_75046_d = new float[1024];

	protected void generateRavine(long seed, int chunkX, int chunkZ, short[] blockIds, byte[] blockMetas, double altX, double altY, double altZ, float par12, float par13, float par14, int par15, int par16, double par17) {
		Random random = new Random(seed);
		double chunkCentX = (double)(chunkX * 16 + 8);
		double chunkCentZ = (double)(chunkZ * 16 + 8);
		float f3 = 0.0F;
		float f4 = 0.0F;

		if (par16 <= 0) {
			int j1 = this.range * 16 - 16;
			par16 = j1 - random.nextInt(j1 / 4);
		}

		boolean flag = false;

		if (par15 == -1) {
			par15 = par16 / 2;
			flag = true;
		}

		float f5 = 1.0F;

		for (int k1 = 0; k1 < 128; ++k1) {
			if (k1 == 0 || random.nextInt(3) == 0) {
				f5 = 1.0F + random.nextFloat() * random.nextFloat() * 1.0F;
			}

			this.field_75046_d[k1] = f5 * f5;
		}

		for (; par15 < par16; ++par15) {
			double d6 = 1.5D + (double)(MathHelper.sin((float)par15 * (float)Math.PI / (float)par16) * par12 * 1.0F);
			double d7 = d6 * par17;
			d6 *= (double)random.nextFloat() * 0.25D + 0.75D;
			d7 *= (double)random.nextFloat() * 0.25D + 0.75D;
			float f6 = MathHelper.cos(par14);
			float f7 = MathHelper.sin(par14);
			altX += (double)(MathHelper.cos(par13) * f6);
			altY += (double)f7;
			altZ += (double)(MathHelper.sin(par13) * f6);
			par14 *= 0.7F;
			par14 += f4 * 0.05F;
			par13 += f3 * 0.05F;
			f4 *= 0.8F;
			f3 *= 0.5F;
			f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

			if (flag || random.nextInt(4) != 0) {
				double d8 = altX - chunkCentX;
				double d9 = altZ - chunkCentZ;
				double d10 = (double)(par16 - par15);
				double d11 = (double)(par12 + 2.0F + 16.0F);

				if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
					return;
				}

				if (altX >= chunkCentX - 16.0D - d6 * 2.0D && altZ >= chunkCentZ - 16.0D - d6 * 2.0D && altX <= chunkCentX + 16.0D + d6 * 2.0D && altZ <= chunkCentZ + 16.0D + d6 * 2.0D) {
					int minX = MathHelper.floor_double(altX - d6) - chunkX * 16 - 1;
					int maxX = MathHelper.floor_double(altX + d6) - chunkX * 16 + 1;
					int minY = MathHelper.floor_double(altY - d7) - 1;
					int maxY = MathHelper.floor_double(altY + d7) + 1;
					int minZ = MathHelper.floor_double(altZ - d6) - chunkZ * 16 - 1;
					int maxZ = MathHelper.floor_double(altZ + d6) - chunkZ * 16 + 1;

					if (minX < 0) {
						minX = 0;
					}

					if (maxX > 16) {
						maxX = 16;
					}

					if (minY < 1) {
						minY = 1;
					}

					if (maxY > 120) {
						maxY = 120;
					}

					if (minZ < 0) {
						minZ = 0;
					}

					if (maxZ > 16) {
						maxZ = 16;
					}

					boolean oceanBlockFound = false;
					for (int lx = minX; !oceanBlockFound && lx < maxX; ++lx) {
						for (int lz = minZ; !oceanBlockFound && lz < maxZ; ++lz) {
							for (int ly = maxY + 1; !oceanBlockFound && ly >= minY - 1; --ly) {
								int blockIndex = ly<<8 | lz<<4 | lx;

								if (ly >= 0 && ly < 128) {
									oceanBlockFound = blockIds[blockIndex] == Block.waterMoving.blockID || blockIds[blockIndex] == Block.waterStill.blockID;

									if (ly != minY - 1 && lx != minX && lx != maxX - 1 && lz != minZ && lz != maxZ - 1) {
										ly = minY;
									}
								}
							}
						}
					}

					if (!oceanBlockFound) {
						for (int lx = minX; lx < maxX; ++lx) {
							double d12 = ((double)(lx + chunkX * 16) + 0.5D - altX) / d6;

							for (int lz = minZ; lz < maxZ; ++lz) {
								double d13 = ((double)(lz + chunkZ * 16) + 0.5D - altZ) / d6;
								
								if (d12 * d12 + d13 * d13 < 1.0D) {
									for (int ly = maxY - 1; ly >= minY; --ly) {
										double d14 = ((double)ly + 0.5D - altY) / d7;

										int blockIndex = ly<<8 | lz<<4 | lx;
										if ((d12 * d12 + d13 * d13) * (double)this.field_75046_d[ly] + d14 * d14 / 6.0D < 1.0D) {
											clearBlock(blockIds, blockMetas, blockIndex);
										}
									}
								}
							}
						}

						if (flag) {
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Recursively called by generate() (generate) and optionally by itself.
	 */
	@Override
	protected void recursiveGenerate(World par1World, int altChunkX, int altChunkZ, int chunkX, int chunkZ, short[] blockIds, byte[] blockMetas) {
		if (this.rand.nextInt(50) == 0) {
			double altX = (double)(altChunkX * 16 + this.rand.nextInt(16));
			double altY = (double)(this.rand.nextInt(this.rand.nextInt(40) + 8) + 20);
			double altZ = (double)(altChunkZ * 16 + this.rand.nextInt(16));
			byte b0 = 1;

			for (int i1 = 0; i1 < b0; ++i1) {
				float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f2 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
				this.generateRavine(this.rand.nextLong(), chunkX, chunkZ, blockIds, blockMetas, altX, altY, altZ, f2, f, f1, 0, 0, 3.0D);
			}
		}
	}

}
