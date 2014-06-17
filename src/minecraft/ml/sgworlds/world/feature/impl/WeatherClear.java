package ml.sgworlds.world.feature.impl;

import net.minecraft.nbt.NBTTagCompound;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;

public class WeatherClear extends WeatherAbstract {

	public WeatherClear(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public WeatherClear(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}

	@Override
	public void startWeather() {}

}
