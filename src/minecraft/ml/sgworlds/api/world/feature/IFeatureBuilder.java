package ml.sgworlds.api.world.feature;

import java.util.List;
import java.util.Random;

public interface IFeatureBuilder {

	/**
	 * Looks up the identifier and tries to create a feature, with an optional list of parameters to be passed to its constructor.<br/>
	 * @param identifier
	 * @param properties A list of objects to be passed to the constructor.
	 * @return The created Feature
	 */
	public WorldFeature createFeatureConstructor(String identifier, Object... properties);
	
	/**
	 * Same as {@link #createFeatureNBT(String, Object...)}, except it uses reflection to set properties.<br/>
	 * If a specified field does not exist, it will fail-silently and continue to the next.
	 * @param identifier
	 * @param properties Similar to creating crafting recipes. Provide the NBT-name for the property and then the value. e.g. ("period", 2400L, "offset", 0, ...)
	 * @return The created Feature
	 */
	public WorldFeature createFeatureReflection(String identifier, Object... properties);
	
	/**
	 * Looks up the identifier and tries to create a random instance of that feature.<br/>
	 * @param identifier
	 * @param rnd
	 * @return +The created Feature
	 */
	public WorldFeature createFeatureRandom(String identifier, Random rnd);

	/**
	 * Adds an externally created feature to the builder's list
	 * @param feature
	 */
	public void addFeature(WorldFeature feature);
	
	/**
	 * Gets a List of all the features generated by this Builder. Similar to {@link StringBuilder#toString()}.
	 * @return A list of the features generated by this builder.
	 */
	public abstract List<WorldFeature> getFeatureList();

}
