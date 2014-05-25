package ml.sgworlds.world.feature;

import ml.sgworlds.api.world.WorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;
import ml.sgworlds.api.world.feature.sky.BaseCelestialObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class MoonDefault extends WorldFeatureProvider {

	public MoonDefault() {
		super("NormalMoon", WorldFeatureType.MOON, Feature.class);
		// TODO Auto-generated constructor stub
	}

	private class Feature extends BaseCelestialObject {

		@Override
		protected void doRender(float partialTicks, World world, Minecraft mc) {
			Tessellator tess = Tessellator.instance;
			int phase = getPhase(world.getWorldTime());
			int l = phase % 4;
			int i1 = phase / 4 % 2;
			float f14 = (float)(l + 0) / 4.0F;
			float f15 = (float)(i1 + 0) / 2.0F;
			float f16 = (float)(l + 1) / 4.0F;
			float f17 = (float)(i1 + 1) / 2.0F;
			
			tess.startDrawingQuads();
			tess.addVertexWithUV((double)(-size), -100.0D, (double)size, (double)f16, (double)f17);
			tess.addVertexWithUV((double)size, -100.0D, (double)size, (double)f14, (double)f17);
			tess.addVertexWithUV((double)size, -100.0D, (double)(-size), (double)f14, (double)f15);
			tess.addVertexWithUV((double)(-size), -100.0D, (double)(-size), (double)f16, (double)f15);
			tess.draw();
		}
		
		public int getPhase(long worldTime) {
			return (int)(worldTime / orbitPeriod) % 8;
		}

		@Override
		public WorldFeatureProvider getProvider() {
			return MoonDefault.this;
		}
	}
}
