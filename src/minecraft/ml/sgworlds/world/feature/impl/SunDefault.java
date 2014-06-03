package ml.sgworlds.world.feature.impl;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseCelestialObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SunDefault extends BaseCelestialObject {

	private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");

	public SunDefault(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
		this.textureLocation = locationSunPng;
	}
	
	public SunDefault(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
		this.textureLocation = locationSunPng;
	}
	
	public SunDefault(FeatureProvider provider, IWorldData worldData, Random rand) {
		super(provider, worldData, rand);
		this.textureLocation = locationSunPng;
	}
}
