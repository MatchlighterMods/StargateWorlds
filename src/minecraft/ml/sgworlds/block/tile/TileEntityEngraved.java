package ml.sgworlds.block.tile;

import ml.sgworlds.client.ClientProxy;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityEngraved extends TileEntityFacade {
	
	public String[] sideStrings = new String[6];
	public float scale = 1.0F;
	public int rotation = 0;
	public int fontColor = 0x686442;
	
	public void setString(int side, String str) {
		sideStrings[side] = str;
		onInventoryChanged();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.fontColor = tag.getInteger("fontColor");
		for (int i=0; i<sideStrings.length; i++) {
			sideStrings[i] = tag.getString("string_" + i);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("fontColor", fontColor);
		for (int i=0; i<sideStrings.length; i++) {
			if (sideStrings[i] == null) continue;
			tag.setString("string_" + i, sideStrings[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer() {
		return ClientProxy.fontRendererAncient; //Minecraft.getMinecraft().fontRenderer;
	}
	
}
