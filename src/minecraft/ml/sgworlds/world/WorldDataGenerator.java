package ml.sgworlds.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.world.feature.FeatureManager;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomItem;
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

	private static WorldFeature generateRandomTypeFeature(FeatureProvider provider, IWorldData worldData, FeatureType type) {
		WorldFeature feature = provider.generateRandom(worldData, new Random());

		if (type.clazz == null || type.clazz.isAssignableFrom(feature.getClass())) {
			return feature;
		} else {
			throw new IllegalArgumentException(String.format("The class \"%s\" tried to provide \"%s\" as the wrong feature type (%s)",
					provider.getClass().getName(), feature.getClass().getName(), type.name()));
		}
	}

	/**
	 * Generates features for the type and count specified.
	 * @param worldData The {@link SGWorldData} to associate the features with.
	 * @param type The type of the features to generate.
	 * @param count The number of features to generate.
	 * @param rand The {@link Random} to use for generation.
	 * @return A list of the generated {@link WorldFeature}s
	 */
	public static List<WorldFeature> generateRandomTypeFeatures(IWorldData worldData, FeatureType type, int count, Random rand) {

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

			genFeatures.add(generateRandomTypeFeature(provider, worldData, type));

			count--;
		}
		
		// Independent Features
		for (FeatureProvider provider : getIndependentProviders(typeProviders)) {
			if (rand.nextInt(100) < provider.getWeight() && checkCompatible(worldData, provider)) {
				genFeatures.add(generateRandomTypeFeature(provider, worldData, type));
			}
		}

		return genFeatures;
	}

	/**
	 * Generates a map of Features for the {@link SGWorldData}, but does NOT add them to the {@link SGWorldData}.
	 * @return The map of {@link WorldFeature}s
	 */
	public static Multimap<FeatureType, WorldFeature> generateRandomFeatures(IWorldData worldData) {
		Random rand = new Random();

		Multimap<FeatureType, WorldFeature> featureMap = HashMultimap.create();
		for (FeatureType type : FeatureType.values()) {
			if (type == FeatureType.ALL) continue;

			int randCount = type.getRandomCount(rand, FeatureManager.instance.getFeatureProviders(type).size()) - worldData.getFeatures(type).size();

			List<WorldFeature> features = generateRandomTypeFeatures(worldData, type, randCount, rand);
			featureMap.putAll(type, features);
		}

		return featureMap;
	}

	/**
	 * Generates an options Stargate address and optionally reserves it.
	 */
	public static Address generateAddress() {
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
				|| (sgn.prefixExists((Symbol[])Arrays.copyOfRange(symbols, 0, 3))));
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
