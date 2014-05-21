package ml.sgworlds.dimension;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldFeatureProvider;
import ml.sgworlds.api.world.IWorldFeatureProvider.IWorldFeature;
import ml.sgworlds.api.world.WorldFeatureType;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomItem;
import scala.actors.threadpool.Arrays;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IStargateNetwork;
import stargatetech2.api.stargate.Symbol;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Helper class for generating {@link SGWorldData}
 * @author Matchlighter
 */
public class WorldDataGenerator {
	
	public static WorldDataGenerator instance;
	
	private BiMap<IWorldFeatureProvider, WeightedRandomFeature> mapFeatureWeights(List<IWorldFeatureProvider> providers) {
		BiMap<IWorldFeatureProvider, WeightedRandomFeature> map = HashBiMap.create();
		for (IWorldFeatureProvider provider : providers) {
			map.put(provider, new WeightedRandomFeature(provider.getWeight(), provider));
		}
		return map;
	}
	
	public SGWorldData generateRandomWorld() {
		Random rand = new Random();
		Multimap<WorldFeatureType, IWorldFeature> features = HashMultimap.create();
		
		for (WorldFeatureType type : WorldFeatureType.values()) {
			if (type == WorldFeatureType.ALL) continue;
			
			BiMap<IWorldFeatureProvider, WeightedRandomFeature> featureProviders = mapFeatureWeights(FeatureManager.instance.getFeatureProviders(type));
			
			int cnt = type.getRandomCount(rand, featureProviders.size());
			while (cnt > 0 && featureProviders.size() > 0) {
				IWorldFeatureProvider provider = ((WeightedRandomFeature)WeightedRandom.getRandomItem(rand, featureProviders.values())).provider;
				
				if (!provider.compatibleWith(features.get(type))) {
					featureProviders.remove(provider);
					continue;
				}
				
				IWorldFeature feature = provider.generateRandomFeature();
				
				if (type.clazz != null && type.clazz.isAssignableFrom(feature.getClass())) {
					features.put(type, feature);
				} else {
					throw new RuntimeException(String.format("The class \"%s\" tried to provide \"%s\" as the wrong Feature Type (%s)",
							provider.getClass().getName(), feature.getClass().getName(), type.name()));
				}
				
				cnt--;
			}
		}
		
		return new SGWorldData(getRandomDesignation(), generateAddress(true), features);
	}
	
	public Address generateAddress(boolean reserveIt) {
		Random random = new Random();
		Symbol[] symbols;
		Address address;
		IStargateNetwork sgn = StargateTechAPI.api().getStargateNetwork();
		
		do{
			boolean used[] = new boolean[40];
			used[0] = true;
			symbols = new Symbol[8];
			for(int i = 0; i < symbols.length; i++){
				int s;
				do{
					s = random.nextInt(39) + 1;
				}while(used[s]);
				symbols[i] = Symbol.values()[s];
				used[s] = true;
			}
			address = Address.create(symbols);
		} while(address == null
				|| sgn.addressExists(address)
				|| (reserveIt && !sgn.reserveDimensionPrefix(SGWorldManager.instance, (Symbol[])Arrays.copyOfRange(symbols, 0, 3))));
		return address;
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
