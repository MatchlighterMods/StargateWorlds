package ml.sgworlds.world.feature;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.feature.ITerrainGenerator;
import ml.sgworlds.api.world.feature.prefab.BaseTerrainGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class TerrainDefault extends FeatureProvider {

	public TerrainDefault() {
		super("DefaultTerrain", FeatureType.TERRAIN_GENERATOR, Feature.class);
	}

	private class Feature extends BaseTerrainGenerator implements ITerrainGenerator {

		private NoiseGeneratorOctaves noiseGen1;
		private NoiseGeneratorOctaves noiseGen2;
		private NoiseGeneratorOctaves noiseGen3;
		private NoiseGeneratorOctaves noiseGen4;
		public NoiseGeneratorOctaves noiseGen5;
		public NoiseGeneratorOctaves noiseGen6;

		double[] noise3;
		double[] noise1;
		double[] noise2;
		double[] noise5;
		double[] noise6;
		
		float[] parabolicField;
		private BiomeGenBase[] biomesForGeneration;
		private IWorldData worldData;
		
		public Feature(FeatureProvider provider, IWorldData worldData) {
			super(provider, worldData);
		}
		
		@Override
		public FeatureProvider getProvider() {
			return TerrainDefault.this;
		}

		@Override
		public void writeNBTData(NBTTagCompound tag) {
			// TODO
		}
		
		@Override
		public void readNBTData(NBTTagCompound tag) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public double[] initializeNoiseField(double[] par1ArrayOfDouble, int subX, int subY, int subZ, int sizeX, int sizeY, int sizeZ) {
			//			ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(this, par1ArrayOfDouble, par2, par3, par4, par5, par6, par7);
			//			MinecraftForge.EVENT_BUS.post(event);
			//			if (event.getResult() == Result.DENY) return event.noisefield;
			
			this.biomesForGeneration = worldData.getWorldProvider().worldChunkMgr.getBiomesForGeneration(null, subX-2, subZ-2, sizeX+5, sizeZ+5);

			if (par1ArrayOfDouble == null) {
				par1ArrayOfDouble = new double[sizeX * sizeY * sizeZ];
			}

			if (this.parabolicField == null) {
				this.parabolicField = new float[25];
				for (int k1 = -2; k1 <= 2; ++k1) {
					for (int l1 = -2; l1 <= 2; ++l1) {
						float f = 10.0F / MathHelper.sqrt_float((float)(k1 * k1 + l1 * l1) + 0.2F);
						this.parabolicField[k1 + 2 + (l1 + 2) * 5] = f;
					}
				}
			}

			double d0 = 684.412D;
			double d1 = 684.412D;
			this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, subX, subZ, sizeX, sizeZ, 1.121D, 1.121D, 0.5D);
			this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, subX, subZ, sizeX, sizeZ, 200.0D, 200.0D, 0.5D);
			this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, subX, subY, subZ, sizeX, sizeY, sizeZ, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
			this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, subX, subY, subZ, sizeX, sizeY, sizeZ, d0, d1, d0);
			this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, subX, subY, subZ, sizeX, sizeY, sizeZ, d0, d1, d0);
			boolean flag = false;
			boolean flag1 = false;
			int i2 = 0;
			int j2 = 0;

			for (int k2 = 0; k2 < sizeX; ++k2)
			{
				for (int l2 = 0; l2 < sizeZ; ++l2)
				{
					float f1 = 0.0F;
					float f2 = 0.0F;
					float f3 = 0.0F;
					byte b0 = 2;
					BiomeGenBase biomegenbase = this.biomesForGeneration[k2 + 2 + (l2 + 2) * (sizeX + 5)];

					for (int i3 = -b0; i3 <= b0; ++i3)
					{
						for (int j3 = -b0; j3 <= b0; ++j3)
						{
							BiomeGenBase biomegenbase1 = this.biomesForGeneration[k2 + i3 + 2 + (l2 + j3 + 2) * (sizeX + 5)];
							float f4 = this.parabolicField[i3 + 2 + (j3 + 2) * 5] / (biomegenbase1.minHeight + 2.0F);

							if (biomegenbase1.minHeight > biomegenbase.minHeight)
							{
								f4 /= 2.0F;
							}

							f1 += biomegenbase1.maxHeight * f4;
							f2 += biomegenbase1.minHeight * f4;
							f3 += f4;
						}
					}

					f1 /= f3;
					f2 /= f3;
					f1 = f1 * 0.9F + 0.1F;
					f2 = (f2 * 4.0F - 1.0F) / 8.0F;
					double d2 = this.noise6[j2] / 8000.0D;

					if (d2 < 0.0D)
					{
						d2 = -d2 * 0.3D;
					}

					d2 = d2 * 3.0D - 2.0D;

					if (d2 < 0.0D)
					{
						d2 /= 2.0D;

						if (d2 < -1.0D)
						{
							d2 = -1.0D;
						}

						d2 /= 1.4D;
						d2 /= 2.0D;
					}
					else
					{
						if (d2 > 1.0D)
						{
							d2 = 1.0D;
						}

						d2 /= 8.0D;
					}

					++j2;

					for (int k3 = 0; k3 < sizeY; ++k3)
					{
						double d3 = (double)f2;
						double d4 = (double)f1;
						d3 += d2 * 0.2D;
						d3 = d3 * (double)sizeY / 16.0D;
						double d5 = (double)sizeY / 2.0D + d3 * 4.0D;
						double d6 = 0.0D;
						double d7 = ((double)k3 - d5) * 12.0D * 128.0D / 128.0D / d4;

						if (d7 < 0.0D)
						{
							d7 *= 4.0D;
						}

						double d8 = this.noise1[i2] / 512.0D;
						double d9 = this.noise2[i2] / 512.0D;
						double d10 = (this.noise3[i2] / 10.0D + 1.0D) / 2.0D;

						if (d10 < 0.0D)
						{
							d6 = d8;
						}
						else if (d10 > 1.0D)
						{
							d6 = d9;
						}
						else
						{
							d6 = d8 + (d9 - d8) * d10;
						}

						d6 -= d7;

						if (k3 > sizeY - 4)
						{
							double d11 = (double)((float)(k3 - (sizeY - 4)) / 3.0F);
							d6 = d6 * (1.0D - d11) + -10.0D * d11;
						}

						par1ArrayOfDouble[i2] = d6;
						++i2;
					}
				}
			}

			return par1ArrayOfDouble;
		}

	}

}
