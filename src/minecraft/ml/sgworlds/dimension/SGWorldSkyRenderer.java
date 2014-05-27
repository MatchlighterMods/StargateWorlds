package ml.sgworlds.dimension;

import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.IWorldFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SGWorldSkyRenderer extends IRenderHandler {

	private final SGWorldData worldData;

	private int glSkyList;
	private int glSkyList2;
	private boolean setup;

	public SGWorldSkyRenderer(SGWorldData wdata) {
		worldData = wdata;
	}

	@SideOnly(Side.CLIENT)
	private void setup() {
		setup = true;

		Tessellator tessellator = Tessellator.instance;
		this.glSkyList = GLAllocation.generateDisplayLists(2);
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		byte b2 = 64;
		int i = 256 / b2 + 2;
		float f = 16.0F;
		int j;
		int k;

		for (j = -b2 * i; j <= b2 * i; j += b2) {
			for (k = -b2 * i; k <= b2 * i; k += b2) {
				tessellator.startDrawingQuads();
				tessellator.addVertex((double)(j + 0), (double)f, (double)(k + 0));
				tessellator.addVertex((double)(j + b2), (double)f, (double)(k + 0));
				tessellator.addVertex((double)(j + b2), (double)f, (double)(k + b2));
				tessellator.addVertex((double)(j + 0), (double)f, (double)(k + b2));
				tessellator.draw();
			}
		}

		GL11.glEndList();
		this.glSkyList2 = glSkyList + 1;
		GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
		f = -16.0F;
		tessellator.startDrawingQuads();

		for (j = -b2 * i; j <= b2 * i; j += b2) {
			for (k = -b2 * i; k <= b2 * i; k += b2) {
				tessellator.addVertex((double)(j + b2), (double)f, (double)(k + 0));
				tessellator.addVertex((double)(j + 0), (double)f, (double)(k + 0));
				tessellator.addVertex((double)(j + 0), (double)f, (double)(k + b2));
				tessellator.addVertex((double)(j + b2), (double)f, (double)(k + b2));
			}
		}

		tessellator.draw();
		GL11.glEndList();
	}

	private void renderType(FeatureType type, float partialTicks, WorldClient world, Minecraft mc) {
		for (IWorldFeature feat : this.worldData.getFeatures(type)) {
			if (feat instanceof IWorldFeature.IWorldFeatureRender) {
				GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				((IWorldFeature.IWorldFeatureRender)feat).render(partialTicks, world, mc);
				GL11.glPopMatrix();
			}
		}
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		if (!setup) setup();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Vec3 vec3 = world.getSkyColor(mc.renderViewEntity, partialTicks);
		float skyRed = (float)vec3.xCoord;
		float skyGreen = (float)vec3.yCoord;
		float skyBlue = (float)vec3.zCoord;
		float f4;

		if (mc.gameSettings.anaglyph) {
			float f5 = (skyRed * 30.0F + skyGreen * 59.0F + skyBlue * 11.0F) / 100.0F;
			float f6 = (skyRed * 30.0F + skyGreen * 70.0F) / 100.0F;
			f4 = (skyRed * 30.0F + skyBlue * 70.0F) / 100.0F;
			skyRed = f5;
			skyGreen = f6;
			skyBlue = f4;
		}

		GL11.glColor3f(skyRed, skyGreen, skyBlue);
		Tessellator tessellator1 = Tessellator.instance;
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glColor3f(skyRed, skyGreen, skyBlue);
		GL11.glCallList(this.glSkyList);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		renderType(FeatureType.STARS, partialTicks, world, mc);
		renderType(FeatureType.SUN, partialTicks, world, mc);
		renderType(FeatureType.MOON, partialTicks, world, mc);
		renderType(FeatureType.SKY_FEATURE, partialTicks, world, mc);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);

		// Render Void
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		double horizonDiff = mc.thePlayer.getPosition(partialTicks).yCoord - world.getHorizon();

		if (horizonDiff < 0.0D) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 12.0F, 0.0F);
			GL11.glCallList(this.glSkyList2);
			GL11.glPopMatrix();
			float f8 = 1.0F;
			float f9 = -((float)(horizonDiff + 65.0D));
			float f10 = -f8;
			tessellator1.startDrawingQuads();
			tessellator1.setColorRGBA_I(0, 255);
			tessellator1.addVertex((double)(-f8), (double)f9, (double)f8);
			tessellator1.addVertex((double)f8, (double)f9, (double)f8);
			tessellator1.addVertex((double)f8, (double)f10, (double)f8);
			tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
			tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
			tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
			tessellator1.addVertex((double)f8, (double)f9, (double)(-f8));
			tessellator1.addVertex((double)(-f8), (double)f9, (double)(-f8));
			tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
			tessellator1.addVertex((double)f8, (double)f10, (double)f8);
			tessellator1.addVertex((double)f8, (double)f9, (double)f8);
			tessellator1.addVertex((double)f8, (double)f9, (double)(-f8));
			tessellator1.addVertex((double)(-f8), (double)f9, (double)(-f8));
			tessellator1.addVertex((double)(-f8), (double)f9, (double)f8);
			tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
			tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
			tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
			tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
			tessellator1.addVertex((double)f8, (double)f10, (double)f8);
			tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
			tessellator1.draw();
		}

		if (world.provider.isSkyColored()) {
			GL11.glColor3f(skyRed * 0.2F + 0.04F, skyGreen * 0.2F + 0.04F, skyBlue * 0.6F + 0.1F);
		} else {
			GL11.glColor3f(skyRed, skyGreen, skyBlue);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, -((float)(horizonDiff - 16.0D)), 0.0F);
		GL11.glCallList(this.glSkyList2);
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);

	}

}
