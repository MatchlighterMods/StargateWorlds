package ml.sgworlds.world.gen.temples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IGateTempleGenerator;
import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class TemplePyramid implements IGateTempleGenerator {

	private static final int[] floorDesign = { 0, 4, 0, 4, 0, 4, 0, 4, 0,11, 0, 4, 0, 4, 0, 4, 0, 4, 0,
											   4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4,
											   0, 4, 0, 4, 0, 4, 0,10, 0, 4, 0,10, 0, 4, 0, 4, 0, 4, 0,
		 									   4, 0, 4,13,11, 0, 4, 0, 4, 0, 4, 0, 4, 0,11,13, 4, 0, 4,
											   0, 4, 0,11, 4,11, 0, 4, 0,10, 0, 4, 0,11, 4,11, 0, 4, 0,
											   4, 0, 4, 0,11, 4,11, 0, 4, 0, 4, 0,11, 4,11, 0, 4, 0, 4,
											   0, 4, 0, 4, 0,11, 4,13, 0, 4, 0,13, 4,11, 0, 4, 0, 4, 0,
											   4, 0, 4, 0, 4, 0,13, 0, 0, 4, 0, 0,13, 0, 4, 0, 4, 0, 4,
											   0, 4, 0, 4, 0, 4, 0, 0, 4, 0, 4, 0, 0, 4, 0, 4, 0, 4, 0,
											   4, 0, 4, 0,10, 0, 4, 4, 0,11, 0, 4, 4, 0,10, 0, 4, 0, 4,
											   0, 4, 0, 4, 0, 4, 0, 0, 4, 0, 4, 0, 0, 4, 0, 4, 0, 4, 0,
											   4, 0, 4, 0, 4, 0,13, 0, 0, 4, 0, 0,13, 0, 4, 0, 4, 0, 4,
											   0, 4, 0, 4, 0,11, 4,13, 0, 4, 0,13, 4,11, 0, 4, 0, 4, 0,
											   4, 0, 4, 0,11, 4,11, 0, 4, 0, 4, 0,11, 4,11, 0, 4, 0, 4,
											   0, 4, 0,11, 4,11, 0, 4, 0,10, 0, 4, 0,11, 4,11, 0, 4, 0,
											   4, 0, 4,13,11, 0, 4, 0, 4, 0, 4, 0, 4, 0,11,13, 4, 0, 4,
											   0, 4, 0, 4, 0, 4, 0,10, 0, 4, 0,10, 0, 4, 0, 4, 0, 4, 0,
											   4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4,
											   0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0,};
	
	private static final int ankh[] = {1, 1, 1,
		  1, 2, 1,
		  0, 1, 0,
		  1, 2, 1,
		  0, 1, 0,
		  0, 1, 0,};
	
	private static final List<BiomeGenBase> validBiomes = new ArrayList<BiomeGenBase>();
	static {
		validBiomes.add(BiomeGenBase.desert);
	}
	
	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder th = new StructureBuilder(world, gateCoords, gateRotation);
		
		// Floor
		th.fillArea(-10, -1, -6, 10, 0, 15, Block.sandStone, 2);
		
		for (int x=-9; x<=9; x++) {
			for (int z=-9; z<=9; z++) {
				int fx = x+9, fz = 18-(z+9);
				if (floorDesign[fx + fz*19] > 0) {
					th.setBlockAt(x, 0, z+4, Block.stainedClay, floorDesign[fx + fz*19]);
				}
			}
		}
		
		// Towers
		th.xSymmetry = true;
		th.wallArea(6, 1, 11, 10, 8, 15, true, false,true, Block.sandStone, 2);
		th.fillArea(7, 9, 12, 9, 9, 14, Block.sandStone, 2);
		
		ankh(8, 2, 15, th);
		th.flipXZ = true;
		ankh(13, 2, 10, th);
		th.xSymmetry = false;
		th.flipXZ = false;
		
		// Pyramid
		int levels = 10;
		int gateOffset = 4;
		for (int y=levels; y>0; y--) {
			int out = levels - y + 1;
			th.borderArea(-out, y, gateOffset-out, out, y, gateOffset+out, Block.sandStone, 2);
			out -= 1;
			th.fillArea(-out, y, gateOffset-out, out, y, gateOffset+out, null, 0);
		}
		
		// Entrance
		th.fillArea(-2, 1, 12, 2, 3, 15, Block.sandStone, 2);
		th.fillArea(-1, 4, 12, 1, 4, 15, Block.sandStone, 2);
		th.fillArea(-1, 1, 12, 1, 3, 15, null, 0);
		
		th.setBlockAt(-2, 4, 15, Block.sandStone, 2);
		th.setBlockAt( 2, 4, 15, Block.sandStone, 2);

		th.setBlockAt(0, 5, 15, Block.sandStone, 1);
		th.setBlockAt(-1, 5, 15, Block.stainedClay, 4);
		th.setBlockAt( 1, 5, 15, Block.stainedClay, 4);
		th.setBlockAt(-2, 5, 15, Block.sandStone, 2);
		th.setBlockAt( 2, 5, 15, Block.sandStone, 2);
		
		th.setBlockAt( 0, 6, 15, Block.sandStone, 2);
		th.setBlockAt(-1, 6, 15, Block.sandStone, 2);
		th.setBlockAt( 1, 6, 15, Block.sandStone, 2);
		
		// Pillars
		th.xSymmetry = true;
		for (int i=0; i<9; i++) {
			int x = 7, z = -4 + 2*i;
			th.setBlockAt(x, 1, z, Block.sandStone, 2);
			th.setBlockAt(x, 2, z, Block.sandStone, 1);
			th.setBlockAt(x, 3, z, Block.sandStone, 2);
		}
		for (int x=-4; x<=4; x+=2) {
			int z = -4;
			th.setBlockAt(x, 1, z, Block.sandStone, 2);
			th.setBlockAt(x, 2, z, Block.sandStone, 1);
		}
		
		for (int i=0; i<4; i++) {
			int h = i==1 || i==2 ? 6 : 4;
			int x = -4, z = -2 + i*4;
			for (int y=1; y<1+h; y++) {
				th.setBlockAt(x, y, z, Block.sandStone, y==2 ? 1 : 2);
				if (y==3) {
					th.setBlockAt(x+1, y, z, Block.torchWood, 0);
					th.setBlockAt(x-1, y, z, Block.torchWood, 0);
				}
			}
		}
		th.xSymmetry = false;
		
	}
	
	private void ankh(int x, int ybot, int z, StructureBuilder th) {
		for (int ax = x-1; ax <= x+1; ax++) {
			for (int sy = 0; sy < 6; sy++) {
				int blk = ankh[(ax+1-x) + (5-sy)*3];
				Block block = blk == 1 ? Block.stainedClay : Block.sandStone;
				int blockMeta = blk == 1 ? 4 : 1;
				if (blk > 0) {
					th.setBlockAt(ax, ybot+sy, z, block, blockMeta);
				}
			}
		}
	}
	
	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		ChunkPosition biomePos = world.provider.worldChunkMgr.findBiomePosition(0, 0, 500, validBiomes, new Random(world.getSeed()));
		if (biomePos == null) return null;
		return new ChunkPosition(biomePos.x, world.getHeightValue(biomePos.x, biomePos.z), biomePos.z);
	}

}
