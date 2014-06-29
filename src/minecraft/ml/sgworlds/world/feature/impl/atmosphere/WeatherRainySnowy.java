package ml.sgworlds.world.feature.impl.atmosphere;

import net.minecraft.nbt.NBTTagCompound;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;

public class WeatherRainySnowy extends WeatherAbstract {

	public WeatherRainySnowy(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public WeatherRainySnowy(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}

	@Override
	public void startWeather() {
		this.rainStrength = 1.0F;
	}

	@Override
	public String getDescription(int maxWords) {
		return "rainy";
	}
}
