package ml.sgworlds.api.world.feature;

import ml.sgworlds.api.world.IStaticWorld;

/**
 * Stores the identifiers for features built-in to SGWorlds.
 * For use with {@link IStaticWorld} and {@link IFeatureBuilder}.
 * @author Matchlighter
 */
public enum SGWFeature {
	
	// SUNS
	SUN_NORMAL,
	
	// MOONS
	MOON_NORMAL,
	
	// STARS
	@Default STARS_NORMAL,
	STARS_END,
	STARS_TWINKLE,
	
	// SKY_FEATURES
	
	// BIOME CONTROLLERS
	@Default BIOME_NATIVE,
	BIOME_SINGLE,
	BIOME_SIZED,
	
	// TERRAIN GENERATORS
	@Default TERRAIN_NORMAL,
	
	// TERRAIN MODIFIERS
	MOD_RAVINES,
	MOD_CAVES,
	
	// CHUNK POPULATORS
	POPULATE_ORE_NAQUADAH,
	POPULATE_DUNGEONS,
	POPULATE_STRONGHOLDS,
	POPULATE_VILLAGES,
	POPULATE_LAKES_WATER,
	POPULATE_LAKES_LAVA,
	
	// WEATHER CONTROLLERS
	@Default WEATHER_NORMAL,
	WEATHER_THUNDER,
	WEATHER_STORMY,
	WEATHER_RAINY,
	WEATHER_CLEAR,
	
	// LIGHTING CONTROLLERS
	@Default LIGHTING_NORMAL,
	
	// FOG COLORS
	@Default FOG_COLOR_NORMAL,
	
	// CLOUD COLORS
	@Default CLOUD_COLOR_NORMAL,
	
	// SKY COLORS
	@Default SKY_COLOR_NORMAL
	
	;
	
	public String toString() {
		return this.name();
	};
	
	public @interface Default {}
}
