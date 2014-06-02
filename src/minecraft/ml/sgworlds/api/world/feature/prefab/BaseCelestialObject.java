package ml.sgworlds.api.world.feature.prefab;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.ICelestialObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public abstract class BaseCelestialObject extends WorldFeature implements ICelestialObject {

	public long orbitPeriod = 24000L;
	public int angle = 0;
	public float offset = -0.25F;
	public int size = 30;
	public ResourceLocation textureLocation;
	
	public BaseCelestialObject(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public BaseCelestialObject(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData);
		
		this.orbitPeriod = tag.getLong("period");
		this.angle = tag.getInteger("angle");
		this.offset = tag.getFloat("offset");
		this.size = tag.getInteger("size");
	}
	
	public BaseCelestialObject(FeatureProvider provider, IWorldData worldData, Random rnd) {
		super(provider, worldData);
		
		this.orbitPeriod = (rnd.nextInt(32-6)+6) * 1000;
		this.angle = rnd.nextInt(36)*10;
		this.offset = rnd.nextFloat();
		this.size = (rnd.nextInt(10-3)+3) * 5;
	}

	@Override
	public void render(float partialTicks, World world, Minecraft mc) {
		float celestialAngle = calculateCelestialAngle(world.getWorldTime(), partialTicks);
		
		renderHorizon(partialTicks, world, mc, celestialAngle, 1.0F);
		
		float invRainStrength = 1.0F - world.getRainStrength(partialTicks);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, invRainStrength);
		GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(celestialAngle * 360.0F, 1.0F, 0.0F, 0.0F);

		mc.getTextureManager().bindTexture(textureLocation);

		doRender(partialTicks, world, mc);
	}

	protected void doRender(float partialTicks, World world, Minecraft mc) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV((double)(-size), 100.0D, (double)(-size), 0.0D, 0.0D);
		tess.addVertexWithUV((double)size, 100.0D, (double)(-size), 1.0D, 0.0D);
		tess.addVertexWithUV((double)size, 100.0D, (double)size, 1.0D, 1.0D);
		tess.addVertexWithUV((double)(-size), 100.0D, (double)size, 0.0D, 1.0D);
		tess.draw();
	}

	// TODO Make Feature?
	private float[] colorsSunriseSunset = new float[4];
	protected float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		float f2 = 0.4F;
		float f3 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) - 0.0F;
		float f4 = -0.0F;

		if (f3 >= f4 - f2 && f3 <= f4 + f2) {
			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
			float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float)Math.PI)) * 0.99F;
			f6 *= f6;
			this.colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
			this.colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
			this.colorsSunriseSunset[3] = f6;
			return this.colorsSunriseSunset;
		} else {
			return null;
		}
	}

	protected void renderHorizon(float partialTicks, World world, Minecraft mc, float celestialAngle, float opacity) {
		RenderHelper.disableStandardItemLighting();
		float[] horizonColors = calcSunriseSunsetColors(celestialAngle, partialTicks);
		float f9;
		float f10;

		if (horizonColors != null) {
			float celestialAngleRads = celestialAngle * (float)Math.PI * 2.0F;
			Tessellator tess = Tessellator.instance;

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(MathHelper.sin(celestialAngleRads) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			float hRed = horizonColors[0];
			float hGreen = horizonColors[1];
			float hBlue = horizonColors[2];
			horizonColors[3] *= opacity;
			float f11;

			if (mc.gameSettings.anaglyph) {
				f9 = (hRed * 30.0F + hGreen * 59.0F + hBlue * 11.0F) / 100.0F;
				f10 = (hRed * 30.0F + hGreen * 70.0F) / 100.0F;
				f11 = (hRed * 30.0F + hBlue * 70.0F) / 100.0F;
				hRed = f9;
				hGreen = f10;
				hBlue = f11;
			}

			tess.startDrawing(6);
			tess.setColorRGBA_F(hRed, hGreen, hBlue, horizonColors[3]);
			tess.addVertex(0.0D, 100.0D, 0.0D);
			byte b0 = 16;
			tess.setColorRGBA_F(horizonColors[0], horizonColors[1], horizonColors[2], 0.0F);

			for (int j = 0; j <= b0; ++j) {
				f11 = (float)j * (float)Math.PI * 2.0F / (float)b0;
				float f12 = MathHelper.sin(f11);
				float f13 = MathHelper.cos(f11);
				tess.addVertex((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * horizonColors[3]));
			}

			tess.draw();
			GL11.glPopMatrix();
			GL11.glShadeModel(GL11.GL_FLAT);
		}
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTickTime) {
		if (this.orbitPeriod == 0L) return offset;

		int j = (int)(worldTime % orbitPeriod);
		float f1 = ((float)j + partialTickTime) / (float)orbitPeriod + offset;

		if (f1 < 0.0F) ++f1;
		if (f1 > 1.0F) --f1;

		float f2 = f1;
		f1 = 1.0F - (float)((Math.cos((double)f1 * Math.PI) + 1.0D) / 2.0D);
		f1 = f2 + (f1 - f2) / 3.0F;
		return f1;
	}
	
	@Override
	public long getTimeToRise(long curtime) {
		if (orbitPeriod == 0) return 0;
		
		long riseTime = (long)((float)orbitPeriod * Math.abs(0.75F - offset));
		long timeOfDay = curtime % orbitPeriod;
		if (timeOfDay > riseTime) riseTime += orbitPeriod;
		return riseTime - timeOfDay;
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		tag.setLong("period", orbitPeriod);
		tag.setInteger("angle", angle);
		tag.setFloat("offset", offset);
		tag.setInteger("size", size);
	}
	
}
