package ml.sgworlds.api.world.feature.prefab;

import java.util.List;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.WorldFeature;
import ml.sgworlds.api.world.feature.ITerrainGenerator;

public abstract class BaseTerrainGenerator extends WorldFeature implements ITerrainGenerator {

	public BaseTerrainGenerator(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	private double[] noiseArray;
	
	public short fillBlockId;
	public byte fillBlockMeta;
	public short oceanBlockId;
	public byte oceanBlockMeta;
	
	@Override
	public void getSecondaryTypes(List<FeatureType> types) {}

	@Override
	public void generateTerrain(int chunkX, int chunkZ, short[] blockIds, byte[] blockMetas) {
		int width = 4;
		int height = 16;
		int seaLevel = 63;
		int k = width + 1;
		int b3 = height + 1;
		int l = width + 1;
		this.noiseArray = this.initializeNoiseField(this.noiseArray, chunkX * width, 0, chunkZ * width, k, b3, l);

		for (int majorX = 0; majorX < width; ++majorX) {
			for (int majorZ = 0; majorZ < width; ++majorZ) {
				for (int majorY = 0; majorY < height; ++majorY) {
					double d0 = 0.125D;
					double d1 = this.noiseArray[((majorX + 0) * l + majorZ + 0) * b3 + majorY + 0];
					double d2 = this.noiseArray[((majorX + 0) * l + majorZ + 1) * b3 + majorY + 0];
					double d3 = this.noiseArray[((majorX + 1) * l + majorZ + 0) * b3 + majorY + 0];
					double d4 = this.noiseArray[((majorX + 1) * l + majorZ + 1) * b3 + majorY + 0];
					double d5 = (this.noiseArray[((majorX + 0) * l + majorZ + 0) * b3 + majorY + 1] - d1) * d0;
					double d6 = (this.noiseArray[((majorX + 0) * l + majorZ + 1) * b3 + majorY + 1] - d2) * d0;
					double d7 = (this.noiseArray[((majorX + 1) * l + majorZ + 0) * b3 + majorY + 1] - d3) * d0;
					double d8 = (this.noiseArray[((majorX + 1) * l + majorZ + 1) * b3 + majorY + 1] - d4) * d0;

					int y = majorY*8;
					for (int minorY = 0; minorY < 8; ++minorY) {
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						int x = majorX*4;
						for (int minorX = 0; minorX < 4; ++minorX) {
							int j2 = minorX + majorX * 4 << 11 | 0 + majorZ * 4 << 7 | majorY * 8 + minorY;
							double d14 = 0.25D;
							double d15 = (d11 - d10) * d14;
							double d16 = d10 - d15;

							int z = majorZ*4;
							for (int minorZ = 0; minorZ < 4; ++minorZ) {
								int apos = y<<8 | z<<4 | x;
								short block=0;
								byte meta=0;
								
								if ((d16 += d15) > 0.0D) {
									block = fillBlockId;
									meta = fillBlockMeta;
								} else if (y < seaLevel) {
									block = oceanBlockId;
									meta = oceanBlockMeta;
								}
								blockIds[apos] = block;
								blockMetas[apos] = meta;
								
								z++;
							}

							d10 += d12;
							d11 += d13;
							
							x++;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
						
						y++;
					}
				}
			}
		}			
	}

	public abstract double[] initializeNoiseField(double[] par1ArrayOfDouble, int subX, int subY, int subZ, int sizeX, int sizeY, int sizeZ);

}
