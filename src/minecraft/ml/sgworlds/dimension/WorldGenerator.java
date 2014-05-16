package ml.sgworlds.dimension;

import java.util.Random;

/**
 * Helper class for generating {@link SGWorldData}
 * @author Matchlighter
 */
public class WorldGenerator {
	
	public static WorldGenerator instance;
	
	public SGWorldData generateRandomWorld() {
		// TODO Remember to pass it to World.setItemData() (But not here)
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
		} while (designation == null); // TODO Add check against existing worlds
		return designation;
	}
}
