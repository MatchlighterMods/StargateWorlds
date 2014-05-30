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
	SUN(1, 4, ICelestialObject.class, RandomMode.LOW_END_WEIGHTED),
	MOON(0, 4, ICelestialObject.class, RandomMode.LOW_END_WEIGHTED) {
		@Override
		public int getRandomCount(Random rand, int registeredTypes) { // Shift the result by +1, maximum becomes 0. Makes 0 possible, but less likely
			return (super.getRandomCount(rand, registeredTypes)+1) % (maximum+1);
		}
	},
	
	STARS(1, WorldFeatureRender.class),
	SKY_FEATURE(-1, ISkyFeature.class) {
		@Override
		public int getRandomCount(Random rand, int registeredTypes) { // Weigh results toward lower numbers
			return RandomMode.LOW_END_WEIGHTED.getRandomVal(0, registeredTypes+1, rand);
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
	 * Independent features will have a 1-in-weight chance of being added, regardless of previously generated features.<br/>
	 * Use {@link WorldFeature#getSecondaryTypes(java.util.List)} to get the actual type.<br/>
	 * You can only use non-singleton feature types with independent features.
	 */
	INDEPENDENT(-1, null),
	
	/**
	 * Registering a {@link WorldFeatureProvider} as {@link WorldFeatureType#ALL} will throw an IllegalArgumentException.
	 */
	ALL(0, null);

	final int minimum;
	final int maximum;
	final RandomMode rMode;
	public final Class clazz;
	
	private FeatureType(int min, int max, Class cls, RandomMode rm) {
		this.minimum = min;
		this.maximum = max;
		this.clazz = cls;
		this.rMode = rm;
	}
	
	private FeatureType(int min, int max, Class cls) {
		this(min, max, cls, RandomMode.NORMAL);
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
		return rMode.getRandomVal(minimum, maximum+1, rand);
	}
	
	private enum RandomMode {
		LOW_END_WEIGHTED {
			@Override
			int getRandomVal(int min, int max, Random rand) {
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
		},
		NORMAL {
			@Override
			int getRandomVal(int min, int max, Random rand) {
				return rand.nextInt(max-min)+min;
			}
		};
		
		abstract int getRandomVal(int min, int max, Random rand);
	}
}
