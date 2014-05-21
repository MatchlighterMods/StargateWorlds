package ml.sgworlds.dimension;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;

public class SGWorldSkyRenderer extends IRenderHandler {

	private final SGWorldController worldController;

    private int glSkyList;
    private int glSkyList2;
	
	public SGWorldSkyRenderer(SGWorldController wdata) {
		worldController = wdata;
	}

	@SideOnly(Side.CLIENT)
	private void setup() {
		Tessellator tessellator = Tessellator.instance;
        this.glSkyList = GLAllocation.generateDisplayLists(2);
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        byte b2 = 64;
        int i = 256 / b2 + 2;
        float f = 16.0F;
        int j;
        int k;

        for (j = -b2 * i; j <= b2 * i; j += b2)
        {
            for (k = -b2 * i; k <= b2 * i; k += b2)
            {
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

        for (j = -b2 * i; j <= b2 * i; j += b2)
        {
            for (k = -b2 * i; k <= b2 * i; k += b2)
            {
                tessellator.addVertex((double)(j + b2), (double)f, (double)(k + 0));
                tessellator.addVertex((double)(j + 0), (double)f, (double)(k + 0));
                tessellator.addVertex((double)(j + 0), (double)f, (double)(k + b2));
                tessellator.addVertex((double)(j + b2), (double)f, (double)(k + b2));
            }
        }

        tessellator.draw();
        GL11.glEndList();
	}
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Vec3 vec3 = world.getSkyColor(mc.renderViewEntity, partialTicks);
		float skyRed = (float)vec3.xCoord;
		float skyGreen = (float)vec3.yCoord;
		float skyBlue = (float)vec3.zCoord;
		float f4;

		if (mc.gameSettings.anaglyph)
		{
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
		
		// Render Horizon
		RenderHelper.disableStandardItemLighting();
		float[] horizonColors = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
		float f7;
		float f8;
		float f9;
		float f10;

		if (horizonColors != null)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			f4 = horizonColors[0];
			f7 = horizonColors[1];
			f8 = horizonColors[2];
			float f11;

			if (mc.gameSettings.anaglyph)
			{
				f9 = (f4 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
				f10 = (f4 * 30.0F + f7 * 70.0F) / 100.0F;
				f11 = (f4 * 30.0F + f8 * 70.0F) / 100.0F;
				f4 = f9;
				f7 = f10;
				f8 = f11;
			}

			tessellator1.startDrawing(6);
			tessellator1.setColorRGBA_F(f4, f7, f8, horizonColors[3]);
			tessellator1.addVertex(0.0D, 100.0D, 0.0D);
			byte b0 = 16;
			tessellator1.setColorRGBA_F(horizonColors[0], horizonColors[1], horizonColors[2], 0.0F);

			for (int j = 0; j <= b0; ++j)
			{
				f11 = (float)j * (float)Math.PI * 2.0F / (float)b0;
				float f12 = MathHelper.sin(f11);
				float f13 = MathHelper.cos(f11);
				tessellator1.addVertex((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * horizonColors[3]));
			}

			tessellator1.draw();
			GL11.glPopMatrix();
			GL11.glShadeModel(GL11.GL_FLAT);
		}

		// Prepare render Sun/Moon
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glPushMatrix();
		f4 = 1.0F - world.getRainStrength(partialTicks);
		f7 = 0.0F;
		f8 = 0.0F;
		f9 = 0.0F;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
		GL11.glTranslatef(f7, f8, f9);
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		
		// Render Sun
		f10 = 30.0F;
		this.renderEngine.bindTexture(locationSunPng);
		tessellator1.startDrawingQuads();
		tessellator1.addVertexWithUV((double)(-f10), 100.0D, (double)(-f10), 0.0D, 0.0D);
		tessellator1.addVertexWithUV((double)f10, 100.0D, (double)(-f10), 1.0D, 0.0D);
		tessellator1.addVertexWithUV((double)f10, 100.0D, (double)f10, 1.0D, 1.0D);
		tessellator1.addVertexWithUV((double)(-f10), 100.0D, (double)f10, 0.0D, 1.0D);
		tessellator1.draw();
		
		// Render Moon
		f10 = 20.0F;
		this.renderEngine.bindTexture(locationMoonPhasesPng);
		int k = this.theWorld.getMoonPhase();
		int l = k % 4;
		int i1 = k / 4 % 2;
		float f14 = (float)(l + 0) / 4.0F;
		float f15 = (float)(i1 + 0) / 2.0F;
		float f16 = (float)(l + 1) / 4.0F;
		float f17 = (float)(i1 + 1) / 2.0F;
		tessellator1.startDrawingQuads();
		tessellator1.addVertexWithUV((double)(-f10), -100.0D, (double)f10, (double)f16, (double)f17);
		tessellator1.addVertexWithUV((double)f10, -100.0D, (double)f10, (double)f14, (double)f17);
		tessellator1.addVertexWithUV((double)f10, -100.0D, (double)(-f10), (double)f14, (double)f15);
		tessellator1.addVertexWithUV((double)(-f10), -100.0D, (double)(-f10), (double)f16, (double)f15);
		tessellator1.draw();

		// Render Stars
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float f18 = world.getStarBrightness(partialTicks) * f4;
		if (f18 > 0.0F) {
			GL11.glColor4f(f18, f18, f18, f18);
			GL11.glCallList(this.starGLCallList);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		
		// Render Void
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		double horizonDiff = mc.thePlayer.getPosition(partialTicks).yCoord - world.getHorizon();

		if (horizonDiff < 0.0D)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 12.0F, 0.0F);
			GL11.glCallList(this.glSkyList2);
			GL11.glPopMatrix();
			f8 = 1.0F;
			f9 = -((float)(horizonDiff + 65.0D));
			f10 = -f8;
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
