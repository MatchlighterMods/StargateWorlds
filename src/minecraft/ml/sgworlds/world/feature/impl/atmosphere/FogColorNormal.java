package ml.sgworlds.world.feature.impl.atmosphere;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseColorProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class FogColorNormal extends BaseColorProvider {

	private static Vec3 defaultColor = Vec3.createVectorHelper(0.7529412F, 0.84705883F, 1.0F);

	public FogColorNormal(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData, defaultColor);
	}
	
	public FogColorNormal(FeatureProvider provider, IWorldData worldData, int color) {
		super(provider, worldData, color);
	}
	
	public FogColorNormal(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}

	@Override
	public Vec3 getColor(float partialTicks) {
		float celestialAngle = worldData.getWorldProvider().calculateCelestialAngle(0, partialTicks);
		float f2 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

		if (f2 < 0.0F) {
			f2 = 0.0F;
		}

		if (f2 > 1.0F) {
			f2 = 1.0F;
		}

		float colorRed = (float)color.xCoord;
		float colorGreen = (float)color.yCoord;
		float colorBlue = (float)color.zCoord;
		colorRed *= f2 * 0.94F + 0.06F;
		colorGreen *= f2 * 0.94F + 0.06F;
		colorBlue *= f2 * 0.91F + 0.09F;
		
		return Vec3.createVectorHelper(colorRed, colorGreen, colorBlue);
	}

}
