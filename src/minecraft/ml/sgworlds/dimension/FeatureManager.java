package ml.sgworlds.dimension;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.api.world.IWorldFeatureAPI;
import ml.sgworlds.api.world.WorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FeatureManager implements IWorldFeatureAPI {
	
	public static FeatureManager instance = new FeatureManager();
	
	private Multimap<WorldFeatureType, WorldFeatureProvider> featureProviders = HashMultimap.create();

	@Override
	public boolean registerFeatureProvider(WorldFeatureProvider provider) {
		if (provider.type == WorldFeatureType.ALL) throw new IllegalArgumentException("(\""+ provider.getClass().getName() +"\").getFeatureType() cannot be ALL.");
		if (featureProviders.containsValue(provider)) return false;
		featureProviders.put(provider.type, provider);
		return true;
	}
	
	@Override
	public boolean unregisterFeatureProvider(WorldFeatureProvider provider) {
		if (!featureProviders.containsValue(provider)) return false;
		featureProviders.remove(provider.type, provider);
		return true;
	}
	
	public WorldFeatureProvider getFeatureProvider(String identifier) {
		for (WorldFeatureProvider p : featureProviders.values()) {
			if (p.identifier.equals(identifier)) return p;
		}
		return null;
	}
	
	public List<WorldFeatureProvider> getFeatureProviders(WorldFeatureType type) {
		return new ArrayList<WorldFeatureProvider>(featureProviders.get(type));
	}

}
