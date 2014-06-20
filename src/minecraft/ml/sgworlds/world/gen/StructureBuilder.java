package ml.sgworlds.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class StructureBuilder {

	/*
	 * Gate Facing:
	 * 0: North -Z
	 * 1: East +X
	 * 2: South +Z
	 * 3: West -X
	 * 
	 * Coords:
	 * +X : Right
	 * -X : Left
	 * +Z : Forward
	 * -Z : Backward
	 */
	
	public World world;
	public int rotation;
	public ChunkPosition center;
	public ChunkCoordinates ioffset = new ChunkCoordinates();
	
	public boolean flipXZ = false;
	public boolean xSymmetry = false;
	public boolean zSymmetry = false;
	
	/**
	 * 
	 * @param world
	 * @param pos The point to rotate the structure around.
	 * @param rot The number of clockwise, 90-degree increments to rotate the structure.
	 */
	public StructureBuilder(World world, ChunkPosition pos, int rot) {
		this.world = world;
		this.center = pos;
		this.rotation = rot;
	}
	
	public int getAbsZ(int x, int z) {
		switch (rotation) {
		case 0:
			return center.z-z;
		case 1:
			return center.z+x;
		case 2:
			return center.z+z;
		case 3:
			return center.z-x;
		}
		return center.z;
	}
	
	public int getAbsX(int x, int z) {
		switch (rotation) {
		case 0:
			return center.x+x;
		case 1:
			return center.x+z;
		case 2:
			return center.x-x;
		case 3:
			return center.x-z;
		}
		return center.x;
	}
	
	public int getBlockIdAt(int rx, int ry, int rz) {
		return world.getBlockId(getAbsX(rx, rz), center.y + ry, getAbsZ(rx, rz));
	}
	
	public void setBlockAt(int rx, int ry, int rz, Block block, int blockMeta) {
		int bx = flipXZ ? rz : rx;
		int bz = flipXZ ? rx : rz;
		bx += ioffset.posX; ry += ioffset.posY; bz += ioffset.posZ;
		world.setBlock(getAbsX(bx, bz), center.y + ry, getAbsZ(bx, bz), block != null ? block.blockID : 0, blockMeta, 2);
		
		if (xSymmetry && zSymmetry) {
			world.setBlock(getAbsX(-bx, -bz), center.y + ry, getAbsZ(-bx, -bz), block != null ? block.blockID : 0, blockMeta, 2);
		}
		if (xSymmetry) {
			world.setBlock(getAbsX(-bx, bz), center.y + ry, getAbsZ(-bx, bz), block != null ? block.blockID : 0, blockMeta, 2);
		}
		if (zSymmetry) {
			world.setBlock(getAbsX(bx, -bz), center.y + ry, getAbsZ(bx, -bz), block != null ? block.blockID : 0, blockMeta, 2);
		}
	}
	
	public void fillArea(int startX, int startY, int startZ, int endX, int endY, int endZ, Block block, int blockMeta) {
		for (int x=startX; x<=endX; x++) {
			for (int z=startZ; z<=endZ; z++) {
				for (int y=startY; y<=endY; y++) {
					setBlockAt(x, y, z, block, blockMeta);
				}
			}
		}
	}
	
	public void borderArea(int startX, int startY, int startZ, int endX, int endY, int endZ, Block block, int blockMeta) {
		for (int x=startX; x<=endX; x++) {
			boolean xc = x==startX || x==endX;
			
			for (int z=startZ; z<=endZ; z++) {
				boolean zc = z==startZ || z==endZ;
				
				for (int y=startY; y<=endY; y++) {
					boolean yc = y==startY || y==endY;
					
					if ((yc && xc) || (yc && zc) || (xc && zc)) setBlockAt(x, y, z, block, blockMeta);
				}
			}
		}
	}
	
	public void wallArea(int startX, int startY, int startZ, int endX, int endY, int endZ, Block block, int blockMeta) {
		for (int x=startX; x<=endX; x++) {
			boolean xc = x==startX || x==endX;
			
			for (int z=startZ; z<=endZ; z++) {
				boolean zc = z==startZ || z==endZ;
				
				for (int y=startY; y<=endY; y++) {
					boolean yc = y==startY || y==endY;
					
					if (xc || yc || zc) setBlockAt(x, y, z, block, blockMeta);
				}
			}
		}
	}
	
	public void wallArea(int startX, int startY, int startZ, int endX, int endY, int endZ, boolean wx, boolean wy, boolean wz, Block block, int blockMeta) {
		for (int x=startX; x<=endX; x++) {
			boolean xc = (x==startX || x==endX) && wx;
			
			for (int z=startZ; z<=endZ; z++) {
				boolean zc = (z==startZ || z==endZ) && wz;
				
				for (int y=startY; y<=endY; y++) {
					boolean yc = (y==startY || y==endY) && wy;
					
					if (xc || yc || zc) setBlockAt(x, y, z, block, blockMeta);
				}
			}
		}
	}
}
