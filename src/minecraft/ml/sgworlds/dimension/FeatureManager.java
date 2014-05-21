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
	public boolean registerFeatureProvider(IWorldFeatureProvider provider) {
		if (provider.getFeatureType() == WorldFeatureType.ALL) throw new IllegalArgumentException("(\""+ provider.getClass().getName() +"\").getFeatureType() cannot be ALL.");
		if (featureProviders.containsValue(provider)) return false;
		featureProviders.put(provider.getFeatureType(), provider);
		return true;
	}
	
	@Override
	public boolean unregisterFeatureProvider(IWorldFeatureProvider provider) {
		if (!featureProviders.containsValue(provider)) return false;
		featureProviders.remove(provider.getFeatureType(), provider);
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
