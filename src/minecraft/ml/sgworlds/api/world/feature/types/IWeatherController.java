package ml.sgworlds.api.world.feature.types;

import net.minecraft.world.chunk.Chunk;


public interface IWeatherController {

	public abstract float getThunderStrength();

	public abstract float getRainStrength();

	public abstract void toggleWeather();

	public abstract void clearWeather();

	public abstract void updateWeather();

	public abstract void tickLightning(Chunk chunk);

}
