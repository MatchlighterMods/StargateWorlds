package ml.sgworlds.world.feature.impl;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseOrbitalObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SunDefault extends BaseOrbitalObject {

	{
		this.textureLocation = new ResourceLocation("textures/environment/sun.png");
		hasHorizon = true;
	}

	public SunDefault(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public SunDefault(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}
	
	public SunDefault(FeatureProvider provider, IWorldData worldData, Random rand) {
		super(provider, worldData, rand);
	}
}
