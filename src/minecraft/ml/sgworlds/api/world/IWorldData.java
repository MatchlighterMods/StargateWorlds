package ml.sgworlds.api.world;

import java.util.List;

import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import net.minecraft.world.WorldProvider;

public interface IWorldData {

	public WorldProvider getWorldProvider();

	public List<WorldFeature> getFeatures(FeatureType type);

	/**
	 * Returns the first found feature of the specified type, or null.
	 * Mainly for use with singleton types.
	 */
	public WorldFeature getFeature(FeatureType type);

	public abstract long getWorldSeed();

	/**
	 * Checks if the world has a feature with the specified identifier
	 */
	public abstract boolean hasFeatureIdentifier(String identifier);
	
}
