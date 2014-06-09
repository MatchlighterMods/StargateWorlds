package ml.sgworlds.api.world;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

/**
 * Implement and register this class to add it to the pool of "temples" that gates appear in in SGWorlds.
 * @author Matchlighter
 */
public interface IGateTempleGenerator {

	/**
	 * Build the temple.
	 * @param gateCoords TODO
	 * @param gateRotation The rotation of the gate.
	 */
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation);
	
	public ChunkPosition getGateCoords(World world, int gateRotation);
	
}
