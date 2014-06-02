package ml.sgworlds.world.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.IFeatureBuilder;
import ml.sgworlds.api.world.feature.IFeatureManager;
import ml.sgworlds.api.world.feature.WorldFeature;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FeatureManager implements IFeatureManager {
	
	public static FeatureManager instance = new FeatureManager();
	
	private Multimap<FeatureType, FeatureProvider> featureProviders = HashMultimap.create();
	private Map<FeatureType, FeatureProvider> defaultFeatures = new HashMap<FeatureType, FeatureProvider>();

	@Override
	public boolean registerFeatureProvider(FeatureProvider provider) {
		if (provider.type == FeatureType.ALL) throw new IllegalArgumentException("(\""+ provider.getClass().getName() +"\") Feature type cannot be ALL!");
		if (featureProviders.containsValue(provider)) return false;
		featureProviders.put(provider.type, provider);
		return true;
	}
	
	@Override
	public boolean unregisterFeatureProvider(FeatureProvider provider) {
		if (!featureProviders.containsValue(provider)) return false;
		featureProviders.remove(provider.type, provider);
		return true;
	}
	
	@Override
	public FeatureProvider registerFeature(String identifier, FeatureType type, Class<? extends WorldFeature> featureClass) {
		FeatureProvider nprovider = new FeatureProvider(identifier, type, featureClass);
		if (registerFeatureProvider(nprovider)) return nprovider;
		return null;
	}
	
	@Override
	public FeatureProvider registerFeature(String identifier, FeatureType type, Class<? extends WorldFeature> featureClass, int weight, boolean independent) {
		FeatureProvider nprovider = new FeatureProvider(identifier, type, featureClass, weight, independent);
		if (registerFeatureProvider(nprovider)) return nprovider;
		return null;
	}
	
	@Override
	public FeatureProvider getFeatureProvider(String identifier) {
		for (FeatureProvider p : featureProviders.values()) {
			if (p.willLoadFeatureId(identifier)) return p;
		}
		return null;
	}
	
	public FeatureProvider getDefaultFeatureProvider(FeatureType type) {
		return defaultFeatures.get(type);
	}
	
	/**
	 * Registers a {@link FeatureProvider} as being the default for its {@link FeatureType}.
	 * The registered provider must be able to create a feature with the default constructor.
	 * @param provider
	 */
	public void setDefaultFeatureProvider(FeatureProvider provider) {
		defaultFeatures.put(provider.type, provider);
	}
	
	public List<FeatureProvider> getFeatureProviders(FeatureType type) {
		return new ArrayList<FeatureProvider>(featureProviders.get(type));
	}
	
	@Override
	public IFeatureBuilder getFeatureBuilder(IWorldData worldData) {
		return new FeatureBuilder(worldData);
	}

}
