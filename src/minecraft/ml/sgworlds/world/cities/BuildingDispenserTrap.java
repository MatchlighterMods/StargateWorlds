package ml.sgworlds.world.cities;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ChunkCoordinates;

/*
 *  Source code for the Walled City Generator and CARuins Mods for the game Minecraft
 *  Copyright (C) 2011 by formivore

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * BuildingDispenserTrap generates a redstone activated dispenser trap
 */
public class BuildingDispenserTrap extends Building {
	public final static int ARROW_MISSILE = 0, DAMAGE_POTION_MISSILE = 1;

	private static int[][] CODE_TO_BLOCK = new int[][] { { PRESERVE_ID, 0 }, {}, { 0, 0 }, { REDSTONE_WIRE_ID, 0 }, { REDSTONE_TORCH_ON_ID, BUTTON_DIR_TO_META[DIR_NORTH] }, { REDSTONE_TORCH_OFF_ID, BUTTON_DIR_TO_META[DIR_SOUTH] }, { REDSTONE_TORCH_ON_ID, 5 } };

	private static int[][][] MECHANISM = new int[][][] { { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, { { 0, 0, 0 }, { 1, 1, 1 }, { 1, 4, 1 }, { 1, 1, 1 } }, { { 0, 0, 0 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 5, 1 } }, { { 0, 1, 0 }, { 1, 1, 1 }, { 1, 3, 1 }, { 1, 1, 1 } }, { { 0, 1, 0 }, { 1, 6, 1 }, { 1, 0, 1 }, { 1, 2, 1 } }, { { 0, 1, 0 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } }, };

	public BuildingDispenserTrap(WorldGeneratorThread wgt_, TemplateRule bRule_, int bDir_, int plateSeparation, ChunkCoordinates sourcePt) {
		super(0, wgt_, bRule_, bDir_, 1, true, new int[] { 3, 6, plateSeparation }, sourcePt);
	}

	// --- bLength+3 - end of mechanism
	// | |
	// | |
	// --- y=bLength - mechanism start,
	// * y==bLength-1 - end of redstone wire
	// *
	// 0 y=0 - trigger plate
	public void build(int missileType, boolean multipleTriggers) {
		if (bLength < 0)
			bLength = 0;
		// if (BuildingWall.DEBUG) FMLLog.getLogger().info("Building dispenser trap at "+i0+","+j0+","+k0+", plateSeparation="+bLength);
		for (int x = 0; x < MECHANISM[0][0].length; x++) {
			for (int y = 0; y < MECHANISM[0].length; y++) {
				for (int z = 0; z < MECHANISM.length; z++) {
					if (MECHANISM[z][3 - y][x] == 1)
						setBlockLocal(x, z - 3, y + bLength, bRule);
					else
						setBlockLocal(x, z - 3, y + bLength, CODE_TO_BLOCK[MECHANISM[z][3 - y][x]]);
				}
			}
		}
		for (int y = 0; y < bLength; y++) {
			setBlockLocal(1, -3, y, bRule);
			setBlockLocal(1, -2, y, Block.redstoneWire);
			setBlockLocal(0, -2, y, bRule);
			setBlockLocal(2, -2, y, bRule);
			setBlockLocal(1, -1, y, bRule);
			setBlockLocal(1, 0, y, multipleTriggers && random.nextBoolean() ? Block.pressurePlateStone : null);
			setBlockLocal(1, 1, y, (Block) null);
		}
		setBlockLocal(1, 0, 0, Block.pressurePlateStone);
		flushDelayed();
		ItemStack itemstack = missileType == ARROW_MISSILE ? new ItemStack(Item.arrow.itemID, 30 + random.nextInt(10), 0) : new ItemStack(Item.potion.itemID, 30 + random.nextInt(10), 12 | 0x4000);
		setItemDispenser(1, 1, bLength + 1, DIR_SOUTH, itemstack);
	}

	public boolean queryCanBuild(int minLength) {
		if (!isFloor(1, 0, 0))
			return false;
		// search along floor for a height 2 wall. If we find it, reset bLength and return true.
		for (int y = 1; y < 9; y++) {
			if (isFloor(1, 0, y))
				continue;
			if (!isWallBlock(1, -1, y))
				return false;
			// now must have block at (1,0,y)...
			if (y < minLength)
				return false;
			if (!isWallable(1, 1, y)) {
				bLength = y;
				return true;
			}
			return false;
		}
		return false;
	}

	private void setItemDispenser(int x, int z, int y, int metaDir, ItemStack itemstack) {
		ChunkCoordinates pt = getAbsXYZPt(x, z, y);
		world.setBlock(pt.posX, pt.posY, pt.posZ, Block.dispenser.blockID, 0, 2);
		world.setBlockMetadataWithNotify(pt.posX, pt.posY, pt.posZ, LADDER_DIR_TO_META[orientDirToBDir(metaDir)], 3);
		try {
			TileEntityDispenser tileentitychest = (TileEntityDispenser) world.getBlockTileEntity(pt.posX, pt.posY, pt.posZ);
			if (itemstack != null && tileentitychest != null)
				tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack);
		} catch (Exception e) {
			System.err.println("Error filling dispenser: " + e.toString());
			e.printStackTrace();
		}
	}
}