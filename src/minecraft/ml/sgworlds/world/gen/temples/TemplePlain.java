package ml.sgworlds.world.gen.temples;

import ml.sgworlds.api.world.IGateTempleGenerator;
import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class TemplePlain implements IGateTempleGenerator {

	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder hp = new StructureBuilder(world, gateCoords, gateRotation);
		
		hp.fillArea(-4, 1, -2, 4, 5, 8, null, 0);
		hp.fillArea(-4,-2, -2, 4, 0, 8, Block.stone, 0);
		hp.fillArea(-2, 0, 0, 2, 0, 0, null, 0);
		
	}

	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		
		int gateY = 256;
		int x = 0, z = 0;
		
		for (int tz=-2; tz<=2; tz++) {
			for (int tx=-2; tx<=2; tx++) {
				gateY = Math.min(gateY, world.getHeightValue(x+tx, z+tz));
			}
		}
		
		return new ChunkPosition(x, gateY, z);
	}

}
