package ml.sgworlds.world.gen.temples;

import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import ml.sgworlds.api.world.IGateTempleGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class TempleLibrary implements IGateTempleGenerator {
	
	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder th = new StructureBuilder(world, gateCoords, gateRotation);
		th.invertZ = true;
		
		th.fillArea(-8, -1, -2, 8, 0, 15, Block.stoneDoubleSlab, 0); // Foundation
		th.fillArea(-7, 0, -1, 7, 0, 14, Block.planks, 0); // Floor
		
		th.fillArea(-7, 1, -2, 7, 9, 14, null, 0); // Clear Interior
		
		// Walls
		for (int i=1; i<=8; i++) {
			th.borderArea(-8, i, -2, 8, i, 15, Block.planks, 0);
			th.borderArea(-7, i, -2, 7, i, 15, Block.planks, 0);
		}
		
		// Roof
		for (int i=0; i<4; i++) {
			int x = 8-i, y = 9+i, minz = -2+i, maxz = 15-i;
			th.fillArea(-x, y, minz, x, y, maxz, Block.planks, 0);
			
			th.setBlockAt(-x, y, minz, null, 0);
			th.setBlockAt(x, y, minz, null, 0);
			th.setBlockAt(-x, y, maxz, null, 0);
			th.setBlockAt(x, y, maxz, null, 0);
			
			if (i<3) {
				x -= 2;
				minz += 1;
				maxz -= 1;
				th.fillArea(-x, y, minz, x, y, maxz, null, 0);
			}
		}
		
		// Small Windows
		th.symmetryX = true;
		for (int i=0; i<4; i++) {
			th.setBlockAt(8, 2, 2+3*i, Block.thinGlass, 0);
			th.setBlockAt(8, 7, 2+3*i, Block.thinGlass, 0);
			th.setBlockAt(7, 2, 2+3*i, null, 0);
			th.setBlockAt(7, 7, 2+3*i, null, 0);
		}
		th.symmetryX = false;
		
		for (int x=-6; x<=6; x+=2) {
			th.setBlockAt(x, 7, 15, Block.thinGlass, 0);
		}
		
		// Large Window
		int[] widths = new int[]{5,7,7,7,7,5,5,3};
		for (int i=0; i<widths.length; i++) {
			int o = (widths[i]-1) / 2;
			for (int x=-o; x<=o; x++) {
				th.setBlockAt(x, i+1, -2, Block.thinGlass, 0);
			}
		}
		
		// Top Floor
		th.fillArea(-6, 5, -1, 6, 5, 14, Block.planks, 0);
		
		th.fillArea(-5, 6, -1, 5, 6, 3, Block.fence, 0);
		th.fillArea(-4, 6, 2, 4, 6, 4, Block.fence, 0);
		th.fillArea(-5, 6, 7, 5, 6, 12, Block.fence, 0);
		th.fillArea(-4, 6, 6, 4, 6, 13, Block.fence, 0);
		
		th.fillArea(-4, 5, -1, 4, 6, 2, null, 0);
		th.fillArea(-3, 5, 3, 3, 6, 3, null, 0);
		th.fillArea(-4, 5, 8, 4, 6, 11, null, 0);
		th.fillArea(-3, 5, 7, 3, 6, 12, null, 0);
		
		// Chandelier
		th.fillArea(0, 8, 9, 0, 11, 10, Block.fence, 0);
		th.fillArea(-1, 8, 9, 1, 8, 10, Block.fence, 0);
		th.setBlockAt(0, 8, 8, Block.fence, 0);
		th.setBlockAt(0, 8, 11, Block.fence, 0);
		
		th.setBlockAt(0, 9, 8, Block.torchWood, 0);
		th.setBlockAt(0, 9, 11, Block.torchWood, 0);
		th.setBlockAt(-1, 9, 9, Block.torchWood, 0);
		th.setBlockAt(-1, 9, 10, Block.torchWood, 0);
		th.setBlockAt(1, 9, 9, Block.torchWood, 0);
		th.setBlockAt(1, 9, 10, Block.torchWood, 0);
		
		th.symmetryX = true;
		
		// Interior Bookshelves
		for (int i=0; i<6; i++) {
			for (int k=0; k<6; k++) {
				th.setBlockAt(3 + k/3, 1+k%3, 3+2*i, Block.bookShelf, 0);
			}
		}
		
		for (int k=0; k<6; k++) {
			th.setBlockAt(1, 1+k%3, 10+k/3, Block.bookShelf, 0);
			th.setBlockAt(4, 1+k%3, 0+k/3, Block.bookShelf, 0);
		}
		
		// Wall Bookshelves
		for (int i=0; i<5; i++) {
			for (int k=0; k<6; k++) {
				th.setBlockAt(7, 1+k%3, 3*i+k/3, Block.bookShelf, 0);
				th.setBlockAt(7, 6+k%3, 3*i+k/3, Block.bookShelf, 0);
			}
		}
		
		// Torches
		for (int i=0; i<6; i++) {
			th.setBlockAt(6, 3, -1+3*i, Block.torchWood, 0);
			th.setBlockAt(6, 8, -1+3*i, Block.torchWood, 0);
		}
		
		th.setBlockAt(2, 3, 3, Block.torchWood, 0);
		th.setBlockAt(2, 3, 7, Block.torchWood, 0);
		
		th.symmetryX = false;
		
		// Door
		th.fillArea(-1, 1, 15, 1, 3, 15, Block.thinGlass, 0);
		th.fillArea(0, 1, 15, 0, 2, 15, null, 0);
		
		// Porch
		th.fillArea(-3, 0, 16, 3, 0, 16, Block.stoneDoubleSlab, 0);
		th.fillArea(-2, 0, 17, 2, 0, 17, Block.stoneDoubleSlab, 0);
		th.fillArea(-2, 4, 16, 2, 4, 17, Block.stoneSingleSlab, 0);
		th.fillArea(-1, 4, 16, 1, 4, 16, Block.stoneDoubleSlab, 0);
		
		th.symmetryX = true;
		th.fillArea(2, 1, 17, 2, 3, 17, Block.fence, 0);
		th.symmetryX = false;
		
		// Gate
		th.fillArea(-2, 0, 0, 2, 0, 0, null, 0);
		
	}
	
	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		Random rnd = new Random(world.getSeed());
		int x = rnd.nextInt(500), z = rnd.nextInt(500);
		
		return new ChunkPosition(x, world.getHeightValue(x, z)-1, z);
	}

}
