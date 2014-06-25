package ml.sgworlds.world.feature.impl.atmosphere;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseOrbitalObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MoonDefault extends BaseOrbitalObject {

	{
		this.textureLocation = new ResourceLocation("textures/environment/moon_phases.png");
	}

	public MoonDefault(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public MoonDefault(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}
	
	public MoonDefault(FeatureProvider provider, IWorldData worldData, Random rand) {
		super(provider, worldData, rand);
	}

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
		tess.addVertexWithUV((double)(-size), 100.0D, (double)-size, (double)f16, (double)f17);
		tess.addVertexWithUV((double)size, 100.0D, (double)-size, (double)f14, (double)f17);
		tess.addVertexWithUV((double)size, 100.0D, (double)(size), (double)f14, (double)f15);
		tess.addVertexWithUV((double)(-size), 100.0D, (double)(size), (double)f16, (double)f15);
		tess.draw();
	}

	public int getPhase(long worldTime) {
		return (int)((worldTime-(orbitPeriod*offset)) / orbitPeriod) % 8;
	}
}
