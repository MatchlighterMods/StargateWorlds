package ml.sgworlds.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


public class MapGenCavesSGW extends MapGenExtended {
	/**
	 * Generates a larger initial cave node than usual. Called 25% of the time.
	 */
	protected void generateLargeCaveNode(long par1, int par3, int par4, short[] blockIds, byte[] blockMetas, double par6, double par8, double par10) {
		this.generateCaveNode(par1, par3, par4, blockIds, blockMetas, par6, par8, par10, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	/**
	 * Generates a node in the current cave system recursion tree.
	 */
	protected void generateCaveNode(long seed, int chunkX, int chunkZ, short[] blockIds, byte[] blockMetas, double altX, double altY, double altZ, float par12, float par13, float par14, int par15, int par16, double par17) {
		double chunkCentX = (double)(chunkX * 16 + 8);
		double chunkCentZ = (double)(chunkZ * 16 + 8);
		float f3 = 0.0F;
		float f4 = 0.0F;
		Random random = new Random(seed);

		if (par16 <= 0) {
			int j1 = this.range * 16 - 16;
			par16 = j1 - random.nextInt(j1 / 4);
		}

		boolean flag = false;

		if (par15 == -1) {
			par15 = par16 / 2;
			flag = true;
		}

		int k1 = random.nextInt(par16 / 2) + par16 / 4;

		for (boolean flag1 = random.nextInt(6) == 0; par15 < par16; ++par15) {
			double d6 = 1.5D + (double)(MathHelper.sin((float)par15 * (float)Math.PI / (float)par16) * par12 * 1.0F);
			double d7 = d6 * par17;
			float f5 = MathHelper.cos(par14);
			float f6 = MathHelper.sin(par14);
			altX += (double)(MathHelper.cos(par13) * f5);
			altY += (double)f6;
			altZ += (double)(MathHelper.sin(par13) * f5);

			if (flag1) {
				par14 *= 0.92F;
			} else {
				par14 *= 0.7F;
			}

			par14 += f4 * 0.1F;
			par13 += f3 * 0.1F;
			f4 *= 0.9F;
			f3 *= 0.75F;
			f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

			if (!flag && par15 == k1 && par12 > 1.0F && par16 > 0) {
				this.generateCaveNode(random.nextLong(), chunkX, chunkZ, blockIds, blockMetas, altX, altY, altZ, random.nextFloat() * 0.5F + 0.5F, par13 - ((float)Math.PI / 2F), par14 / 3.0F, par15, par16, 1.0D);
				this.generateCaveNode(random.nextLong(), chunkX, chunkZ, blockIds, blockMetas, altX, altY, altZ, random.nextFloat() * 0.5F + 0.5F, par13 + ((float)Math.PI / 2F), par14 / 3.0F, par15, par16, 1.0D);
				return;
			}

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

										if (d14 > -0.7D && d12 * d12 + d14 * d14 + d13 * d13 < 1.0D) {
											clearBlock(blockIds, blockMetas, (ly<<8 | lz<<4 | lx)); // TODO Vanilla fills with lava if y<10
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
	protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, short[] blockIds, byte[] blockMetas) {
		int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);

		if (this.rand.nextInt(15) != 0) {
			i1 = 0;
		}

		for (int j1 = 0; j1 < i1; ++j1) {
			double d0 = (double)(par2 * 16 + this.rand.nextInt(16));
			double d1 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
			double d2 = (double)(par3 * 16 + this.rand.nextInt(16));
			int k1 = 1;

			if (this.rand.nextInt(4) == 0) {
				this.generateLargeCaveNode(this.rand.nextLong(), par4, par5, blockIds, blockMetas, d0, d1, d2);
				k1 += this.rand.nextInt(4);
			}

			for (int l1 = 0; l1 < k1; ++l1) {
				float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

				if (this.rand.nextInt(10) == 0) {
					f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
				}

				this.generateCaveNode(this.rand.nextLong(), par4, par5, blockIds, blockMetas, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
			}
		}
	}
}
