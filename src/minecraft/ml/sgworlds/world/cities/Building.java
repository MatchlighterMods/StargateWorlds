package ml.sgworlds.world.cities;

/*
 *  Source code for the The Great Wall Mod and Walled City Generator Mods for the game Minecraft
 *  Copyright (C) 2011 by formivore

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *      Building is a general class for buildings. Classes can inherit from Building to build from a local frame of reference.
 * 
 *  Local frame of reference variables:
 *     i,j,k are coordinate inputs for global frame of reference functions.
 *     x,z,y are coordinate inputs for local frame of reference functions.
 *     bHand =-1,1 determines whether X-axis points left or right respectively when facing along Y-axis.
 *
 *                           (dir=0)
 *                                (-k)
 *                 n
 *                 n
 *  (dir=3) (-i)www*eee(+i)  (dir=1)
 *                 s
 *                 s
 *                (+k)
 *               (dir=2)
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ml.sgworlds.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockVine;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

public class Building {
	public final static int HIT_WATER = -666; // , HIT_SWAMP=-667;
	public final static int EASY_CHEST = 0, MEDIUM_CHEST = 1, HARD_CHEST = 2, TOWER_CHEST = 3;
	public final static int DIR_NORTH = 0, DIR_EAST = 1, DIR_SOUTH = 2, DIR_WEST = 3;
	public final static int ROT_R = 1, R_HAND = 1, L_HAND = -1;
	public final static int SEA_LEVEL = 63, WORLD_MAX_Y = 255;
	// **** WORKING VARIABLES ****
	protected World world;
	protected Random random;
	protected TemplateRule bRule; // main structural blocktype
	public int bWidth, bHeight, bLength;
	public int bID; // Building ID number
	private LinkedList<int[]> delayedBuildQueue;
	protected WorldGeneratorThread wgt;
	protected boolean centerAligned; // if true, alignPt x is the central axis of the building if false, alignPt is the origin
	protected int xOrigin, yOrigin, zOrigin; // origin coordinates (x=0,z=0,y=0). The child class may want to move the origin as it progress to use as a "cursor" position.
	private int xI, zI, xK, zK; //
	protected int bHand; // hand of secondary axis. Takes values of 1 for right-handed, -1 for left-handed.
	protected int bDir; // Direction code of primary axis. Takes values of DIR_NORTH=0,DIR_EAST=1,DIR_SOUTH=2,DIR_WEST=3.

	public final static int IGNORE_WATER = -1;
	// Special Blocks
	public final static int PAINTING_BLOCK_OFFSET = -3;

	public final static String[] SPAWNERS = new String[]{
		"Zombie", "Skeleton", "Spider", "Creeper", "PigZombie", "Ghast", "Enderman", "CaveSpider", "Blaze", "Slime",
		"LavaSlime", "Villager", "SnowMan", "MushroomCow", "Sheep", "Cow", "Chicken", "Squid", "Wolf", "Giant",
		"Silverfish", "EnderDragon", "Ozelot", "VillagerGolem", "WitherBoss", "Bat", "Witch"
	};

	// maps block metadata to a dir
	public final static int[] BED_META_TO_DIR = new int[] { DIR_SOUTH, DIR_WEST, DIR_NORTH, DIR_EAST }, STAIRS_META_TO_DIR = new int[] { DIR_EAST, DIR_WEST, DIR_SOUTH, DIR_NORTH },
			LADDER_META_TO_DIR = new int[] { DIR_NORTH, DIR_SOUTH, DIR_WEST, DIR_EAST }, TRAPDOOR_META_TO_DIR = new int[] { DIR_SOUTH, DIR_NORTH, DIR_EAST, DIR_WEST }, VINES_META_TO_DIR = new int[] {
		0, DIR_SOUTH, DIR_WEST, 0, DIR_NORTH, 0, 0, 0, DIR_EAST }, DOOR_META_TO_DIR = new int[] { DIR_WEST, DIR_NORTH, DIR_EAST, DIR_SOUTH };

	// inverse map should be {North_inv,East_inv,dummy,West_inv,South_inv}
	// inverse map should be {North_inv,East_inv,South_inv, West_inv}
	public final static int[] BED_DIR_TO_META = new int[] { 2, 3, 0, 1 }, BUTTON_DIR_TO_META = new int[] { 4, 1, 3, 2 }, STAIRS_DIR_TO_META = new int[] { 3, 0, 2, 1 }, LADDER_DIR_TO_META = new int[] {
		2, 5, 3, 4 }, TRAPDOOR_DIR_TO_META = new int[] { 1, 2, 0, 3 }, VINES_DIR_TO_META = new int[] { 4, 8, 1, 2 }, DOOR_DIR_TO_META = new int[] { 3, 0, 1, 2 },
				PAINTING_DIR_TO_FACEDIR = new int[] { 0, 3, 2, 1 };
	public final static int[] DIR_TO_I = new int[] { 0, 1, 0, -1 }, DIR_TO_K = new int[] { -1, 0, 1, 0 };

	// for use in local orientation
	public final static int[] DIR_TO_X = new int[] { 0, 1, 0, -1 }, DIR_TO_Y = new int[] { 1, 0, -1, 0 };

	// some prebuilt directional blocks
	public final static BlockAndMeta WEST_FACE_TORCH_BLOCK = new BlockAndMeta(Block.torchWood, BUTTON_DIR_TO_META[DIR_WEST]), EAST_FACE_TORCH_BLOCK = new BlockAndMeta(Block.torchWood, BUTTON_DIR_TO_META[DIR_EAST]),
			NORTH_FACE_TORCH_BLOCK = new BlockAndMeta(Block.torchWood, BUTTON_DIR_TO_META[DIR_NORTH]), SOUTH_FACE_TORCH_BLOCK = new BlockAndMeta(Block.torchWood, BUTTON_DIR_TO_META[DIR_SOUTH]),
			EAST_FACE_LADDER_BLOCK = new BlockAndMeta(Block.ladder, LADDER_DIR_TO_META[DIR_EAST]), HOLE_BLOCK_LIGHTING = new BlockAndMeta(Block.air, 0), HOLE_BLOCK_NO_LIGHTING = new BlockAndMeta(Block.air, 1),
			PRESERVE_BLOCK = new BlockExtended(Block.air, 0, "PRESERVE"),
			TOWER_CHEST_BLOCK = new BlockExtended(Block.chest, 0, TOWER_CHEST), HARD_CHEST_BLOCK = new BlockExtended(Block.chest, 0, HARD_CHEST),
			GHAST_SPAWNER = new BlockExtended(Block.mobSpawner, 0, "Ghast");

	protected final static Block[] STEP_TO_STAIRS = { Block.stairsStoneBrick, Block.stairsSandStone, Block.stairsWoodOak, Block.stairsCobblestone, Block.stairsBrick, Block.stairsStoneBrick, Block.stairsNetherBrick,
		Block.stairsNetherQuartz };


	public final static int MAX_SPHERE_DIAM = 40;
	public final static int[][] SPHERE_SHAPE = new int[MAX_SPHERE_DIAM + 1][];
	public final static int[][][] CIRCLE_SHAPE = new int[MAX_SPHERE_DIAM + 1][][], CIRCLE_CRENEL = new int[MAX_SPHERE_DIAM + 1][][];
	static {
		for (int diam = 1; diam <= MAX_SPHERE_DIAM; diam++) {
			circleShape(diam);
		}
		// change diam 6 shape to look better
		CIRCLE_SHAPE[6] = new int[][] { { -1, -1, 1, 1, -1, -1 }, { -1, 1, 0, 0, 1, -1 }, { 1, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 1 }, { -1, 1, 0, 0, 1, -1 }, { -1, -1, 1, 1, -1, -1 } };
		CIRCLE_CRENEL[6] = new int[][] { { -1, -1, 1, 0, -1, -1 }, { -1, 0, 0, 0, 1, -1 }, { 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1 }, { -1, 1, 0, 0, 0, -1 }, { -1, -1, 0, 1, -1, -1 } };
	}
	private final static int LIGHTING_INVERSE_DENSITY = 10;
	private final static boolean[] randLightingHash = new boolean[512];
	static {
		Random rand = new Random();
		for (int m = 0; m < randLightingHash.length; m++)
			randLightingHash[m] = rand.nextInt(LIGHTING_INVERSE_DENSITY) == 0;
	}
	// TODO:Extend chest_type_labels as config option
	public static String[] CHEST_TYPE_LABELS = new String[] { "CHEST_EASY", "CHEST_MEDIUM", "CHEST_HARD", "CHEST_TOWER" };
	public static List<String> CHEST_LABELS = new ArrayList<String>();
	public static int[] DEFAULT_CHEST_TRIES = new int[] { 4, 6, 6, 6 };


	public Building(int ID_, WorldGeneratorThread wgt_, TemplateRule buildingRule_, int dir_, int axXHand_, boolean centerAligned_, int[] dim, ChunkCoordinates alignPt) {
		bID = ID_;
		wgt = wgt_;
		world = wgt.world;
		bRule = buildingRule_;
		if (bRule == null)
			bRule = TemplateRule.STONE_RULE;
		bWidth = dim[0];
		bHeight = dim[1];
		bLength = dim[2];
		random = wgt.random;
		bHand = axXHand_;
		centerAligned = centerAligned_;
		setPrimaryAx(dir_);
		if (alignPt != null) {
			if (centerAligned)
				setOrigin(alignPt.posX - xI * bWidth / 2, alignPt.posY, alignPt.posZ - xK * bWidth / 2);
			else
				setOrigin(alignPt.posX, alignPt.posY, alignPt.posZ);
		}
		delayedBuildQueue = new LinkedList<PlacedBlock>();
	}

	// ******************** LOCAL COORDINATE FUNCTIONS - ACCESSORS
	// *************************************************************************************************************//
	// Use these instead of World.java functions when to build from a local
	// reference frame
	// when i0,j0,k0 are set to working values.

	public final ChunkCoordinates getAbsXYZPt(int x, int y, int z) {
		return new ChunkCoordinates(getAbsX(x, z), getAbsY(y), getAbsZ(x, z));
	}
	
	public final ChunkCoordinates getAbsXYZPt(ChunkCoordinates p) {
		return new ChunkCoordinates(getAbsX(p.posX, p.posZ), getAbsY(p.posY), getAbsZ(p.posX, p.posZ));
	}
	
	public final int getAbsX(int x, int z) {
		return xOrigin + zI * z + xI * x;
	}

	public final int getAbsY(int y) {
		return yOrigin + y;
	}

	public final int getAbsZ(int x, int z) {
		return zOrigin + zK * z + xK * x;
	}

	public final ChunkCoordinates getSurfaceIJKPt(int x, int z, int startY, boolean wallIsSurface, int waterSurfaceBuffer) {
		ChunkCoordinates pt = getAbsXYZPt(x, 0, z);
		pt.posY = findSurfaceJ(world, pt.posX, pt.posZ, startY, wallIsSurface, waterSurfaceBuffer);
		return pt;
	}

	public final String localCoordString(int x, int z, int y) {
		ChunkCoordinates pt = getAbsXYZPt(x, z, y);
		return "(" + pt.posX + "," + pt.posY + "," + pt.posZ + ")";
	}

	// outputs dir rotated to this Building's orientation and handedness
	// dir input should be the direction desired if bDir==DIR_NORTH and
	// bHand=R_HAND
	public int orientDirToBDir(int dir) {
		return bHand < 0 && dir % 2 == 1 ? (bDir + dir + 2) & 0x3 : (bDir + dir) & 0x3;
	}

	// &&&&&&&&&&&&&&&&& SPECIAL BLOCK FUNCTION - setPainting
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
	public void setPainting(ChunkCoordinates pt, int metadata) {
		// painting uses same orientation meta as ladders.
		// Have to adjust ijk since unlike ladders the entity exists at the
		// block it is hung on.
		int dir = orientDirToBDir(LADDER_META_TO_DIR[metadata]);
		pt.posX -= DIR_TO_I[dir];
		pt.posZ -= DIR_TO_K[dir];
		if (dir == DIR_NORTH)
			pt.posZ++;
		else if (dir == DIR_SOUTH)
			pt.posZ--;
		else if (dir == DIR_WEST)
			pt.posX++;
		else
			pt.posX--;

		EntityPainting entitypainting = new EntityPainting(world, pt.posX, pt.posY, pt.posZ, PAINTING_DIR_TO_FACEDIR[dir]);
		if (!world.isRemote && entitypainting.onValidSurface())
			world.spawnEntityInWorld(entitypainting);
	}

	// ******************** ORIENTATION FUNCTIONS
	// *************************************************************************************************************//
	public void setPrimaryAx(int dir_) {
		bDir = dir_;
		// changes of basis
		switch (bDir) {
		case DIR_NORTH:
			xI = bHand;
			zI = 0;
			xK = 0;
			zK = -1;
			break;
		case DIR_EAST:
			xI = 0;
			zI = 1;
			xK = bHand;
			zK = 0;
			break;
		case DIR_SOUTH:
			xI = -bHand;
			zI = 0;
			xK = 0;
			zK = 1;
			break;
		case DIR_WEST:
			xI = 0;
			zI = -1;
			xK = -bHand;
			zK = 0;
			break;
		}
	}

	// &&&&&&&&&&&&&&&&& SPECIAL BLOCK FUNCTION - setSignOrPost
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
	public void setSignOrPost(int x2, int z2, int y2, boolean post, int sDir, String[] lines) {
		ChunkCoordinates pt = getAbsXYZPt(x2, z2, y2);
		world.setBlock(pt.posX, pt.posY, pt.posZ, post ? Block.signPost.blockID : Block.signWall.blockID, sDir, 2);
		TileEntitySign tileentitysign = (TileEntitySign) world.getBlockTileEntity(pt.posX, pt.posY, pt.posZ);
		if (tileentitysign == null)
			return;
		for (int m = 0; m < Math.min(lines.length, 4); m++) {
			tileentitysign.signText[m] = lines[m];
		}
	}

	// call with z=start of builDown, will buildDown a maximum of maxDepth
	// blocks + foundationDepth.
	// if buildDown column is completely air, instead buildDown reserveDepth
	// blocks.
	protected final void buildDown(int x, int z, int y, TemplateRule buildRule, int maxDepth, int foundationDepth, int reserveDepth) {
		int stopZ;
		for (stopZ = z; stopZ > z - maxDepth; stopZ--) {
			if (!isWallable(x, stopZ, y))
				break; // find ground height
		}
		if (stopZ == z - maxDepth && isWallable(x, z - maxDepth, y)) // if we never hit ground
			stopZ = z - reserveDepth;
		else
			stopZ -= foundationDepth;
		for (int z1 = z; z1 > stopZ; z1--) {
			BlockAndMeta idAndMeta = buildRule.getBlockId(world.rand);
			setBlockWithLightingLocal(x, z1, y, idAndMeta.get(), idAndMeta.getMeta(), false);
		}
	}

	protected final Block getBlockIdLocal(int x, int y, int z) {
		return Block.blocksList[world.getBlockId(xOrigin + zI * z + xI * x, yOrigin + y, zOrigin + zK * z + xK * x)];
	}

	protected final int getBlockMetadataLocal(int x, int z, int y) {
		return world.getBlockMetadata(xOrigin + zI * y + xI * x, yOrigin + z, zOrigin + zK * y + xK * x);
	}

	// replaces orientationString
	protected final String IDString() {
		String str = "ID=" + bID + " axes(Y,X)=";
		switch (bDir) {
		case DIR_SOUTH:
			return str + "(S," + (bHand > 0 ? "W)" : "E)");
		case DIR_NORTH:
			return str + "(N," + (bHand > 0 ? "E)" : "W)");
		case DIR_WEST:
			return str + "(W," + (bHand > 0 ? "N)" : "S)");
		case DIR_EAST:
			return str + "(E," + (bHand > 0 ? "S)" : "N)");
		}
		return "Error - bad dir value for ID=" + bID;
	}

	protected final boolean isArtificialWallBlock(int x, int z, int y) {
		Block block = getBlockIdLocal(x, z, y);
		return BlockProperties.get(block).isArtificial && !(block == Block.sandStone && (getBlockIdLocal(x, z + 1, y) == Block.sand || getBlockIdLocal(x, z + 2, y) == Block.sand));
	}

	protected final boolean isDoorway(int x, int z, int y) {
		return isFloor(x, z, y) && (isWallBlock(x + 1, z, y) && isWallBlock(x - 1, z, y) || isWallBlock(x, z, y + 1) && isWallBlock(x - 1, z, y - 1));
	}
	
	protected final boolean hasNoDoorway(int x, int z, int y) {
		return !(isDoorway(x - 1, z, y) || isDoorway(x + 1, z, y) || isDoorway(x, z, y - 1) || isDoorway(x - 1, z, y + 1));
	}

	// true if block is air, block below is wall block
	protected final boolean isFloor(int x, int z, int y) {
		Block blkId1 = getBlockIdLocal(x, z, y), blkId2 = getBlockIdLocal(x, z - 1, y);
		return blkId1 == null && BlockProperties.get(blkId2).isArtificial && blkId2 != Block.ladder;
	}

	protected final boolean isNextToDoorway(int x, int z, int y) {
		return isDoorway(x - 1, z, y) || isDoorway(x + 1, z, y) || isDoorway(x, z, y - 1) || isDoorway(x - 1, z, y + 1);
	}

	protected boolean isObstructedFrame(int zstart, int ybuffer) {
		for (int z1 = zstart; z1 < bHeight; z1++) {
			// for(int x1=0; x1<length; x1++) for(int y1=ybuffer;
			// y1<width-1;y1++)
			// if(isWallBlock(x1,z1,y1))
			// return true;
			for (int x1 = 0; x1 < bWidth; x1++) {
				if (isArtificialWallBlock(x1, z1, bLength - 1)) {
					return true;
				}
			}
			for (int y1 = ybuffer; y1 < bLength - 1; y1++) {
				if (isArtificialWallBlock(0, z1, y1)) {
					return true;
				}
				if (isArtificialWallBlock(bWidth - 1, z1, y1)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isObstructedSolid(int pt1[], int pt2[]) {
		for (int x1 = pt1[0]; x1 <= pt2[0]; x1++) {
			for (int z1 = pt1[1]; z1 <= pt2[1]; z1++) {
				for (int y1 = pt1[2]; y1 <= pt2[2]; y1++) {
					if (!isWallable(x1, z1, y1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected final boolean isStairBlock(int x, int z, int y) {
		return getBlockIdLocal(x, z, y) instanceof BlockHalfSlab;
	}

	// ******************** LOCAL COORDINATE FUNCTIONS - BLOCK TEST FUNCTIONS
	// *************************************************************************************************************//
	protected final boolean isWallable(int x, int y, int z) {
		return BlockProperties.get(getBlockIdLocal(x, y, z)).isWallable;
	}

	protected final boolean isWallBlock(int x, int y, int z) {
		return BlockProperties.get(getBlockIdLocal(x, y, z)).isArtificial;
	}

	public final void flushDelayed(){
		while(delayedBuildQueue.size()>0){
			PlacedBlock block = delayedBuildQueue.poll();
			setDelayed(block.get(), block.x, block.y, block.z, block.getMeta());
		}
	}

	protected void setDelayed(Block blc, int...block) {
		if (BlockProperties.get(blc).isStair) {
			BlockAndMeta temp = getDelayedStair(blc, block);
			blc = temp.get();
			block[3] = temp.getMeta();
		} else if (blc instanceof BlockVine) {
			if (block[3] == 0 && !isSolidBlock(world.getBlock(block[0], block[1] + 1, block[2])))
				block[3] = 1;
			if (block[3] != 0) {
				int dir = VINES_META_TO_DIR[block[3]];
				while (true) {
					if (isSolidBlock(world.getBlock(block[0] + DIR_TO_I[dir], block[1], block[2] + DIR_TO_K[dir])))
						break;
					dir = (dir + 1) % 4;
					if (dir == VINES_META_TO_DIR[block[3]]) { // we've looped through everything
						if (isSolidBlock(world.getBlock(block[0], block[1] + 1, block[2]))) {
							dir = -1;
							break;
						}
						return; // did not find a surface we can attach to
					}
				}
				block[4] = dir == -1 ? 0 : VINES_DIR_TO_META[dir];
			}
		}
		// It seems Minecraft orients torches automatically, so I shouldn't have to do anything...
		// else if(block[3]==TORCH_ID || block[3]==REDSTONE_TORCH_ON_ID ||
		// block[3]==REDSTONE_TORCH_OFF_ID){
		// block[4]=1;
		// }
		if (blc == Registry.blockAir && block[3]>=PAINTING_BLOCK_OFFSET)//Remember:Paintings are not blocks
			setPainting(block, block[3]-PAINTING_BLOCK_OFFSET);
		else if (blc == Block.torchWood) {
			if (Block.torchWood.canPlaceBlockAt(world, block[0], block[1], block[2]))
				world.setBlock(block[0], block[1], block[2], blc.blockID, block[3], 3);// force lighting update
		} else if (blc == Block.glowStone)
			world.setBlock(block[0], block[1], block[2], blc.blockID, block[3], 3);// force lighting update
		else if(blc!=null) {
			if((randLightingHash[(block[0] & 0x7) | (block[1] & 0x38) | (block[2] & 0x1c0)]))
				world.setBlock(block[0], block[1], block[2], blc.blockID, block[3], 3);
			else
				setBlockAndMetaNoLighting(world, block[0], block[1], block[2], blc, block[3]);
		}
	}

	protected BlockAndMeta getDelayedStair(Block blc, int...block){
		// if stairs are running into ground. replace them with a solid block
		int dirX = block[0] - DIR_TO_I[STAIRS_META_TO_DIR[block[3] % 4]];
		int dirZ = block[2] - DIR_TO_K[STAIRS_META_TO_DIR[block[3] % 4]];
		if(world.getHeightValue(dirX, dirZ)>block[1]) {
			Block adjId = Block.blocksList[world.getBlockId(dirX, block[1], dirZ)];
			Block aboveID = Block.blocksList[world.getBlockId(block[0], block[1] + 1, block[2]);
			if (BlockProperties.get(aboveID).isGround && BlockProperties.get(adjId).isGround) {
				return new BlockAndMeta(blc, block[3]).stairToSolid();
			} else if (!BlockProperties.get(adjId).isWallable || !BlockProperties.get(aboveID).isWallable) {
				return new BlockAndMeta(null, 0); // solid or liquid non-wall block. In this case, just don't build the stair (aka preserve block).
			}
		}
		return new BlockAndMeta(blc, block[3]);
	}

	// The origin of this building was placed to match a centerline.
	// The building previously had bWidth=oldWidth, now it has the current
	// value of bWidth and needs to have origin updated.
	protected final void recenterFromOldWidth(int oldWidth) {
		xOrigin += xI * (oldWidth - bWidth) / 2;
		zOrigin += xK * (oldWidth - bWidth) / 2;
	}

	// ******************** LOCAL COORDINATE FUNCTIONS - SET BLOCK FUNCTIONS
	// *************************************************************************************************************//
	protected final void setBlockLocal(int x, int z, int y, Block blockID) {
		setBlockLocal(x, z, y, blockID, 0);
	}

	protected final void setBlockLocal(int x, int z, int y, Block block, int metadata) {
		ChunkCoordinates pt = getAbsXYZPt(x, z, y);
		if (block == null && world.getBlockId(pt.posX, pt.posY, pt.posZ) == 0)
			return;
		if (block != Block.chest)
			emptyIfChest(pt);
		if (BlockProperties.get(block).isDelayed){
			offer(block, new int[] { pt.posX, pt.posY, pt.posZ, rotateMetadata(block, metadata) });
		}else if (randLightingHash[(x & 0x7) | (y & 0x38) | (z & 0x1c0)]) {
			world.setBlock(pt.posX, pt.posY, pt.posZ, block.blockID, rotateMetadata(block, metadata), 2);
		} else {
			if (metadata == 0)
				setBlockNoLighting(world, pt.posX, pt.posY, pt.posZ, block);
			else
				setBlockAndMetaNoLighting(world, pt.posX, pt.posY, pt.posZ, block, rotateMetadata(block, metadata));
		}
		if (BlockProperties.get(block).isDoor) {
			addDoorToNewListIfAppropriate(pt.posX, pt.posY, pt.posZ);
		}
	}

	protected final void setBlockLocal(int x, int z, int y, BlockAndMeta block) {
		if(block instanceof BlockExtended){
			setSpecialBlockLocal(x, z, y, block.get(), block.getMeta(), ((BlockExtended) block).info);
		}else{
			setBlockLocal(x, z, y, block.get(), block.getMeta());
		}
	}

	protected final void setBlockLocal(int x, int z, int y, TemplateRule rule) {
		setBlockLocal(x, z, y, rule.getBlockId(random));
	}

	private static boolean isSpecialBlock(Block blockID, int metadata){
		return metadata != 0 && (blockID == Block.mobSpawner || blockID == Block.chest || (blockID == Block.air && metadata < -PAINTING_BLOCK_OFFSET));
	}

	protected final void setBlockWithLightingLocal(int x, int z, int y, TemplateRule rule, boolean lighting) {
		setBlockWithLightingLocal(x, z, y, rule.getBlockOrHole(random), lighting);
	}

	protected final void removeBlockWithLighting(int x, int z, int y){
		setBlockWithLightingLocal(x, z, y, TemplateRule.AIR_RULE, true);
	}

	protected final void setBlockWithLightingLocal(int x, int z, int y, BlockAndMeta block, boolean lighting) {
		if(block instanceof BlockExtended) {
			setSpecialBlockLocal(x, z, y, block.get(), block.getMeta(), ((BlockExtended) block).info);
		} else {
			setBlockWithLightingLocal(x, z, y, block.get(), block.getMeta(), lighting);
		}
	}

	// allows control of lighting. Also will build even if replacing air with air.
	protected final void setBlockWithLightingLocal(int x, int z, int y, Block blockID, int metadata, boolean lighting) {
		ChunkCoordinates pt = getAbsXYZPt(x, z, y);
		if (blockID == null && world.isAirBlock(pt.posX, pt.posY, pt.posZ))
			return;
		if (!(blockID instanceof BlockChest))
			emptyIfChest(pt);
		if (BlockProperties.get(blockID).isDelayed)
			delayedBuildQueue.offer(new PlacedBlock(blockID, new int[]{pt.posX, pt.posY, pt.posZ, rotateMetadata(blockID, metadata)}));
		else if (lighting)
			world.setBlock(pt.posX, pt.posY, pt.posZ, blockID.blockID, rotateMetadata(blockID, metadata), 3);
		else
			setBlockAndMetaNoLighting(world, pt.posX, pt.posY, pt.posZ, blockID, rotateMetadata(blockID, metadata));
		if (BlockProperties.get(blockID).isDoor) {
			addDoorToNewListIfAppropriate(pt.posX, pt.posY, pt.posZ);
		}
	}

	protected final void setOrigin(int x, int y, int z) {
		xOrigin = x;
		yOrigin = y;
		zOrigin = z;
	}

	protected final void setOriginLocal(int i1, int j1, int k1, int x, int z, int y) {
		xOrigin = i1 + zI * y + xI * x;
		yOrigin = j1 + z;
		zOrigin = k1 + zK * y + xK * x;
	}

	// ******************** LOCAL COORDINATE FUNCTIONS - SPECIAL BLOCK FUNCTIONS
	// *************************************************************************************************************//
	// &&&&&&&&&&&&&&&&& SPECIAL BLOCK FUNCTION - setSpecialBlockLocal
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
	protected final void setSpecialBlockLocal(int x, int y, int z, Block block, int metadata, String extra) {
		if (extra.equals(TemplateRule.SPECIAL_AIR))
			return; // preserve existing world block
		ChunkCoordinates pt = getAbsXYZPt(x, y, z);
		if (block == null) {
			if(extra.equals(TemplateRule.SPECIAL_STAIR) && metadata<=0){
				world.setBlock(pt.posX, pt.posY, pt.posZ, Block.stoneSingleSlab.blockID, rotateMetadata(Block.stoneSingleSlab, -metadata), 2);
				return;
			}
			if (extra.equals(TemplateRule.SPECIAL_PAINT) && metadata>=PAINTING_BLOCK_OFFSET) {//Remember:Paintings are not blocks
				delayedBuildQueue.offer(new PlacedBlock(block, new int[]{pt.posX, pt.posY, pt.posZ, metadata}));
				return;
			}

			Block presentBlock = Block.blocksList[world.getBlockId(pt.posX, pt.posY, pt.posZ)];

			if (presentBlock != null && !presentBlock.isAirBlock(world, pt.posX, pt.posY, pt.posZ) && !BlockProperties.get(presentBlock).isWater) {
				if (!(BlockProperties.get(world.getBlockId(pt.posX - 1, pt.posY, pt.posZ)).isWater || BlockProperties.get(world.getBlockId(pt.posX, pt.posY, pt.posZ - 1)).isWater
						|| BlockProperties.get(world.getBlockId(pt.posX + 1, pt.posY, pt.posZ)).isWater || BlockProperties.get(world.getBlockId(pt.posX, pt.posY, pt.posZ + 1)).isWater || BlockProperties.get(world.getBlockId(pt.posX, pt.posY + 1,
								pt.posZ)).isWater)) {// don't adjacent to a water block
					world.setBlockToAir(pt.posX, pt.posY, pt.posZ);
				}
			}
		}else if(block instanceof BlockMobSpawner){
			setMobSpawner(pt, block, metadata, extra);
		}else if(block instanceof BlockChest){
			setLootChest(pt, block, metadata, extra);
		}else{
			world.setBlock(pt.posX, pt.posY, pt.posZ, block.blockID, metadata, 2);
		}
	}

	private void addDoorToNewListIfAppropriate(int par1, int par2, int par3) {
		if (!(this.wgt instanceof WorldGenWalledCity)) {
			return;
		}
		int id = getKnownBuilding();
		if (id == -1) {
			((PopulatorWalledCity) this.wgt.master).cityDoors.put(bID, new ArrayList<VillageDoorInfo>());
			id = bID;
		}
		int l = ((BlockDoor) Block.doorWood).getDoorOrientation(this.world, par1, par2, par3);
		int i1;
		if (l != 0 && l != 2) {
			i1 = 0;
			for (int j1 = -5; j1 < 0; ++j1) {
				if (this.world.canBlockSeeTheSky(par1, par2, par3 + j1)) {
					--i1;
				}
			}
			for (int j1 = 1; j1 <= 5; ++j1) {
				if (this.world.canBlockSeeTheSky(par1, par2, par3 + j1)) {
					++i1;
				}
			}
			if (i1 != 0) {
				((PopulatorWalledCity) this.wgt.master).cityDoors.get(id).add(new VillageDoorInfo(par1, par2, par3, 0, i1 > 0 ? -2 : 2, 0));
			}
		} else {
			i1 = 0;
			for (int j1 = -5; j1 < 0; ++j1) {
				if (this.world.canBlockSeeTheSky(par1 + j1, par2, par3)) {
					--i1;
				}
			}
			for (int j1 = 1; j1 <= 5; ++j1) {
				if (this.world.canBlockSeeTheSky(par1 + j1, par2, par3)) {
					++i1;
				}
			}
			if (i1 != 0) {
				((PopulatorWalledCity) this.wgt.master).cityDoors.get(id).add(new VillageDoorInfo(par1, par2, par3, i1 > 0 ? -2 : 2, 0, 0));
			}
		}
	}

	// ******************** LOCAL COORDINATE FUNCTIONS - HELPER FUNCTIONS
	// *************************************************************************************************************//
	private final void emptyIfChest(ChunkCoordinates pt) {
		// if block is a chest empty it
		if (pt != null && world.getBlockId(pt.posX, pt.posY, pt.posZ) == Block.chest.blockID) {
			TileEntityChest tileentitychest = (TileEntityChest) world.getBlockTileEntity(pt.posX, pt.posY, pt.posZ);
			for (int m = 0; m < tileentitychest.getSizeInventory(); m++)
				tileentitychest.setInventorySlotContents(m, null);
		}
	}

	private ItemStack getChestItemstack(int chestType) {
		if (chestType == TOWER_CHEST && random.nextInt(4) == 0) { // for tower chests, chance of returning the tower block
			return new ItemStack(bRule.primaryBlock.get(), random.nextInt(10), bRule.primaryBlock.getMeta());
		}
		int[][] itempool = wgt.chestItems[chestType];
		int idx = pickWeightedOption(world.rand, itempool[3], itempool[0]);
		return new ItemStack(itempool[1][idx], itempool[4][idx] + random.nextInt(itempool[5][idx] - itempool[4][idx] + 1), itempool[2][idx]);
	}

	private int getKnownBuilding() {
		Set<?> keys = ((PopulatorWalledCity) this.wgt.master).cityDoors.keySet();
		for (int id = bID - 3; id < bID + 4; id++) {
			if (keys.contains(id))
				return id;
		}
		return -1;
	}

	private int rotateMetadata(Block blockID, int metadata) {
		int tempdata = 0;
		if (BlockProperties.get(blockID).isStair) {
			if (metadata >= 4) {
				tempdata += 4;
				metadata -= 4;
			}
			return STAIRS_DIR_TO_META[orientDirToBDir(STAIRS_META_TO_DIR[metadata])] + tempdata;
		}
		if (BlockProperties.get(blockID).isDoor) {
			// think of door metas applying to doors with hinges on the left
			// that open in (when seen facing in)
			// in this case, door metas match the dir in which the door opens
			// e.g. a door on the south face of a wall, opening in to the north
			// has a meta value of 0 (or 4 if the door is opened).
			if (metadata >= 8)// >=8:the top half of the door
				return metadata;
			if (metadata >= 4) {
				// >=4:the door is open
				tempdata += 4;
			}
			return DOOR_DIR_TO_META[orientDirToBDir(DOOR_META_TO_DIR[metadata % 4])] + tempdata;
		}
		if(blockID==Block.lever||blockID==Block.stoneButton||blockID==Block.woodenButton){
			// check to see if this is flagged as thrown
			if (metadata - 8 > 0) {
				tempdata += 8;
				metadata -= 8;
			}
			if (metadata == 0 || (blockID==Block.lever && metadata >= 5))
				return metadata + tempdata;
			return BUTTON_DIR_TO_META[orientDirToBDir(STAIRS_META_TO_DIR[metadata - 1])] + tempdata;
		}else if(blockID==Block.torchWood||blockID==Block.torchRedstoneActive||blockID==Block.torchRedstoneIdle){
			if (metadata == 0 || metadata >= 5) {
				return metadata;
			}
			return BUTTON_DIR_TO_META[orientDirToBDir(STAIRS_META_TO_DIR[metadata - 1])];
		}else if(blockID==Block.ladder||blockID==Block.dispenser||blockID==Block.furnaceIdle||blockID==Block.furnaceBurning||blockID==Block.signWall||blockID==Block.pistonBase||blockID==Block.pistonExtension||blockID==Block.chest||blockID==Block.hopperBlock||blockID==Block.dropper){
			if (blockID==Block.pistonBase|| blockID==Block.pistonExtension) {
				if (metadata - 8 >= 0) {
					// pushed or not, sticky or not
					tempdata += 8;
					metadata -= 8;
				}
			}
			if (metadata <= 1)
				return metadata + tempdata;
			return LADDER_DIR_TO_META[orientDirToBDir(LADDER_META_TO_DIR[metadata - 2])] + tempdata;
		}else if(blockID==Block.rail||blockID==Block.railPowered||blockID==Block.railDetector||blockID==Block.railActivator){
			switch (bDir) {
			case DIR_NORTH:
				// flat tracks
				if (metadata == 0) {
					return 0;
				}
				if (metadata == 1) {
					return 1;
				}
				// ascending tracks
				if (metadata == 2) {
					return 2;
				}
				if (metadata == 3) {
					return 3;
				}
				if (metadata == 4) {
					return bHand == 1 ? 4 : 5;
				}
				if (metadata == 5) {
					return bHand == 1 ? 5 : 4;
				}
				// curves
				if (metadata == 6) {
					return bHand == 1 ? 6 : 9;
				}
				if (metadata == 7) {
					return bHand == 1 ? 7 : 8;
				}
				if (metadata == 8) {
					return bHand == 1 ? 8 : 7;
				}
				if (metadata == 9) {
					return bHand == 1 ? 9 : 6;
				}
			case DIR_EAST:
				// flat tracks
				if (metadata == 0) {
					return 1;
				}
				if (metadata == 1) {
					return 0;
				}
				// ascending tracks
				if (metadata == 2) {
					return 5;
				}
				if (metadata == 3) {
					return 4;
				}
				if (metadata == 4) {
					return bHand == 1 ? 2 : 3;
				}
				if (metadata == 5) {
					return bHand == 1 ? 3 : 2;
				}
				// curves
				if (metadata == 6) {
					return bHand == 1 ? 7 : 6;
				}
				if (metadata == 7) {
					return bHand == 1 ? 8 : 9;
				}
				if (metadata == 8) {
					return bHand == 1 ? 9 : 8;
				}
				if (metadata == 9) {
					return bHand == 1 ? 6 : 7;
				}
			case DIR_SOUTH:
				// flat tracks
				if (metadata == 0) {
					return 0;
				}
				if (metadata == 1) {
					return 1;
				}
				// ascending tracks
				if (metadata == 2) {
					return 3;
				}
				if (metadata == 3) {
					return 2;
				}
				if (metadata == 4) {
					return bHand == 1 ? 5 : 4;
				}
				if (metadata == 5) {
					return bHand == 1 ? 4 : 5;
				}
				// curves
				if (metadata == 6) {
					return bHand == 1 ? 8 : 7;
				}
				if (metadata == 7) {
					return bHand == 1 ? 9 : 6;
				}
				if (metadata == 8) {
					return bHand == 1 ? 6 : 9;
				}
				if (metadata == 9) {
					return bHand == 1 ? 7 : 8;
				}
			case DIR_WEST:
				// flat tracks
				if (metadata == 0) {
					return 1;
				}
				if (metadata == 1) {
					return 0;
				}
				// ascending tracks
				if (metadata == 2) {
					return 4;
				}
				if (metadata == 3) {
					return 5;
				}
				if (metadata == 4) {
					return bHand == 1 ? 3 : 2;
				}
				if (metadata == 5) {
					return bHand == 1 ? 2 : 3;
				}
				// curves
				if (metadata == 6) {
					return bHand == 1 ? 9 : 8;
				}
				if (metadata == 7) {
					return bHand == 1 ? 6 : 7;
				}
				if (metadata == 8) {
					return bHand == 1 ? 7 : 6;
				}
				if (metadata == 9) {
					return bHand == 1 ? 8 : 9;
				}
			}
		}else if(blockID==Block.bed||blockID==Block.fenceGate||blockID==Block.tripWireSource||blockID==Block.pumpkin||blockID==Block.pumpkinLantern||blockID==Block.redstoneRepeaterActive||blockID==Block.redstoneRepeaterIdle){
			while (metadata >= 4) {
				tempdata += 4;
				metadata -= 4;
			}
			if (blockID==Block.trapdoor)
				return TRAPDOOR_DIR_TO_META[orientDirToBDir(TRAPDOOR_META_TO_DIR[metadata])] + tempdata;
			else
				return BED_DIR_TO_META[orientDirToBDir(BED_META_TO_DIR[metadata])] + tempdata;
		}else if(blockID==Block.vine){
			if (metadata == 0)
				return 0;
			else if (metadata == 1 || metadata == 2 || metadata == 4 || metadata == 8)
				return VINES_DIR_TO_META[(bDir + VINES_META_TO_DIR[metadata]) % 4];
			else
				return 1; // default case since vine do not have to have correct
			// metadata
		}else if(blockID==Block.signPost){
			// sign posts
			switch (bDir) {
			case DIR_NORTH:
				if (metadata == 0) {
					return bHand == 1 ? 0 : 8;
				}
				if (metadata == 1) {
					return bHand == 1 ? 1 : 7;
				}
				if (metadata == 2) {
					return bHand == 1 ? 2 : 6;
				}
				if (metadata == 3) {
					return bHand == 1 ? 3 : 5;
				}
				if (metadata == 4) {
					return 4;
				}
				if (metadata == 5) {
					return bHand == 1 ? 5 : 3;
				}
				if (metadata == 6) {
					return bHand == 1 ? 6 : 2;
				}
				if (metadata == 7) {
					return bHand == 1 ? 7 : 1;
				}
				if (metadata == 8) {
					return bHand == 1 ? 8 : 0;
				}
				if (metadata == 9) {
					return bHand == 1 ? 9 : 15;
				}
				if (metadata == 10) {
					return bHand == 1 ? 10 : 14;
				}
				if (metadata == 11) {
					return bHand == 1 ? 11 : 13;
				}
				if (metadata == 12) {
					return 12;
				}
				if (metadata == 13) {
					return bHand == 1 ? 13 : 11;
				}
				if (metadata == 14) {
					return bHand == 1 ? 14 : 10;
				}
				if (metadata == 15) {
					return bHand == 1 ? 15 : 9;
				}
			case DIR_EAST:
				if (metadata == 0) {
					return bHand == 1 ? 4 : 12;
				}
				if (metadata == 1) {
					return bHand == 1 ? 5 : 11;
				}
				if (metadata == 2) {
					return bHand == 1 ? 6 : 10;
				}
				if (metadata == 3) {
					return bHand == 1 ? 7 : 9;
				}
				if (metadata == 4) {
					return 8;
				}
				if (metadata == 5) {
					return bHand == 1 ? 9 : 7;
				}
				if (metadata == 6) {
					return bHand == 1 ? 10 : 6;
				}
				if (metadata == 7) {
					return bHand == 1 ? 11 : 5;
				}
				if (metadata == 8) {
					return bHand == 1 ? 12 : 4;
				}
				if (metadata == 9) {
					return bHand == 1 ? 13 : 3;
				}
				if (metadata == 10) {
					return bHand == 1 ? 14 : 2;
				}
				if (metadata == 11) {
					return bHand == 1 ? 15 : 1;
				}
				if (metadata == 12) {
					return 0;
				}
				if (metadata == 13) {
					return bHand == 1 ? 1 : 15;
				}
				if (metadata == 14) {
					return bHand == 1 ? 2 : 14;
				}
				if (metadata == 15) {
					return bHand == 1 ? 3 : 13;
				}
			case DIR_SOUTH:
				if (metadata == 0) {
					return bHand == 1 ? 8 : 0;
				}
				if (metadata == 1) {
					return bHand == 1 ? 9 : 15;
				}
				if (metadata == 2) {
					return bHand == 1 ? 10 : 14;
				}
				if (metadata == 3) {
					return bHand == 1 ? 11 : 13;
				}
				if (metadata == 4) {
					return 12;
				}
				if (metadata == 5) {
					return bHand == 1 ? 13 : 11;
				}
				if (metadata == 6) {
					return bHand == 1 ? 14 : 10;
				}
				if (metadata == 7) {
					return bHand == 1 ? 15 : 9;
				}
				if (metadata == 8) {
					return bHand == 1 ? 0 : 8;
				}
				if (metadata == 9) {
					return bHand == 1 ? 1 : 7;
				}
				if (metadata == 10) {
					return bHand == 1 ? 2 : 6;
				}
				if (metadata == 11) {
					return bHand == 1 ? 3 : 5;
				}
				if (metadata == 12) {
					return 4;
				}
				if (metadata == 13) {
					return bHand == 1 ? 5 : 3;
				}
				if (metadata == 14) {
					return bHand == 1 ? 6 : 2;
				}
				if (metadata == 15) {
					return bHand == 1 ? 7 : 1;
				}
			case DIR_WEST:
				if (metadata == 0) {
					return bHand == 1 ? 12 : 4;
				}
				if (metadata == 1) {
					return bHand == 1 ? 13 : 3;
				}
				if (metadata == 2) {
					return bHand == 1 ? 14 : 2;
				}
				if (metadata == 3) {
					return bHand == 1 ? 15 : 1;
				}
				if (metadata == 4) {
					return 0;
				}
				if (metadata == 5) {
					return bHand == 1 ? 1 : 15;
				}
				if (metadata == 6) {
					return bHand == 1 ? 2 : 14;
				}
				if (metadata == 7) {
					return bHand == 1 ? 3 : 13;
				}
				if (metadata == 8) {
					return bHand == 1 ? 4 : 12;
				}
				if (metadata == 9) {
					return bHand == 1 ? 5 : 11;
				}
				if (metadata == 10) {
					return bHand == 1 ? 6 : 10;
				}
				if (metadata == 11) {
					return bHand == 1 ? 7 : 9;
				}
				if (metadata == 12) {
					return 8;
				}
				if (metadata == 13) {
					return bHand == 1 ? 9 : 7;
				}
				if (metadata == 14) {
					return bHand == 1 ? 10 : 6;
				}
				if (metadata == 15) {
					return bHand == 1 ? 11 : 5;
				}
			}
		}
		return metadata + tempdata;
	}

	private void setLootChest(ChunkCoordinates pt, Block chestBlock, int meta, String chestType) {
		if (world.setBlock(pt.posX, pt.posY, pt.posZ, chestBlock.blockID, meta, 2)) {
			TileEntityChest chest = (TileEntityChest) world.getBlockTileEntity(pt.posX, pt.posY, pt.posZ);
			if (wgt.chestTries != null && wgt.chestTries.containsKey(chestType)) {
				for (int m = 0; m < wgt.chestTries.get(chestType); m++) {
					if (random.nextBoolean()) {
						ItemStack itemstack = getChestItemstack(chestType);
						if (itemstack != null && chest != null)
							chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), itemstack);
					}
				}
			}
		}
	}

	// &&&&&&&&&&&&&&&&& SPECIAL BLOCK FUNCTION - setMobSpawner
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
	private void setMobSpawner(ChunkCoordinates pt, Block spawner, int metadata, String info) {
		if(world.setBlock(pt.posX, pt.posY, pt.posZ, spawner.blockID, metadata, 2)){
			TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner) world.getBlockTileEntity(pt.posX, pt.posY, pt.posZ);
			if (tileentitymobspawner != null){
				if(info.equals("UPRIGHT")) {
					if (random.nextInt(3) == 0)
						setMobSpawner(tileentitymobspawner, 1, 3);
					else
						setMobSpawner(tileentitymobspawner, 2, 0);
				}else if(info.equals("EASY")){
					setMobSpawner(tileentitymobspawner, 2, 0);
				}else if(info.equals("MEDIUM")){
					setMobSpawner(tileentitymobspawner, 3, 0);
				}else if(info.equals("HARD")){
					setMobSpawner(tileentitymobspawner, 4, 0);
				}else
					tileentitymobspawner.getSpawnerLogic().setMobID(info);
			}
		}
	}

	private void setMobSpawner(TileEntityMobSpawner spawner, int nTypes, int offset) {
		String mob = "Pig";
		int n = random.nextInt(nTypes) + offset;
		if(n<SPAWNERS.length){
			mob = SPAWNERS[n];
		}
		spawner.getSpawnerLogic().setMobID(mob);
	}

	public static int distance(ChunkCoordinates pt1, ChunkCoordinates pt2) {
		return (int)Math.sqrt(pt1.getDistanceSquaredToChunkCoordinates(pt2));
	}

	public static int findSurfaceJ(World world, int i, int k, int jinit, boolean wallIsSurface, int waterSurfaceBuffer) {
		Block blockId;
		//if(world.getChunkProvider().chunkExists(i>>4, k>>4))
		{
			if (world.provider.isHellWorld) {// the Nether
				if ((i % 2 == 1) ^ (k % 2 == 1)) {
					for (int j = (int) (WORLD_MAX_Y * 0.5); j > -1; j--) {
						if (world.isAirBlock(i, j, k))
							for (; j > -1; j--)
								if (!BlockProperties.get(world.getBlockId(i, j, k)).isWallable)
									return j;
					}
				} else {
					for (int j = 0; j <= (int) (WORLD_MAX_Y * 0.5); j++)
						if (world.isAirBlock(i, j, k))
							return j;
				}
				return -1;
			} else { // other dimensions
				int minecraftHeight = world.getChunkFromBlockCoords(i, k).getHeightValue(i & 0xf, k & 0xf);
				if (minecraftHeight < jinit)
					jinit = minecraftHeight;
				for (int j = jinit; j >= 0; j--) {
					blockId = Block.blocksList[world.getBlockId(i, j, k)];
					if (!BlockProperties.get(blockId).isWallable && (wallIsSurface || !BlockProperties.get(blockId).isArtificial))
						return j;
					if (waterSurfaceBuffer != IGNORE_WATER && BlockProperties.get(blockId).isWater)
						return BlockProperties.get(world.getBlockId(i, j - waterSurfaceBuffer, k)).isWater ? HIT_WATER : j;
					// so we can still build in swamps...
				}
			}
		}
		return -1;
	}

	public final static int flipDir(int dir) {
		return (dir + 2) % 4;
	}

	public final static String globalCoordString(int i, int j, int k) {
		return "(" + i + "," + j + "," + k + ")";
	}

	public final static String globalCoordString(int[] pt) {
		return "(" + pt[0] + "," + pt[1] + "," + pt[2] + ")";
	}

	public static String metaValueCheck(Block blockID, int metadata) {
		if (metadata < 0 || metadata >= 16)
			return "All Minecraft meta values should be between 0 and 15";
		String fail = blockID.getUnlocalizedName() + " meta value should be between";
		if (BlockProperties.get(blockID).isStair)
			return metadata < 8 ? null : fail + " 0 and 7";
		// orientation metas
		if(blockID==Block.rail){
			return metadata < 10 ? null : fail + " 0 and 9";
		}else if(blockID instanceof BlockButton){
			return metadata % 8 > 0 && metadata % 8 < 5 ? null : fail + " 1 and 4 or 9 and 12";
		}else if(blockID==Block.ladder||blockID==Block.dispenser||blockID==Block.furnaceIdle||blockID==Block.furnaceBurning||blockID==Block.signWall
				||blockID==Block.pistonBase||blockID==Block.pistonExtension||blockID==Block.chest||blockID==Block.hopperBlock||blockID==Block.dropper||blockID==Block.railPowered||blockID==Block.railDetector||blockID==Block.railActivator){
			return metadata % 8 < 6 ? null : fail + " 0 and 5 or 8 and 13";
		}else if(blockID==Block.pumpkin||blockID==Block.pumpkinLantern){
			return metadata < 5 ? null : fail + " 0 and 4";
		}else if(blockID==Block.fenceGate){
			return metadata < 8 ? null : fail + " 0 and 7";
		}else if(blockID==Block.woodSingleSlab ||blockID==Block.bed){
			return metadata % 8 < 4 ? null : fail + " 0 and 3 or 8 and 11";
		}else if(blockID==Block.torchWood||blockID==Block.torchRedstoneIdle||blockID==Block.torchRedstoneActive){
			return metadata > 0 && metadata < 7 ? null : fail + " 1 and 6";
		}
		return null;
	}

	public static int pickWeightedOption(Random random, int[] weights, int[] options) {
		int sum = 0, n;
		for (n = 0; n < weights.length; n++)
			sum += weights[n];
		if (sum <= 0) {
			System.err.println("Error selecting options, weightsum not positive!");
			return options[0]; // default to returning first option
		}
		int s = random.nextInt(sum);
		sum = 0;
		n = 0;
		while (n < weights.length) {
			sum += weights[n];
			if (sum > s)
				return options[n];
			n++;
		}
		return options[options.length - 1];
	}

	public final static int rotDir(int dir, int rotation) {
		return (dir + rotation + 4) % 4;
	}

	public static void setBlockAndMetaNoLighting(World world, int i, int j, int k, Block blockId, int meta) {
		if (i < 0xfe363c80 || k < 0xfe363c80 || i >= 0x1c9c380 || k >= 0x1c9c380 || j < 0 || j > Building.WORLD_MAX_Y)
			return;
		world.getChunkFromChunkCoords(i >> 4, k >> 4).setBlockIDWithMetadata(i & 0xf, j, k & 0xf, blockId.blockID, meta);
	}

	// ******************** STATIC FUNCTIONS
	// ******************************************************************************************************************************************//
	public static void setBlockNoLighting(World world, int i, int j, int k, Block block) {
		if (i < 0xfe363c80 || k < 0xfe363c80 || i >= 0x1c9c380 || k >= 0x1c9c380 || j < 0 || j > Building.WORLD_MAX_Y)
			return;
		world.setBlock(i, j, k, block.blockID, 0, 2);
	}

	protected static void fillDown(ChunkCoordinates lowPt, int jtop, World world) {
		while (BlockProperties.get(world.getBlockId(lowPt.posX, lowPt.posY, lowPt.posZ)).isArtificial)
			lowPt.posY--;

		Block oldSurfaceBlockId = Block.blocksList[world.getBlockId(lowPt.posX, lowPt.posY, lowPt.posZ)];
		if (BlockProperties.get(oldSurfaceBlockId).isOre)
			oldSurfaceBlockId = Block.stone;
		if (oldSurfaceBlockId == Block.dirt || (lowPt.posY <= SEA_LEVEL && oldSurfaceBlockId == Block.sand))
			oldSurfaceBlockId = Block.grass;
		if (oldSurfaceBlockId == null)
			oldSurfaceBlockId = world.provider.isHellWorld ? Block.netherrack : Block.grass;
		Block fillBlockId = oldSurfaceBlockId == Block.grass ? Block.dirt : oldSurfaceBlockId;
		for (; lowPt.posY <= jtop; lowPt.posY++)
			setBlockAndMetaNoLighting(world, lowPt.posX, lowPt.posY, lowPt.posZ, lowPt.posY == jtop ? oldSurfaceBlockId : fillBlockId, 0);
	}

	protected final static int minOrMax(int[] a, boolean isMin) {
		if (isMin) {
			int min = Integer.MAX_VALUE;
			for (int i : a)
				min = Math.min(min, i);
			return min;
		} else {
			int max = Integer.MIN_VALUE;
			for (int i : a)
				max = Math.max(max, i);
			return max;
		}
	}

	// wiggle allows for some leeway before nonzero is detected
	protected final static int signum(int n, int wiggle) {
		if (n <= wiggle && -n <= wiggle)
			return 0;
		return n < 0 ? -1 : 1;
	}

	private final static void circleShape(int diam) {
		float rad = diam / 2.0F;
		float[][] shape_density = new float[diam][diam];
		for (int x = 0; x < diam; x++)
			for (int y = 0; y < diam; y++)
				shape_density[y][x] = ((x + 0.5F - rad) * (x + 0.5F - rad) + (y + 0.5F - rad) * (y + 0.5F - rad)) / (rad * rad);
		int[] xheight = new int[diam];
		for (int y = 0; y < diam; y++) {
			int x = 0;
			for (; shape_density[y][x] > 1.0F; x++) {
			}
			xheight[y] = x;
		}
		CIRCLE_SHAPE[diam] = new int[diam][diam];
		CIRCLE_CRENEL[diam] = new int[diam][diam];
		SPHERE_SHAPE[diam] = new int[(diam + 1) / 2];
		int nextHeight = 0, crenel_adj = 0;
		for (int x = 0; x < diam; x++)
			for (int y = 0; y < diam; y++) {
				CIRCLE_SHAPE[diam][y][x] = 0;
				CIRCLE_CRENEL[diam][y][x] = 0;
			}
		for (int y = 0; y < diam; y++) {
			if (y == 0 || y == diam - 1)
				nextHeight = diam / 2 + 1;
			else
				nextHeight = xheight[y < diam / 2 ? y - 1 : y + 1] + (xheight[y] == xheight[y < diam / 2 ? y - 1 : y + 1] ? 1 : 0);
			if (y > 0 && xheight[y] == xheight[y - 1])
				crenel_adj++;
			int x = 0;
			for (; x < xheight[y]; x++) {
				CIRCLE_SHAPE[diam][y][x] = -1;
				CIRCLE_SHAPE[diam][y][diam - x - 1] = -1;
				CIRCLE_CRENEL[diam][y][x] = -1;
				CIRCLE_CRENEL[diam][y][diam - x - 1] = -1;
			}
			for (; x < nextHeight; x++) {
				CIRCLE_SHAPE[diam][y][x] = 1;
				CIRCLE_SHAPE[diam][y][diam - x - 1] = 1;
				CIRCLE_CRENEL[diam][y][x] = (x + crenel_adj) % 2;
				CIRCLE_CRENEL[diam][y][diam - x - 1] = (x + crenel_adj + diam + 1) % 2;
			}
		}
		for (int y = diam / 2; y < diam; y++)
			SPHERE_SHAPE[diam][y - diam / 2] = (2 * (diam / 2 - xheight[y]) + (diam % 2 == 0 ? 0 : 1));
	}

	private final static boolean isSolidBlock(Block block) {
		return block != null && block.blockMaterial.isSolid();
	}
}