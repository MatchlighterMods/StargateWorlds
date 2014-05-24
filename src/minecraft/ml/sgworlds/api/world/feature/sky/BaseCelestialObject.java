package ml.sgworlds.api.world.feature.sky;

import java.util.List;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.WorldFeatureType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public abstract class BaseCelestialObject implements ICelestialObject {

	public long orbitPeriod = 2400L;
	public int yawAngle = 0;
	public float offset = -0.25F;
	public int size = 30;
	public ResourceLocation textureLocation;

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
		
		mc.getTextureManager().bindTexture(textureLocation);
		
		tess.startDrawingQuads();
		tess.addVertexWithUV((double)(-size), 100.0D, (double)(-size), 0.0D, 0.0D);
		tess.addVertexWithUV((double)size, 100.0D, (double)(-size), 1.0D, 0.0D);
		tess.addVertexWithUV((double)size, 100.0D, (double)size, 1.0D, 1.0D);
		tess.addVertexWithUV((double)(-size), 100.0D, (double)size, 0.0D, 1.0D);
		tess.draw();

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
	public void getSecondaryTypes(List<WorldFeatureType> types) {}
	
	@Override
	public void setWorldData(IWorldData data) {}
	
}
