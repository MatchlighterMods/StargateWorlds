package ml.sgworlds.api.world.feature;

import ml.sgworlds.api.world.IStaticWorld;

/**
 * Stores the identifiers for features built-in to SGWorlds.
 * For use with {@link IStaticWorld} and {@link IFeatureBuilder}.
 * @author Matchlighter
 */
public enum SGWFeatures {
	
	// SUNS
	SUN_NORMAL,
	
	// MOONS
	MOON_NORMAL,
	
	// STARS
	STARS_NORMAL,
	STARS_END,
	STARS_TWINKLE,
	
	// SKY_FEATURES
	
	// BIOME CONTROLLERS
	BIOME_NATIVE,
	BIOME_SINGLE,
	BIOME_SIZED,
	
	// TERRAIN GENERATORS
	TERRAIN_NORMAL,
	
	// TERRAIN MODIFIERS
	
	// CHUNK POPULATORS
	
	// WEATHER CONTROLLERS
	
	// LIGHTING CONTROLLERS
	
	;
	
	public String toString() {
		return this.name();
	};
}
