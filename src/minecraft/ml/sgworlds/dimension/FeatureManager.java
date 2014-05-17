package ml.sgworlds.dimension;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.api.world.IWorldFeatureAPI;
import ml.sgworlds.api.world.IWorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FeatureManager implements IWorldFeatureAPI {
	
	public static FeatureManager instance = new FeatureManager();
	
	private Multimap<WorldFeatureType, IWorldFeatureProvider> featureProviders = HashMultimap.create();

	@Override
	public boolean registerFeatureProvider(WorldFeatureType type, IWorldFeatureProvider provider) {
		if (featureProviders.containsValue(provider)) return false;
		featureProviders.put(type, provider);
		return true;
	}
	
	@Override
	public boolean unregisterFeatureProvider(WorldFeatureType type, IWorldFeatureProvider provider) {
		if (!featureProviders.containsValue(provider)) return false;
		featureProviders.remove(type, provider);
		return true;
	}
	
	public IWorldFeatureProvider getFeatureProvider(String identifier) {
		for (IWorldFeatureProvider p : featureProviders.values()) {
			if (p.willProvideFeatureFor(identifier)) return p;
		}
		return null;
	}
	
	public List<IWorldFeatureProvider> getFeatureProviders(WorldFeatureType type) {
		return new ArrayList<IWorldFeatureProvider>(featureProviders.get(type));
	}

}
