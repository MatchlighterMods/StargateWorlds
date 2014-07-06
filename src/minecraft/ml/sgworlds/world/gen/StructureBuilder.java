package ml.sgworlds.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.item.ItemDoor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.ForgeDirection;

/**
 * Helper class for making the creation of rotatable structures easier.<br/>
 * Imposes a custom coordinate system:
 * 
 * <pre>
 *              Z-
 *              ^
 *              |
 *              N
 *  X- <------W + E------> X+
 *              S
 *              |
 *              v
 *              Z+
 * </pre>
 * 
 * N,E,S,W are default minecraft directions, in global space. Z-, X+, Z+, X- are in local space and are rotated depending on the passed rotation.
 * Rotation works in 90-degree increments, rotating the axes in the above illustration clockwise. e.g. Z- becomes East, X+ becomes South, etc.<br/>
 * 
 * @author Matchlighter
 */
public class StructureBuilder {

	public final World world;
	public final int rotation;
	public final ChunkPosition center;
	public ChunkCoordinates ioffset = new ChunkCoordinates();
	public ChunkCoordinates absMinimum, absMaximum;
	
	public boolean flipXZ = false;
	
	public boolean symmetryX = false;
	public boolean symmetryZ = false;
	
	public boolean invertX = false;
	public boolean invertY = false;
	public boolean invertZ = false;
	
	/**
	 * See {@link StructureBuilder} for details.
	 * @param world
	 * @param pos The point to rotate the structure around.
	 * @param rot The number of clockwise, 90-degree increments to rotate the structure.
	 */
	public StructureBuilder(World world, ChunkPosition pos, int rot) {
		this.world = world;
		this.center = pos;
		this.rotation = rot;
	}
	
	public StructureBuilder(World world, ChunkCoordinates pos, int rot) {
		this.world = world;
		this.center = new ChunkPosition(pos.posX, pos.posY, pos.posZ);
		this.rotation = rot;
	}
	
	/**
	 * Converts the local x passed to a global x-value.
	 */
	public int getAbsX(int x, int z) {
		if (invertX) x = -x; if (invertZ) z = -z;
		x += ioffset.posX; z += ioffset.posZ;
		
		switch (rotation) {
		case 0:
			return center.x+x;
		case 1:
			return center.x-z;
		case 2:
			return center.x-x;
		case 3:
			return center.x+z;
		}
		return center.x;
	}
	
	public int getAbsY(int oy) {
		if (invertY) oy = -oy;
		return center.y + oy + ioffset.posY;
	}
	
	/**
	 * Converts the local z passed to a global z-value.
	 */
	public int getAbsZ(int x, int z) {
		if (invertX) x = -x; if (invertZ) z = -z;
		x += ioffset.posX; z += ioffset.posZ;
		
		switch (rotation) {
		case 0:
			return center.z+z;
		case 1:
			return center.z+x;
		case 2:
			return center.z-z;
		case 3:
			return center.z-x;
		}
		return center.z;
	}
	
	public TileEntity getTileEntityAt(int rx, int ry, int rz) {
		ChunkCoordinates abs = getAbsCoords(rx, ry, rz);
		return world.getBlockTileEntity(abs.posX, abs.posY, abs.posZ);
	}
	
	/**
	 * Converts the relative x,y,z into a global point.
	 */
	public ChunkCoordinates getAbsCoords(int rx, int ry, int rz) {
		int bx = flipXZ ? rz : rx;
		int bz = flipXZ ? rx : rz;
		
		return new ChunkCoordinates(getAbsX(bx, bz), getAbsY(ry), getAbsZ(bx, bz));
	}
	
	public boolean absCoordsInRange(int ax, int ay, int az) {
		if (absMinimum == null || absMaximum == null) return true;
		return  (ax >= absMinimum.posX && ax <= absMaximum.posX) &&
				(ay >= absMinimum.posY && ay <= absMaximum.posY) &
				(az >= absMinimum.posZ && az <= absMaximum.posZ); 
	}
	
	public boolean relCoordsInRange(int rx, int ry, int rz) {
		if (absMinimum == null || absMaximum == null) return true;
		ChunkCoordinates absPt = getAbsCoords(rx, ry, rz);
		return (absPt.posX >= absMinimum.posX && absPt.posX <= absMaximum.posX) &&
				(absPt.posY >= absMinimum.posY && absPt.posY <= absMaximum.posY) &
				(absPt.posZ >= absMinimum.posZ && absPt.posZ <= absMaximum.posZ); 
	}
	
	public void setMinMax(ChunkCoordinates min, ChunkCoordinates max) {
		this.absMinimum = min;
		this.absMaximum = max;
	}
	
	public void setMinMax(StructureBoundingBox bb) {
		if (bb != null) {
			this.absMinimum = new ChunkCoordinates(bb.minX, bb.minY, bb.minZ);
			this.absMaximum = new ChunkCoordinates(bb.maxX, bb.maxY, bb.maxZ);
		} else {
			this.absMinimum = null;
			this.absMaximum = null;
		}
	}
	
	public int getBlockIdAt(int rx, int ry, int rz) {
		ChunkCoordinates p = getAbsCoords(rx, ry, rz);
		return world.getBlockId(p.posX, p.posY, p.posZ);
	}
	
	private void setBlockAtAbs(int ax, int ay, int az, Block block, int blockMeta) {
		if (!absCoordsInRange(ax, ay, az)) return;
		
		if (block instanceof BlockDoor) {
			ItemDoor.placeDoorBlock(world, ax, ay, az, blockMeta, block);
		} else {
			world.setBlock(ax, ay, az, block != null ? block.blockID : 0, blockMeta, 3);
		}
	}
	
	/**
	 * Set the block at the local coordinates (rx, ry, rz) to the specified block and meta.
	 * Takes symmetry booleans into account, so up to 4 blocks may be placed.
	 */
	public void setBlockAt(int rx, int ry, int rz, Block block, int blockMeta) {
		
		int bx = flipXZ ? rz : rx;
		int bz = flipXZ ? rx : rz;
		
		setBlockAtAbs(getAbsX(bx, bz), getAbsY(ry), getAbsZ(bx, bz), block, blockMeta);
		
		if (symmetryX && symmetryZ && bx != 0 && bz != 0) {
			setBlockAtAbs(getAbsX(-bx, -bz), getAbsY(ry), getAbsZ(-bx, -bz), block, blockMeta);
		}
		if (symmetryX && bx != 0) {
			setBlockAtAbs(getAbsX(-bx, bz), getAbsY(ry), getAbsZ(-bx, bz), block, blockMeta);
		}
		if (symmetryZ && bz != 0) {
			setBlockAtAbs(getAbsX(bx, -bz), getAbsY(ry), getAbsZ(bx, -bz), block, blockMeta);
		}
	}
	
	/**
	 * Fills the specified area with blocks. start_ and end_ are both inclusive.
	 */
	public void fillArea(int startX, int startY, int startZ, int endX, int endY, int endZ, Block block, int blockMeta) {
		for (int x=startX; x<=endX; x++) {
			for (int z=startZ; z<=endZ; z++) {
				for (int y=startY; y<=endY; y++) {
					setBlockAt(x, y, z, block, blockMeta);
				}
			}
		}
	}
	
	/**
	 * Fills the edges of the specified area with blocks. start_ and end_ are both inclusive.
	 */
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
	
	/**
	 * Fills the faces of the specified area with blocks. start_ and end_ are both inclusive.
	 */
	public void wallArea(int startX, int startY, int startZ, int endX, int endY, int endZ, Block block, int blockMeta) {
		wallArea(startX, startY, startZ, endX, endY, endZ, true, true, true, block, blockMeta);
	}
	
	/**
	 * Fills the faces of the specified area with blocks, if the w_ is true for the axis. start_ and end_ are both inclusive.
	 */
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
	
	public void fillDown(int startX, int startY, int startZ, int endX, int endZ, Block block, int blockMeta) {
		for (int x=startX; x<=endX; x++) {
			for (int z=startZ; z<=endZ; z++) {
				for (int y=startY; getAbsY(y)>0; y--) {
					if (getBlockIdAt(x, y, z) != 0) {
						break;
					}
					setBlockAt(x, y, z, block, blockMeta);
				}
			}
		}
	}
	
	/**
	 * Attempts to get the rotated metadata of a rotatable block.
	 */
	public int getRotatedMeta(Block block, int cRotation) {
		int rot4 =(this.rotation + cRotation) % 4;
		
		if (block instanceof BlockRailBase) {
			return (this.rotation + cRotation) % 2;
			
		} else if (block instanceof BlockDoor) {
			return (this.rotation + cRotation + 1) % 4;
			
		} else if (block instanceof BlockStairs) {
			int rot = rot4;
			
			if (rot4 == 0) rot = 2;
			else if (rot4 == 2) rot = 3;
			else if (rot4 == 3) rot = 0;
			
			return rot | (cRotation & 8);
			
		} else if (block == Block.ladder) {
			if (rot4 == 0) return 2;
			if (rot4 == 1) return 5;
			else if (rot4 == 2) return 3;
			else if (rot4 == 3) return 4;
			
		} else if (block instanceof BlockButton || block instanceof BlockLever || block instanceof BlockTorch) {
			if (rot4 == 0) return 4;
			if (rot4 == 1) return 1;
			else if (rot4 == 2) return 3;
			else if (rot4 == 3) return 2;
			
		} else if (block instanceof BlockPistonBase || block instanceof BlockLever || block == Block.dispenser) {
			// TODO
		} else if (block instanceof BlockRedstoneRepeater) {
			return rot4;
		}
		
		return rot4;
	}
	
	private static final int[][] rotMatrix = {
		{0,1, 2,3,4,5},
		{0,1, 5,4,2,3},
		{0,1, 3,2,5,4},
		{0,1, 4,5,3,2},
	};  
	
	public ForgeDirection rotateForgeDir(ForgeDirection fdir) {
		return ForgeDirection.getOrientation(rotMatrix[rotation][fdir.ordinal()]);
	}
}
