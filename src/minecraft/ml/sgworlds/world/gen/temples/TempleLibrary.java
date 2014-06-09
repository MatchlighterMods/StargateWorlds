package ml.sgworlds.world.gen.temples;

import java.util.Random;

import ml.sgworlds.api.world.IGateTempleGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class TempleLibrary implements IGateTempleGenerator {
	
	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder th = new StructureBuilder(world, gateCoords, gateRotation);
		
		th.fillArea(-8, -1, -2, 8, 0, 15, Block.stoneDoubleSlab, 0); // Foundation
		th.fillArea(-7, 0, -1, 7, 0, 14, Block.planks, 0); // Floor
		
		th.fillArea(-7, 1, -2, 7, 9, 14, null, 0); // Clear Interior
		
		// Walls
		for (int i=1; i<=8; i++) {
			th.borderArea(-8, i, -2, 8, i, 15, Block.planks, 0);
			th.borderArea(-7, i, -2, 7, i, 15, Block.planks, 0);
		}
		
		// Roof
		
		// Small Windows
		for (int i=0; i<4; i++) {
			setBlockSymX(th, 8, 2, 2+3*i, Block.thinGlass, 0);
			setBlockSymX(th, 8, 7, 2+3*i, Block.thinGlass, 0);
			setBlockSymX(th, 7, 2, 2+3*i, null, 0);
			setBlockSymX(th, 7, 7, 2+3*i, null, 0);
		}
		
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
		
		// Interior Bookshelves
		for (int i=0; i<6; i++) {
			for (int k=0; k<6; k++) {
				setBlockSymX(th, 3 + k/3, 1+k%3, 3+2*i, Block.bookShelf, 0);
			}
		}
		
		for (int k=0; k<6; k++) {
			setBlockSymX(th, 1, 1+k%3, 10+k/3, Block.bookShelf, 0);
			setBlockSymX(th, 4, 1+k%3, 0+k/3, Block.bookShelf, 0);
		}
		
		// Wall Bookshelves
		for (int i=0; i<5; i++) {
			for (int k=0; k<6; k++) {
				setBlockSymX(th, 7, 1+k%3, 3*i+k/3, Block.bookShelf, 0);
				setBlockSymX(th, 7, 6+k%3, 3*i+k/3, Block.bookShelf, 0);
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
		
		// Torches
		for (int i=0; i<6; i++) {
			setBlockSymX(th, 6, 3, -1+3*i, Block.torchWood, 0);
			setBlockSymX(th, 6, 8, -1+3*i, Block.torchWood, 0);
		}
		
		// Door
		th.fillArea(-1, 1, 15, 1, 3, 15, Block.thinGlass, 0);
		th.fillArea(0, 1, 15, 0, 2, 15, null, 0);
		
		// Gate
		th.fillArea(-2, 0, 0, 2, 0, 0, null, 0);
		
	}
	
	private void setBlockSymX(StructureBuilder th, int x, int y, int z, Block block, int meta) {
		th.setBlockAt(x, y, z, block, meta);
		th.setBlockAt(-x, y, z, block, meta);
	}

	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		Random rnd = new Random(world.getSeed());
		int x = rnd.nextInt(500), z = rnd.nextInt(500);
		
		return new ChunkPosition(x, world.getHeightValue(x, z)-1, z);
	}

}
