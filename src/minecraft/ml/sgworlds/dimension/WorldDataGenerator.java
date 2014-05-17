package ml.sgworlds.dimension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldFeatureProvider;
import ml.sgworlds.api.world.IWorldFeatureProvider.IWorldFeature;
import ml.sgworlds.api.world.WorldFeatureType;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomItem;

/**
 * Helper class for generating {@link SGWorldData}
 * @author Matchlighter
 */
public class WorldDataGenerator {
	
	public static WorldDataGenerator instance;
	
	private Collection<WeightedRandomFeature> getWeightCollection(List<IWorldFeatureProvider> providers) {
		Collection<WeightedRandomFeature> weights = new ArrayList<WorldDataGenerator.WeightedRandomFeature>();
		for (IWorldFeatureProvider provider : providers) {
			weights.add(new WeightedRandomFeature(provider.getWeight(), provider));
		}
		return weights;
	}
	
	public SGWorldData generateRandomWorld() {
		Random rand = new Random();
		List<IWorldFeature> features = new ArrayList<IWorldFeatureProvider.IWorldFeature>();
		
		for (WorldFeatureType type : WorldFeatureType.values()) {
			if (type == WorldFeatureType.ALL) continue;
			
			List<IWorldFeatureProvider> providers = FeatureManager.instance.getFeatureProviders(type);
			Collection<WeightedRandomFeature> featureWeights = getWeightCollection(providers);
			
			for (int i=0; i<type.getRandomCount(rand, providers.size()); i++) {
				IWorldFeatureProvider provider = ((WeightedRandomFeature)WeightedRandom.getRandomItem(rand, featureWeights)).provider;
				IWorldFeature feature = provider.generateRandomFeature();
				
				if (type.clazz != null && type.clazz.isAssignableFrom(feature.getClass())) {
					features.add(feature);
				} else {
					throw new RuntimeException(String.format("The class \"%s\" tried to provide \"%s\" as the wrong Feature Type (%s)",
							provider.getClass().getName(), feature.getClass().getName(), type.name()));
				}
				
			}
		}
		return new SGWorldData(getRandomDesignation(), null, features); // TODO Random Address
	}

	public String getRandomDesignation() {
		String designation;
		do {
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			sb.append("P");
			sb.append(random.nextInt(9)+1);
			sb.append(random.nextInt(26) + 'A');
			sb.append("-");
			sb.append(random.nextInt(9)+1);
			sb.append(random.nextInt(9)+1);
			sb.append(random.nextInt(9)+1);
			designation = sb.toString();
		} while (designation == null || SGWorldManager.instance.getWorldData(designation) != null);
		return designation;
	}
	
	private class WeightedRandomFeature extends WeightedRandomItem {
		public final IWorldFeatureProvider provider;
		public WeightedRandomFeature(int par1, IWorldFeatureProvider provider) {
			super(par1);
			this.provider = provider;
		}
		
	}
}
