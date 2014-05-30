package ml.sgworlds.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.world.feature.FeatureManager;
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

	private static BiMap<FeatureProvider, WeightedRandomFeature> mapFeatureWeights(List<FeatureProvider> providers) {
		BiMap<FeatureProvider, WeightedRandomFeature> map = HashBiMap.create();
		for (FeatureProvider provider : providers) {
			if (!provider.independent) {
				map.put(provider, new WeightedRandomFeature(provider.getWeight(), provider));
			}
		}
		return map;
	}

	private static List<FeatureProvider> getIndependentProviders(List<FeatureProvider> providers) {
		List<FeatureProvider> indeps = new ArrayList<FeatureProvider>();
		for (FeatureProvider provider : providers) {
			if (provider.independent) {
				indeps.add(provider);
			}
		}
		return indeps;
	}

	private static boolean checkCompatible(IWorldData worldDataGen, FeatureProvider provider) {
		for (WorldFeature ofeature : worldDataGen.getFeatures(provider.type)) {
			FeatureProvider oProvider = ofeature.getProvider();
			if (!provider.compatibleWith(oProvider) || !oProvider.compatibleWith(provider)) {
				return false;
			}
		}
		return true;
	}

	private static WorldFeature getRandomFeature(FeatureProvider provider, IWorldData worldData, FeatureType type) {
		WorldFeature feature = provider.generateRandom(worldData, new Random());

		if (type.clazz == null || type.clazz.isAssignableFrom(feature.getClass())) {
			return feature;
		} else {
			throw new IllegalArgumentException(String.format("The class \"%s\" tried to provide \"%s\" as the wrong feature type (%s)",
					provider.getClass().getName(), feature.getClass().getName(), type.name()));
		}
	}

	public static List<WorldFeature> generateRandomFeatureType(IWorldData worldData, FeatureType type, int count, Random rand) {

		List<WorldFeature> genFeatures = new ArrayList<WorldFeature>();
		List<FeatureProvider> typeProviders = FeatureManager.instance.getFeatureProviders(type);

		BiMap<FeatureProvider, WeightedRandomFeature> weightedProviders = mapFeatureWeights(typeProviders);

		// Dependent Features
		while (count > 0 && weightedProviders.size() > 0) {
			FeatureProvider provider = ((WeightedRandomFeature)WeightedRandom.getRandomItem(rand, weightedProviders.values())).provider;

			if (!checkCompatible(worldData, provider)) {
				weightedProviders.remove(provider);
				continue;
			}

			genFeatures.add(getRandomFeature(provider, worldData, type));

			count--;
		}
		
		// Independent Features
		for (FeatureProvider provider : getIndependentProviders(typeProviders)) {
			if (rand.nextInt(provider.getWeight()) == 0 && checkCompatible(worldData, provider)) {
				genFeatures.add(getRandomFeature(provider, worldData, type));
			}
		}

		return genFeatures;
	}

	public static Multimap<FeatureType, WorldFeature> generateRandomFeatures(IWorldData worldData) {
		Random rand = new Random();

		Multimap<FeatureType, WorldFeature> featureMap = HashMultimap.create();
		for (FeatureType type : FeatureType.values()) {
			if (type == FeatureType.ALL) continue;

			int randCount = type.getRandomCount(rand, FeatureManager.instance.getFeatureProviders(type).size()) - worldData.getFeatures(type).size();

			List<WorldFeature> features = generateRandomFeatureType(worldData, type, randCount, rand);
			featureMap.putAll(type, features);
		}

		return featureMap;
	}

	public static List<SGWorldData> generateRandomWorlds(int count) {
		List<SGWorldData> worlds = new ArrayList<SGWorldData>();
		for (int i=0; i < count; i++) {
			worlds.add(SGWorldData.generateRandom());
		}
		return worlds;
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
			sb.append((char)('A' + random.nextInt(26)));
			sb.append("-");
			sb.append(random.nextInt(9)+1);
			sb.append(random.nextInt(9)+1);
			sb.append(random.nextInt(9)+1);
			designation = sb.toString();
		} while (designation == null || SGWorldManager.instance.getWorldData(designation) != null);
		return designation;
	}

	private static class WeightedRandomFeature extends WeightedRandomItem {
		public final FeatureProvider provider;
		public WeightedRandomFeature(int par1, FeatureProvider provider) {
			super(par1);
			this.provider = provider;
		}

	}
}
