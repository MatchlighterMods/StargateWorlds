package ml.sgworlds.client.render;

import ml.core.block.BlockUtils;
import ml.sgworlds.block.tile.TileEntityEngraved;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class TEEngravedRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
		TileEntityEngraved tee = (TileEntityEngraved)tileentity;
		Tessellator tess = Tessellator.instance;
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);
		GL11.glRotatef(tee.rotation*90F, 0, 1.0F, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glNormal3i(0, 0, 1);
		
		renderEngraving(tee);
		
		GL11.glPopMatrix();
	}
	
	protected void renderEngraving(TileEntityEngraved tee) {
		FontRenderer fr = tee.getFontRenderer();
		GL11.glDisable(GL11.GL_LIGHTING);
		
		for (int i=0; i<6; i++) {
			if (tee.sideStrings[i] == null || tee.sideStrings[i].equals("")) continue;
			
			GL11.glPushMatrix();
			
			ForgeDirection fdir = ForgeDirection.getOrientation(i);
			BlockUtils.glRotateForFaceDir(fdir);
			
			int l = tee.worldObj.getLightBrightnessForSkyBlocks(tee.xCoord+fdir.offsetX, tee.yCoord+fdir.offsetY, tee.zCoord+fdir.offsetZ, 0);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(l % 65536) / 1.0F, (float)(l / 65536) / 1.0F);
			
			GL11.glTranslatef(1.0F, 1.0F, 0);
			GL11.glRotatef(180F, 0, 0, 1.0F);
			
			GL11.glTranslatef(0, 0, -0.001F);
			
			String[] lines = tee.sideStrings[i].split("\n");
			int mxLn = 1;
			for (String ln : lines) mxLn = Math.max(mxLn, fr.getStringWidth(ln));
			GL11.glScalef(1F / (float)mxLn, 1F / (float)(fr.FONT_HEIGHT * lines.length), 1F);
			
			GL11.glTranslatef(0.5F, 0.5F, 0);
			for (int k=0; k<lines.length; k++) {
				fr.drawString(lines[k], 0, 0, tee.fontColor);
				GL11.glTranslatef(0, fr.FONT_HEIGHT, 0);
			}
			
			GL11.glPopMatrix();
		}
		
		GL11.glEnable(GL11.GL_LIGHTING);
	}

}
