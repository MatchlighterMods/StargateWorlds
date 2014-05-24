package ml.sgworlds.api.world;

import java.util.Random;

import ml.sgworlds.api.world.feature.IColorProvider;
import ml.sgworlds.api.world.feature.ILightingController;
import ml.sgworlds.api.world.feature.IWeatherController;
import ml.sgworlds.api.world.feature.earth.IBiomeController;
import ml.sgworlds.api.world.feature.earth.IFeatureLocator;
import ml.sgworlds.api.world.feature.earth.IPopulate;
import ml.sgworlds.api.world.feature.earth.ITerrainGenerator;
import ml.sgworlds.api.world.feature.earth.ITerrainModifier;
import ml.sgworlds.api.world.feature.sky.ICelestialObject;
import ml.sgworlds.api.world.feature.sky.ISkyFeature;

/**
 * 
 * @author Matchlighter
 */
public enum WorldFeatureType {
	SUN(1, 4, ICelestialObject.class),
	MOON(0, 4, ICelestialObject.class),
	STARS(1, ICelestialObject.class),
	SKY_FEATURE(-1, ISkyFeature.class) {
		@Override
		public int getRandomCount(Random rand, int registeredTypes) { // Weigh results toward lower numbers
			registeredTypes += 1;
			int sum = 0;
			for (int i=1; i<=registeredTypes; i++) {
				sum += i;
			}
			int randInt = rand.nextInt(sum+1);
			for (int i=registeredTypes; i>0; i--) {
				randInt -= i;
				if (randInt <= 0) return registeredTypes - i;
				
			}
			return 0;
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
	
	public boolean isSingleton() {
		return minimum == 1 && maximum == 1;
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
