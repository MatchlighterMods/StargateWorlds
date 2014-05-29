package ml.sgworlds.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.IFeatureBuilder;
import ml.sgworlds.api.world.feature.WorldFeature;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.common.FMLLog;

public class FeatureBuilder implements IFeatureBuilder {
	
	private final IWorldData worldData;
	private List<WorldFeature> cFeatures = new ArrayList<WorldFeature>();
	
	public FeatureBuilder(IWorldData worldData) {
		this.worldData = worldData;
	}

	private FeatureProvider getProvider(String identifier) {
		FeatureProvider provider = FeatureManager.instance.getFeatureProvider(identifier);
		if (provider == null) throw new RuntimeException(
				String.format("Could not find a provider for the identifier \"%s\"!", identifier));
		return provider;
	}
	
	@Override
	public WorldFeature createFeatureConstructor(String identifier, Object... properties) {
		FeatureProvider provider = getProvider(identifier);
		
		WorldFeature feature = provider.constructFeature(worldData, properties);
		addFeature(feature);
		return feature;
	}

	@Override
	public WorldFeature createFeatureReflection(String identifier, Object... properties) {
		WorldFeature feature = createFeatureConstructor(identifier);
		
		for (int i=0; i<properties.length; i+=2) {
			try {
				FieldUtils.writeDeclaredField(feature, (String)properties[i], properties[i+1]);
			} catch (Exception e) {
				FMLLog.severe("Could not set field \"%s\"!", (String)properties[i]);
			}
		}
		
		return feature;
	}
	
	@Override
	public WorldFeature createFeatureRandom(String identifier, Random rnd) {
		WorldFeature feature = getProvider(identifier).generateRandom(worldData, rnd);
		addFeature(feature);
		return feature;
	}
	
	@Override
	public void addFeature(WorldFeature feature) {
		cFeatures.add(feature);
	}
	
	@Override
	public List<WorldFeature> getFeatureList() {
		return cFeatures;
	}

}
