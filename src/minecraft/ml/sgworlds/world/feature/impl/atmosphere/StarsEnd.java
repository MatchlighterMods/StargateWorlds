package ml.sgworlds.world.feature.impl.atmosphere;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.WorldFeature.WorldFeatureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class StarsEnd extends WorldFeature implements WorldFeatureRender {

	private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");

	public StarsEnd(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

	@Override
	public void render(float partialTicks, World world, Minecraft mc) {
		mc.getTextureManager().bindTexture(locationEndSkyPng);
		Tessellator tessellator = Tessellator.instance;
		for (int i = 0; i < 6; ++i) {
			GL11.glPushMatrix();

			if (i == 1) {
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 2) {
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 3) {
				GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 4) {
				GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			}

			if (i == 5) {
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
			}

			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(2631720);
			tessellator.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
			tessellator.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
			tessellator.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
			tessellator.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
			tessellator.draw();
			GL11.glPopMatrix();
		}
	}

}
