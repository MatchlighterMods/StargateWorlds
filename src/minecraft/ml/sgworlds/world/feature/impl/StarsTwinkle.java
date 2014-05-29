package ml.sgworlds.world.feature.impl;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseCelestialObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class StarsTwinkle extends BaseCelestialObject {

	protected int[] starGLCallLists;
	protected long[] timeOffsets;
	private boolean setup;

	public StarsTwinkle(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public StarsTwinkle(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}
	
	public StarsTwinkle(FeatureProvider provider, IWorldData worldData, Random rand) {
		super(provider, worldData, rand);
	}

	private void setup() {
		setup=true;
		
		Random rnd = new Random();
		
		this.starGLCallLists = new int[10];
		timeOffsets = new long[starGLCallLists.length];
		int clists = GLAllocation.generateDisplayLists(starGLCallLists.length);
		for (int i=0; i<starGLCallLists.length; i++) {
			starGLCallLists[i] = clists+i;
			timeOffsets[i] = rnd.nextLong();
			
			GL11.glNewList(clists+i, GL11.GL_COMPILE);
			this.renderStars(150);
			GL11.glEndList();
		}
	}

	protected void renderStars(int count) {
		Random random = new Random(10842L);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for (int i = 0; i < count; ++i) {
			double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d0 *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for (int j = 0; j < 4; ++j) {
					double d17 = 0.0D;
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d20 = d18 * d16 - d19 * d15;
					double d21 = d19 * d16 + d18 * d15;
					double d22 = d20 * d12 + d17 * d13;
					double d23 = d17 * d12 - d20 * d13;
					double d24 = d23 * d9 - d21 * d10;
					double d25 = d21 * d9 + d23 * d10;
					tessellator.addVertex(d5 + d24, d6 + d22, d7 + d25);
				}
			}
		}

		tessellator.draw();
	}

	@Override
	protected void doRender(float partialTicks, World world, Minecraft mc) {
		if (!setup) setup();

		float f4 = 1.0F - world.getRainStrength(partialTicks);
		Tessellator tess = Tessellator.instance;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float f18 = world.getStarBrightness(partialTicks) * f4;

		if (f18 > 0.0F) {
			for (int i=0; i<starGLCallLists.length; i++) {
				GL11.glColor4f(f18, f18, f18, f18 * getBrightness(i, world, partialTicks));
				GL11.glCallList(this.starGLCallLists[i]);
			}
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	private float getBrightness(int set, World world, float partialTicks) {
		float f1 = (float)((world.getWorldTime() + timeOffsets[set]) % 200L) + partialTicks;
		float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + Math.abs(0.5F-partialTicks)/2.0F);

		if (f2 < 0.0F) f2 = 0.0F;
		if (f2 > 1.0F) f2 = 1.0F;
		
		return f2;
	}
}
