package ml.sgworlds.block.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFacade extends BaseTileEntity {

	protected Block[] sideBlocks = new Block[6];
	protected int[] sideBlockMetas = new int[6];
	
	public TileEntityFacade(Block blk, int meta) {
		setBlockSide(-1, blk, meta);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		for (int i=0; i<sideBlocks.length; i++) {
			sideBlocks[i] = Block.blocksList[tag.getInteger("blockId_" + i)];
			sideBlockMetas[i] = tag.getInteger("blockMeta_" + i);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		for (int i=0; i<sideBlocks.length; i++) {
			tag.setInteger("blockId_" + i, sideBlocks[i].blockID);
			tag.setInteger("blockMeta_" + i, sideBlockMetas[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIconFromSide(int side) {
		if (sideBlocks[side] == null) return null;
		return sideBlocks[side].getIcon(side, sideBlockMetas[side]);
	}
	
	public void setBlockSide(int side, Block blk, int meta) {
		if (side == -1) {
			for (int i=0; i<sideBlocks.length; i++) {
				sideBlocks[i] = blk;
				sideBlockMetas[i] = meta;
			}
		} else {
			sideBlocks[side] = blk;
			sideBlockMetas[side] = meta;
		}
		onInventoryChanged();
	}
	
}
