package ml.sgworlds.world.feature.impl;

import net.minecraft.nbt.NBTTagCompound;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.ILightingController;

public class LightingNormal extends WorldFeature implements ILightingController {

	public LightingNormal(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void populateBrightnessTable(float[] brightnessTable) {
		float f = 0.0F;

		for (int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float)i / 15.0F;
			brightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
