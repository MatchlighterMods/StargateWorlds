package ml.sgworlds.world.gen.structure;

import java.util.Random;

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
}