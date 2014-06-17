package ml.sgworlds.block.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityEngraved extends TileEntityFacade {
	
	protected String[] sideStrings = new String[6];
	protected float scale = 1.0F;
	
	public TileEntityEngraved(Block blk, int meta) {
		super(blk, meta);
		// TODO Auto-generated constructor stub
	}

	public void setString(int side, String str) {
		sideStrings[side] = str;
		onInventoryChanged();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		for (int i=0; i<sideStrings.length; i++) {
			sideStrings[i] = tag.getString("string_" + i);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		for (int i=0; i<sideStrings.length; i++) {
			tag.setString("string_" + i, sideStrings[i]);
		}
	}
	
}
