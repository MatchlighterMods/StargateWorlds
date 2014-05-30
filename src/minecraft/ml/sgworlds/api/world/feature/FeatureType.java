package ml.sgworlds.api.world.feature;

import java.util.Random;

import ml.sgworlds.api.world.feature.WorldFeature.WorldFeatureRender;
import ml.sgworlds.api.world.feature.types.IBiomeController;
import ml.sgworlds.api.world.feature.types.ICelestialObject;
import ml.sgworlds.api.world.feature.types.IColorProvider;
import ml.sgworlds.api.world.feature.types.IFeatureLocator;
import ml.sgworlds.api.world.feature.types.ILightingController;
import ml.sgworlds.api.world.feature.types.IPopulate;
import ml.sgworlds.api.world.feature.types.ISkyFeature;
import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import ml.sgworlds.api.world.feature.types.IWeatherController;

/**
 * Feature types may be singletons, meaning only one instance of that type can exist per world (e.g. {@link FeatureType#TERRAIN_GENERATOR}).
 * @author Matchlighter
 */
public enum FeatureType {
	SUN(1, 2, ICelestialObject.class) {
		@Override
		public int getRandomCount(Random rand, int registeredTypes) {
			return getLowEndRandom(minimum, maximum, rand);
		}
	},
	MOON(0, 4, ICelestialObject.class) {
		@Override
		public int getRandomCount(Random rand, int registeredTypes) { // Shift the result by +1, maximum becomes 0. Makes 0 possible, but less likely
			return (getLowEndRandom(minimum, maximum, rand)+1) % (maximum+1);
		}
	},
	STARS(1, WorldFeatureRender.class),
	SKY_FEATURE(-1, ISkyFeature.class) {
		@Override
		public int getRandomCount(Random rand, int registeredTypes) { // Weigh results toward lower numbers
			return getLowEndRandom(0, registeredTypes+1, rand);
		}
	},
	
	BIOME_CONTROLLER(1, IBiomeController.class),
	WEATHER_CONTROLLER(1, IWeatherController.class),
	LIGHTING_CONTROLLER(1, ILightingController.class),
	
	TERRAIN_GENERATOR(1, ITerrainGenerator.class),
	
	TERRAIN_MODIFIFIER(0, 3, ITerrainModifier.class),
	CHUNK_POPULATOR(0, 3, IPopulate.class),
	FEATURE_LOCATOR(0, IFeatureLocator.class),
	
	FOG_COLOR(1, IColorProvider.class),
	SKY_COLOR(1, IColorProvider.class),
	SUNSET_COLOR(1, IColorProvider.class),
	
	/**
	 * Registering a {@link WorldFeatureProvider} as {@link WorldFeatureType#ALL} will throw an IllegalArgumentException.
	 */
	ALL(0, null);

	final int minimum;
	final int maximum;
	public final Class clazz;
	
	private FeatureType(int min, int max, Class cls) {
		this.minimum = min;
		this.maximum = max;
		this.clazz = cls;
	}
	
	private FeatureType(int count, Class cls) {
		this(count,count, cls);
	}
	
	public boolean isSingleton() {
		return minimum == 1 && maximum == 1;
	}
	
	public int getMinimumCount() {
		return minimum;
	}
	
	public int getMaximumCount() {
		return maximum;
	}
	
	/**
	 * Returns the number of this type of WorldFeature that should be randomly generated
	 */
	public int getRandomCount(Random rand, int registeredTypes) {
		if (minimum == maximum) return minimum;
		return getNormalRandom(minimum, maximum+1, rand);
	}
	
	
	
	private static int getNormalRandom(int min, int max, Random rand) {
		return rand.nextInt(max-min)+min;
	}
	
	private static int getLowEndRandom(int min, int max, Random rand) {
		max -= min;
		int sum = 0;
		for (int i=1; i<=max; i++) {
			sum += i;
		}
		int randInt = rand.nextInt(sum+1);
		for (int i=max; i>0; i--) {
			randInt -= i;
			if (randInt <= 0) return (max - i + min);
		}
		return min;
	}
}
