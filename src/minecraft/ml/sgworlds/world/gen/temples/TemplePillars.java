package ml.sgworlds.world.gen.temples;

import java.util.Random;

import ml.sgworlds.api.world.IGateTempleGenerator;
import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class TemplePillars implements IGateTempleGenerator {
	
	private int[] pillarX = {4, 6, 7, 7}, pillarZ = {1, 3, 6, 9};

	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder th = new StructureBuilder(world, gateCoords, gateRotation);
		
		for (int i=6; i>=0; i--) {
			int r = 14 + i*2;
			int rsqr = r*r;
			
			for (int x=-r; x<=r; x++) {
				for (int z=-r; z<=r; z++) {
					if ((x*x + z*z) < rsqr) {
						if (th.getBlockIdAt(x, i, z+6) != 0) {
							Block topBlock = Block.blocksList[(world.getBiomeGenForCoords(th.getAbsX(x, z), th.getAbsZ(x, z))).topBlock];
							th.setBlockAt(x, i, z+6, topBlock, 0);
						}
						th.setBlockAt(x, 1+i, z+6, null, 0);
					}
				}
			}
		}
		
		Random rnd = new Random();
		for (int i=0; i<pillarX.length; i++) {
			pillar(pillarX[i], pillarZ[i], 2+rnd.nextInt(3), th);
			pillar(-pillarX[i], pillarZ[i], 2+rnd.nextInt(3), th);
		}
		
	}

	private void pillar(int x, int z, int h, StructureBuilder th) {
		for (int y=1; y<h; y++) {
			th.setBlockAt(x, y, z, Block.blockNetherQuartz, 2);
		}
		th.setBlockAt(x, h, z, Block.blockNetherQuartz, 1);
	}
	
	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		Random rnd = new Random(world.getSeed());
		int x = rnd.nextInt(500), z = rnd.nextInt(500);
		
		return new ChunkPosition(x, world.getHeightValue(x, z)-1, z);
	}

}
