package ml.sgworlds.world.gen.structure;

import java.util.ArrayList;
import java.util.List;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import stargatetech2.transport.ModuleTransport;

public class RingsPlatforms {

	public static enum PlatformStyle {
		Sandstone,
		SandstoneInset,
		Quartz,
		Stonebrick,
	}

	public static void generateRingsPlatform(World world, int x, int y, int z, int rot, PlatformStyle style) {
		StructureBuilder b = new StructureBuilder(world, x, y, z, rot);
		switch (style) {
		case Sandstone:
			b.fillArea(-1, 0,-1, 1, 0, 1, Block.sandStone, 0);
			b.fillArea(-2, 0,-2, 2, 0,-2, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 0));
			b.fillArea(-2, 0, 2, 2, 0, 2, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 2));

			b.fillArea( 2, 0,-1, 2, 0, 1, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 1));
			b.fillArea(-2, 0,-1,-2, 0, 1, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 3));
			break;
		case SandstoneInset:
			b.fillArea(-1, 0,-1, 1, 0, 1, Block.sandStone, 0);
			b.fillArea(-2, 1,-2, 2, 1,-2, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 2));
			b.fillArea(-2, 1, 2, 2, 1, 2, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 0));

			b.fillArea( 2, 1,-1, 2, 1, 1, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 3));
			b.fillArea(-2, 1,-1,-2, 1, 1, Block.stairsSandStone, b.getRotatedMeta(Block.stairsSandStone, 1));
			break;
		case Quartz:
			b.fillArea(-1, 0,-1, 1, 0, 1, Block.blockNetherQuartz, 0);
			b.fillArea(-2, 0,-2, 2, 0,-2, Block.stairsNetherQuartz, b.getRotatedMeta(Block.stairsNetherQuartz, 0));
			b.fillArea(-2, 0, 2, 2, 0, 2, Block.stairsNetherQuartz, b.getRotatedMeta(Block.stairsNetherQuartz, 2));

			b.fillArea( 2, 0,-1, 2, 0, 1, Block.stairsNetherQuartz, b.getRotatedMeta(Block.stairsNetherQuartz, 1));
			b.fillArea(-2, 0,-1,-2, 0, 1, Block.stairsNetherQuartz, b.getRotatedMeta(Block.stairsNetherQuartz, 3));
			break;
		case Stonebrick:
			b.fillArea(-2, 0,-1, 2, 0, 1, Block.stoneBrick, 0);
			b.fillArea(-1, 0,-2, 1, 0, 2, Block.stoneBrick, 0);
			b.setBlockAt(0, 0, 0, Block.stoneBrick, 3);
			break;
		}
		b.setBlockAt(0, -1, 0, ModuleTransport.transportRing, 0);
	}

	public static void generateRingsPlatform(World world, int x, int z, int rot, PlatformStyle style) {
		List<Integer> ys = new ArrayList<Integer>();
		for (int xn=-3; xn<=3; xn++) {
			for (int zn=-3; zn<=3; zn++) {
				ys.add(world.getHeightValue(x+xn, z+zn));
			}
		}

		int maxValue=0, maxCount=0;
		for (int i : ys) {
			int count = 0;
			for (int j : ys) {
				if (i == j) count++;
			}
			if (count > maxCount) {
				maxCount = count;
				maxValue = i;
			}
		}

		generateRingsPlatform(world, x, maxValue, z, rot, style);
	}
}
