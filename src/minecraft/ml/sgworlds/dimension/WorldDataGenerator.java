package ml.sgworlds.dimension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.WorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureProvider.IWorldFeature;
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
	
	private String designation;
	private Address address;
	private Multimap<WorldFeatureType, IWorldFeature> features = HashMultimap.create();
	
	public WorldDataGenerator() {
		this(getRandomDesignation(), generateAddress(true));
	}
	
	public WorldDataGenerator(String designation, Address addr) {
		this.designation = designation;
		this.address = addr;
	}
	
	private BiMap<WorldFeatureProvider, WeightedRandomFeature> mapFeatureWeights(List<WorldFeatureProvider> providers) {
		BiMap<WorldFeatureProvider, WeightedRandomFeature> map = HashBiMap.create();
		for (WorldFeatureProvider provider : providers) {
			map.put(provider, new WeightedRandomFeature(provider.getWeight(), provider));
		}
		return map;
	}
	
	public boolean checkCompatible(WorldFeatureProvider provider) {
		for (IWorldFeature ofeature : features.values()) {
			WorldFeatureProvider oProvider = ofeature.getProvider();
			if (!provider.compatibleWith(oProvider) || !oProvider.compatibleWith(provider)) {
				return false;
			}
		}
		return true;
	}
	
	public SGWorldData generateRandomWorld() {
		Random rand = new Random();
		
		for (WorldFeatureType type : WorldFeatureType.values()) {
			if (type == WorldFeatureType.ALL) continue;
			
			BiMap<WorldFeatureProvider, WeightedRandomFeature> featureProviders = mapFeatureWeights(FeatureManager.instance.getFeatureProviders(type));
			
			int cnt = type.getRandomCount(rand, featureProviders.size());
			
			while (cnt > 0 && featureProviders.size() > 0) {
				WorldFeatureProvider provider = ((WeightedRandomFeature)WeightedRandom.getRandomItem(rand, featureProviders.values())).provider;
				
				if (!checkCompatible(provider)) {
					featureProviders.remove(provider);
					continue;
				}
				
				IWorldFeature feature = provider.generateRandomFeature(rand);
				
				if (type.clazz == null || type.clazz.isAssignableFrom(feature.getClass())) {
					features.put(type, feature);

					List<WorldFeatureType> secondaryTypes = new ArrayList<WorldFeatureType>();
					feature.getSecondaryTypes(secondaryTypes);
					for (WorldFeatureType stype : secondaryTypes) {
						if (stype == type) continue;
						if (stype.isSingleton()) {
							throw new IllegalArgumentException(String.format("The class \"%s\" tried to register \"%s\" (a singleton feature type) as a secondary feature type.",
									feature.getClass().getName(), stype.name()));
						}
						features.put(stype, feature);
					}
				} else {
					throw new IllegalArgumentException(String.format("The class \"%s\" tried to provide \"%s\" as the wrong feature type (%s)",
						provider.getClass().getName(), feature.getClass().getName(), type.name()));
				}
				
				cnt--;
			}
		}
		
		return new SGWorldData(designation, address, features);
	}
	
	public static Address generateAddress(boolean reserveIt) {
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

	public static String getRandomDesignation() {
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
	
	public static List<SGWorldData> generateRandomWorlds(int count) {
		List<SGWorldData> worlds = new ArrayList<SGWorldData>();
		for (int i=0; i < count; i++) {
			WorldDataGenerator generator = new WorldDataGenerator();
			worlds.add(generator.generateRandomWorld());
		}
		return worlds;
	}
	
	private class WeightedRandomFeature extends WeightedRandomItem {
		public final WorldFeatureProvider provider;
		public WeightedRandomFeature(int par1, WorldFeatureProvider provider) {
			super(par1);
			this.provider = provider;
		}
		
	}
}
