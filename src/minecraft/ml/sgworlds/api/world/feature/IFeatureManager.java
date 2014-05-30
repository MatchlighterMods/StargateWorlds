package ml.sgworlds.api.world.feature;

import ml.sgworlds.api.world.IWorldData;


public interface IFeatureManager {

	public boolean registerFeatureProvider(FeatureProvider feature);

	public boolean unregisterFeatureProvider(FeatureProvider provider);
	
	/**
	 * A shortcut for {@link #registerFeatureProvider}(new {@link FeatureProvider}(...))
	 * @param identifier
	 * @param type
	 * @param featureClass
	 * @return The new {@link FeatureProvider}, or null
	 */
	public FeatureProvider registerFeature(String identifier, FeatureType type, Class<? extends WorldFeature> featureClass);
	
	public FeatureProvider registerFeature(String identifier, FeatureType type, Class<? extends WorldFeature> featureClass, int weight, boolean independent);

	public FeatureProvider getFeatureProvider(String identifier);
	
	public IFeatureBuilder getFeatureBuilder(IWorldData worldData);
}
