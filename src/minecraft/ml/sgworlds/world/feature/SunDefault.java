package ml.sgworlds.world.feature;

import java.util.Collection;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ml.sgworlds.api.world.IWorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;
import ml.sgworlds.api.world.feature.ISun;

public class SunDefault implements IWorldFeatureProvider {

	private static final String FEATURE_ID = "";

	@Override
	public boolean willProvideFeatureFor(String identifier) {
		return identifier == FEATURE_ID;
	}

	@Override
	public boolean compatibleWith(Collection<IWorldFeature> existingFeatures) {
		return true;
	}

	@Override
	public IWorldFeature generateRandomFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWeight() {
		return 100;
	}

	@Override
	public WorldFeatureType getFeatureType() {
		return WorldFeatureType.SUN;
	}

	private class Feature implements ISun {

		private long orbitPeriod;
		private float yawAngle;
		private float offset;
		private int size;

		@Override
		public void render(float partialTicks, World world, Minecraft mc) {
			Tessellator tess = Tessellator.instance;
			float celestialAngle = calculateCelestialAngle(world.getWorldTime(), partialTicks);
			float f4 = 1.0F - world.getRainStrength(partialTicks);
			float f7 = 0.0F;
			float f8 = 0.0F;
			float f9 = 0.0F;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
			GL11.glTranslatef(f7, f8, f9);
			GL11.glRotatef(yawAngle, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(celestialAngle * 360.0F, 1.0F, 0.0F, 0.0F);
			
			mc.getTextureManager().bindTexture(locationSunPng);
			
			tess.startDrawingQuads();
			tess.addVertexWithUV((double)(-size), 100.0D, (double)(-size), 0.0D, 0.0D);
			tess.addVertexWithUV((double)size, 100.0D, (double)(-size), 1.0D, 0.0D);
			tess.addVertexWithUV((double)size, 100.0D, (double)size, 1.0D, 1.0D);
			tess.addVertexWithUV((double)(-size), 100.0D, (double)size, 0.0D, 1.0D);
			tess.draw();

		}

		@Override
		public String getFeatureIdentifier() {
			return FEATURE_ID;
		}

		@Override
		public void writeNBTData(NBTTagCompound tag) {
			// TODO Auto-generated method stub

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

	}

}
