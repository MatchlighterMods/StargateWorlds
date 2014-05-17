package ml.sgworlds.dimension;

import java.util.Random;

import stargatetech2.transport.stargates.LoreAddresses;

/**
 * Helper class for generating {@link SGWorldData}
 * @author Matchlighter
 */
public class WorldDataGenerator {
	
	public static WorldDataGenerator instance;
	
	public SGWorldData generateRandomWorld() { // TODO
		return new SGWorldData(getRandomDesignation(), LoreAddresses.ABYDOS);
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
}
