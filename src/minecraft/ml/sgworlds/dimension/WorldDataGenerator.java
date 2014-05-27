package ml.sgworlds.dimension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.IWorldFeature;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomItem;
import scala.actors.threadpool.Arrays;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IStargateNetwork;
import stargatetech2.api.stargate.Symbol;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Helper class for generating {@link SGWorldData}
 * @author Matchlighter
 */
public class WorldDataGenerator {
	
	private static BiMap<FeatureProvider, WeightedRandomFeature> mapFeatureWeights(List<FeatureProvider> providers) {
		BiMap<FeatureProvider, WeightedRandomFeature> map = HashBiMap.create();
		for (FeatureProvider provider : providers) {
			map.put(provider, new WeightedRandomFeature(provider.getWeight(), provider));
		}
		return map;
	}
	
	private static boolean checkCompatible(SGWorldData worldDataGen, FeatureProvider provider) {
		for (IWorldFeature ofeature : worldDataGen.getFeatures(provider.type)) {
			FeatureProvider oProvider = ofeature.getProvider();
			if (!provider.compatibleWith(oProvider) || !oProvider.compatibleWith(provider)) {
				return false;
			}
		}
		return true;
	}
	
	public static List<IWorldFeature> generateRandomFeatureType(SGWorldData worldData, FeatureType type, int count, Random rand) {
		
		List<IWorldFeature> features = new ArrayList<IWorldFeature>();
		BiMap<FeatureProvider, WeightedRandomFeature> featureProviders = mapFeatureWeights(FeatureManager.instance.getFeatureProviders(type));
		
		while (count > 0 && featureProviders.size() > 0) {
			FeatureProvider provider = ((WeightedRandomFeature)WeightedRandom.getRandomItem(rand, featureProviders.values())).provider;
			
			if (!checkCompatible(worldData, provider)) {
				featureProviders.remove(provider);
				continue;
			}
			
			IWorldFeature feature = provider.constructFeature(worldData);
			feature.randomizeProperties(rand);
			
			if (type.clazz == null || type.clazz.isAssignableFrom(feature.getClass())) {
				features.add(feature);
			} else {
				throw new IllegalArgumentException(String.format("The class \"%s\" tried to provide \"%s\" as the wrong feature type (%s)",
					provider.getClass().getName(), feature.getClass().getName(), type.name()));
			}
			
			count--;
		}
		
		return features;
	}
	
	public static SGWorldData generateRandomWorldFeatures(SGWorldData worldData) {
		Random rand = new Random();
		
		for (FeatureType type : FeatureType.values()) {
			if (type == FeatureType.ALL) continue;
			
			List<IWorldFeature> features = generateRandomFeatureType(worldData, type, type.getRandomCount(rand, FeatureManager.instance.getFeatureProviders(type).size()), rand);
			for (IWorldFeature feature : features) {
				worldData.addFeature(type, feature);
			}
		}
		
		worldData.markDirty();
		return worldData;
	}
	
	public static SGWorldData generateRandomWorld() {
		return generateRandomWorldFeatures(new SGWorldData(getRandomDesignation(), generateAddress(true)));
	}
	
	public static List<SGWorldData> generateRandomWorlds(int count) {
		List<SGWorldData> worlds = new ArrayList<SGWorldData>();
		for (int i=0; i < count; i++) {
			worlds.add(generateRandomWorld());
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
