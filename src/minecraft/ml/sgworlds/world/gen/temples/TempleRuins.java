package ml.sgworlds.world.gen.temples;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import ml.core.world.structure.StructureBuilder;
import ml.sgworlds.api.world.IGateTempleGenerator;

public class TempleRuins implements IGateTempleGenerator {

	private int[] blengs = {0,1,1,1,1,1,1,1,0}, flengs = {6,7,5,4,4,4,5,7,6};
	
	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder b = new StructureBuilder(world, gateCoords, gateRotation);
		Random rnd = new Random();
		
		for (int x=-4; x<=4; x++) {
			for (int z=-flengs[x+4]; z<=blengs[x+4]; z++) {
				b.setBlockAt(x, 0, z, Block.stoneBrick, rnd.nextInt(3));
			}
		}
		
		for (int z=2; z<=5; z+=3) {
			for (int y=1; y<=2; y+=1) {
				b.setBlockAt(3, y, z, Block.stoneBrick, rnd.nextInt(3));
				b.setBlockAt(-3, y, z, Block.stoneBrick, rnd.nextInt(3));
			}
		}
	}

	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		Random rnd = new Random(world.getSeed());
		int x = rnd.nextInt(1000)-500, z = rnd.nextInt(1000)-500;
		
		return new ChunkPosition(x, world.getHeightValue(x, z) - 1, z);
	}

}
