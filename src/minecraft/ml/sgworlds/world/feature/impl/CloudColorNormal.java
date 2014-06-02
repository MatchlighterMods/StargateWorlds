package ml.sgworlds.world.feature.impl;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseColorProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class CloudColorNormal extends BaseColorProvider {

	private static Vec3 defaultColor = Vec3.createVectorHelper(1.0F, 1.0F, 1.0F);

	public CloudColorNormal(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData, defaultColor);
	}
	
	public CloudColorNormal(FeatureProvider provider, IWorldData worldData, int color) {
		super(provider, worldData, color);
	}
	
	public CloudColorNormal(FeatureProvider provider, IWorldData worldData, Random rand) {
		super(provider, worldData, defaultColor);
		
		if (rand.nextInt(4) == 0) {
			color = Vec3.createVectorHelper(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		} else {
			color = defaultColor;
		}
	}
	
	public CloudColorNormal(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}

}
