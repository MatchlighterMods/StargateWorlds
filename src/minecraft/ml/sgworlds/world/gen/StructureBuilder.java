package ml.sgworlds.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.ItemDoor;
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
	
	public final World world;
	public final int rotation;
	public final ChunkPosition center;
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
	
	public ChunkPosition getAbsCoords(int rx, int ry, int rz) {
		int bx = flipXZ ? rz : rx;
		int bz = flipXZ ? rx : rz;
		bx += ioffset.posX; ry += ioffset.posY; bz += ioffset.posZ;
		
		return new ChunkPosition(getAbsX(bx, bz), center.y + ry, getAbsZ(bx, bz));
	}
	
	public int getBlockIdAt(int rx, int ry, int rz) {
		return world.getBlockId(getAbsX(rx, rz), center.y + ry, getAbsZ(rx, rz));
	}
	
	private void setBlockAtAbs(int ax, int ay, int az, Block block, int blockMeta) {
		if (block instanceof BlockDoor) {
			ItemDoor.placeDoorBlock(world, ax, ay, az, blockMeta, block);
		} else {
			world.setBlock(ax, ay, az, block != null ? block.blockID : 0, blockMeta, 2);
		}
	}
	
	public void setBlockAt(int rx, int ry, int rz, Block block, int blockMeta) {
		
		int bx = flipXZ ? rz : rx;
		int bz = flipXZ ? rx : rz;
		bx += ioffset.posX; ry += ioffset.posY; bz += ioffset.posZ;
		
		setBlockAtAbs(getAbsX(bx, bz), center.y + ry, getAbsZ(bx, bz), block, blockMeta);
		
		if (xSymmetry && zSymmetry) {
			setBlockAtAbs(getAbsX(-bx, -bz), center.y + ry, getAbsZ(-bx, -bz), block, blockMeta);
		}
		if (xSymmetry) {
			setBlockAtAbs(getAbsX(-bx, bz), center.y + ry, getAbsZ(-bx, bz), block, blockMeta);
		}
		if (zSymmetry) {
			setBlockAtAbs(getAbsX(bx, -bz), center.y + ry, getAbsZ(bx, -bz), block, blockMeta);
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
	
	public int getMetadataWithOffset(Block block, int cRotation) {
		int rot4 =(this.rotation + cRotation) % 4;
		
		if (block instanceof BlockRailBase) {
			return (this.rotation + cRotation) % 2;
			
		} else if (block instanceof BlockDoor) {
			return (this.rotation + cRotation + 1) % 4;
			
		} else if (block instanceof BlockStairs) {
			int rot = rot4;
			
			if (rot4 == 0) rot4 = 2;
			else if (rot4 == 2) rot4 = 3;
			else if (rot4 == 3) rot4 = 0;
			
			return rot | (cRotation & 8);
			
		} else if (block == Block.ladder) {
			if (rot4 == 0) return 2;
			if (rot4 == 1) return 5;
			else if (rot4 == 2) return 3;
			else if (rot4 == 3) return 4;
			
		} else if (block instanceof BlockButton || block instanceof BlockLever) {
			if (rot4 == 0) return 4;
			if (rot4 == 1) return 1;
			else if (rot4 == 2) return 3;
			else if (rot4 == 3) return 2;
			
		} else if (block instanceof BlockPistonBase || block instanceof BlockLever || block == Block.dispenser) {
			// TODO
		} else {
		}
		
		return cRotation;
	}
}
