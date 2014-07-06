package ml.sgworlds.world.gen.structure;

import java.util.Random;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class StructureHelper {

	public static boolean shouldGenerateAt(World world, int chunkX, int chunkZ, int minDist, int maxDist) {
		int k = chunkX;
		int l = chunkZ;

		if (chunkX < 0) {
			chunkX -= maxDist - 1;
		}

		if (chunkZ < 0) {
			chunkZ -= maxDist - 1;
		}

		int i1 = chunkX / maxDist;
		int j1 = chunkZ / maxDist;
		Random random = world.setRandomSeed(i1, j1, 14357617);
		i1 *= maxDist;
		j1 *= maxDist;
		i1 += random.nextInt(maxDist - minDist);
		j1 += random.nextInt(maxDist - minDist);

		return (k == i1 && l == j1);
	}
	
	public static int getRotatedX(int x, int z, int rotation) {
		switch (rotation) {
		case 0:
			return x;
		case 1:
			return -z;
		case 2:
			return -x;
		case 3:
			return z;
		}
		return x;
	}
	
	public static int getRotatedZ(int x, int z, int rotation) {
		switch (rotation) {
		case 0:
			return z;
		case 1:
			return x;
		case 2:
			return -z;
		case 3:
			return -x;
		}
		return z;
	}
	
	public static ChunkCoordinates getRotatedCoords(ChunkCoordinates coord, int rotation) {
		return new ChunkCoordinates(getRotatedX(coord.posX, coord.posZ, rotation), coord.posY, getRotatedZ(coord.posX, coord.posZ, rotation));
	}
	
	public static ChunkCoordinates addCoords(ChunkCoordinates a, ChunkCoordinates b) {
		return new ChunkCoordinates(a.posX + b.posX, a.posY + b.posY, a.posZ + b.posZ);
	}
}
