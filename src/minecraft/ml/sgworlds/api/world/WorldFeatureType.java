package ml.sgworlds.api.world;

import java.util.Random;

import ml.sgworlds.api.world.feature.IBiomeController;
import ml.sgworlds.api.world.feature.IColorProvider;
import ml.sgworlds.api.world.feature.ILightingController;
import ml.sgworlds.api.world.feature.IMoon;
import ml.sgworlds.api.world.feature.ISkyFeature;
import ml.sgworlds.api.world.feature.ISun;
import ml.sgworlds.api.world.feature.ITerrainGenerator;
import ml.sgworlds.api.world.feature.IWeatherController;

/**
 * 
 * @author Matchlighter
 */
public enum WorldFeatureType {
	SUN(1, 4, ISun.class),
	MOON(0, 4, IMoon.class),
	SKY_FEATURE(0, 4, ISkyFeature.class),
	
	BIOME_CONTROLLER(1, IBiomeController.class),
	WEATHER_CONTROLLER(1, IWeatherController.class),
	LIGHTING_CONTROLLER(1, ILightingController.class),
	
	TERRAIN_GENERATOR(1, ITerrainGenerator.class),
	
	FOG_COLOR(1, IColorProvider.class),
	SKY_COLOR(1, IColorProvider.class),
	SUNSET_COLOR(1, IColorProvider.class),
	
	/**
	 * Registering a {@link IWorldFeatureProvider} as {@link WorldFeatureType#ALL} will throw an IllegalArgumentException.
	 */
	ALL(0, null);

	private final int minimum;
	private final int maximum;
	public final Class clazz;
	
	private WorldFeatureType(int min, int max, Class cls) {
		this.minimum = min;
		this.maximum = max;
		clazz = cls;
	}
	
	private WorldFeatureType(int count, Class cls) {
		this(count,count, cls);
	}
	
	/**
	 * Returns the number of this type of WorldFeature that should be randomly generated
	 */
	public int getRandomCount(Random rand, int registeredTypes) {
		if (minimum == maximum) return minimum;
		int rnd = rand.nextInt(maximum - minimum +1)+minimum;
		return rnd;
	}
	
}
