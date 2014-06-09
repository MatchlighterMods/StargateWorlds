package ml.sgworlds.world.gen.temples;

import java.util.Random;

import ml.sgworlds.api.world.IGateTempleGenerator;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class TempleUnderground implements IGateTempleGenerator {

	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		// TODO Auto-generated method stub
	}

	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		Random rnd = new Random(world.getSeed());
		int x = rnd.nextInt(500), z = rnd.nextInt(500);
		
		return new ChunkPosition(x, rnd.nextInt(world.getHeightValue(x, z) - 20) + 10, z);
	}

}
