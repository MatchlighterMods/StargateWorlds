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

	public int plevels;
	public int gateRoomHeight;
	public int sanctumOffset;
	public int gateOffset = -4;
	
	public TemplePyramid() {
		this(10);
	}
	
	public TemplePyramid(int plevels) {
		this(plevels, 10, Math.max(plevels-20, 0));
	}

	public TemplePyramid(int plevels, int gateRoomHeight, int sanctumOffset) {
		this.plevels = plevels;
		this.gateRoomHeight = gateRoomHeight;
		this.sanctumOffset = sanctumOffset;
	}

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
		th.ioffset.posZ = gateOffset+sanctumOffset;
		
		int front = -(plevels+1), back = plevels;
		
		th.fillDown(-plevels, -2, front, plevels, back, Block.sand, 0);
		
		// Floor
		th.fillArea(-plevels, -1, front, plevels, 0, back, Block.sandStone, 2);
		
		// Towers
		int tCenterX = 8, tCenterZ = front + 2;
		
		th.symmetryX = true;
		th.wallArea(tCenterX-2, 1, tCenterZ-2, tCenterX+2, 8, tCenterZ+2, true, false,true, Block.sandStone, 2);
		th.fillArea(tCenterX-1, 9, tCenterZ-1, tCenterX+1, 9, tCenterZ+1, Block.sandStone, 2);
		
		ankh(tCenterX, 2, tCenterZ-2, th);
		th.flipXZ = true;
		ankh(tCenterZ, 2, tCenterX+2, th);
		th.symmetryX = false;
		th.flipXZ = false;
		
		// Pyramid
		for (int y=plevels; y>0; y--) {
			int out = plevels - y + 1;
			th.borderArea(-out, y, -out, out, y, out, Block.sandStone, 2);
			out -= 1;
			th.fillArea(-out, y, -out, out, y, out, null, 0);
		}
		
		// Inner Sanctum
		th.ioffset.posZ = gateOffset;
		
		th.wallArea(-9, 1, -10, 9, Math.min(plevels - 9, gateRoomHeight), 10, true, false, true, Block.sandStone, 2);
		if (plevels > gateRoomHeight) {
			th.fillArea(-9, gateRoomHeight, -10, 9, gateRoomHeight, 10, Block.sandStone, 2);
		}
		
		for (int x=-9; x<=9; x++) {
			for (int z=-9; z<=9; z++) {
				int fx = x+9, fz = 18-(z+9);
				if (floorDesign[fx + fz*19] > 0) {
					th.setBlockAt(x, 0, z, Block.stainedClay, floorDesign[fx + fz*19]);
				}
			}
		}
		
		// Pillars
		for (int x=-5; x<=5; x+=2) {
			int z = -8, z2 = 8;
			for (int y=1; y<= Math.min(plevels-8, gateRoomHeight); y++) {
				th.setBlockAt(x, y, z, Block.sandStone, y==2 ? 1 : 2);
				th.setBlockAt(x, y, z2, Block.sandStone, y==2 ? 1 : 2);
			}
		}
		
		th.symmetryX = true;
		for (int i=0; i<9; i++) {
			int x = 7, z = 8 - 2*i;
			for (int y=1; y<= Math.min(plevels-7, gateRoomHeight); y++) {
				th.setBlockAt(x, y, z, Block.sandStone, y==2 ? 1 : 2);
			}
		}
		
		for (int i=0; i<4; i++) {
			int h = Math.min(plevels-(i==1 || i==2 ? 4 : 6), gateRoomHeight);
			int x = -4, z = 6 - i*4;
			for (int y=1; y<1+h; y++) {
				th.setBlockAt(x, y, z, Block.sandStone, y==2 ? 1 : 2);
				if (y==3) {
					th.setBlockAt(x+1, y, z, Block.torchWood, 0);
					th.setBlockAt(x-1, y, z, Block.torchWood, 0);
				}
			}
		}
		th.symmetryX = false;
		
		th.ioffset.posZ = gateOffset+sanctumOffset;
		
		// Entrance
		th.fillArea(-2, 1, front, 2, 3, -(9+sanctumOffset), Block.sandStone, 2);
		th.fillArea(-2, 4, -(9+sanctumOffset), 2, 4, -(8+sanctumOffset), Block.sandStone, 2);
		th.fillArea(-1, 4, front, 1, 4,-(10+sanctumOffset), Block.sandStone, 2);
		th.fillArea(-1, 1, front, 1, 3, -(8+sanctumOffset), null, 0);
		
		th.setBlockAt(-2, 4, front, Block.sandStone, 2);
		th.setBlockAt( 2, 4, front, Block.sandStone, 2);

		th.setBlockAt( 0, 5, front, Block.sandStone, 1);
		th.setBlockAt(-1, 5, front, Block.stainedClay, 4);
		th.setBlockAt( 1, 5, front, Block.stainedClay, 4);
		th.setBlockAt(-2, 5, front, Block.sandStone, 2);
		th.setBlockAt( 2, 5, front, Block.sandStone, 2);

		th.setBlockAt( 0, 6, front, Block.sandStone, 2);
		th.setBlockAt(-1, 6, front, Block.sandStone, 2);
		th.setBlockAt( 1, 6, front, Block.sandStone, 2);
		
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
