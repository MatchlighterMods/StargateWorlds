package ml.sgworlds.dimension;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.api.world.IFeatureAPI;
import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.FeatureType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FeatureManager implements IFeatureAPI {
	
	public static FeatureManager instance = new FeatureManager();
	
	private Multimap<FeatureType, FeatureProvider> featureProviders = HashMultimap.create();

	@Override
	public boolean registerFeatureProvider(FeatureProvider provider) {
		if (provider.type == FeatureType.ALL) throw new IllegalArgumentException("(\""+ provider.getClass().getName() +"\").getFeatureType() cannot be ALL.");
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
	
	public FeatureProvider getFeatureProvider(String identifier) {
		for (FeatureProvider p : featureProviders.values()) {
			if (p.willLoadFeatureId(identifier)) return p;
		}
		return null;
	}
	
	public List<FeatureProvider> getFeatureProviders(FeatureType type) {
		return new ArrayList<FeatureProvider>(featureProviders.get(type));
	}

}
