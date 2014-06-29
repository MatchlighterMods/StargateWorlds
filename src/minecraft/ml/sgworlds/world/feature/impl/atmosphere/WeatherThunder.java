package ml.sgworlds.world.feature.impl.atmosphere;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import net.minecraft.nbt.NBTTagCompound;

public class WeatherThunder extends WeatherAbstract {

	public WeatherThunder(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}
	
	public WeatherThunder(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData, tag);
	}

	@Override
	public void startWeather() {
		this.enableLightning = true;
	}

	@Override
	public String getDescription(int maxWords) {
		return "thundering";
	}
}
