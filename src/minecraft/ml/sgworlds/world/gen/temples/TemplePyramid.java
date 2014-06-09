package ml.sgworlds.world.gen.temples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IGateTempleGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class TemplePyramid implements IGateTempleGenerator {

	private static final List<BiomeGenBase> validBiomes = new ArrayList<BiomeGenBase>();
	static {
		validBiomes.add(BiomeGenBase.desert);
	}
	
	@Override
	public void generateGateTemple(World world, ChunkPosition gateCoords, int gateRotation) {
		StructureBuilder th = new StructureBuilder(world, gateCoords, gateRotation);
		
		// Floor
		th.fillArea(-10, -1, -6, 10, 0, 15, Block.sandStone, 2);
		
		// Towers
		th.wallArea(6, 1, 11, 10, 8, 15, true, false,true, Block.sandStone, 2);
		th.wallArea(-10, 1, 11, -6, 8, 15, true, false,true, Block.sandStone, 2);
		
		// Pyramid
		int levels = 10;
		int gateOffset = 4;
		for (int y=levels; y>0; y--) {
			int out = levels - y + 1;
			th.borderArea(-out, y, gateOffset-out, out, y, gateOffset+out, Block.sandStone, 2);
			out -= 1;
			th.fillArea(-out, y, gateOffset-out, out, y, gateOffset+out, null, 0);
		}
		
		// Pillars
		
	}

	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		ChunkPosition biomePos = world.provider.worldChunkMgr.findBiomePosition(0, 0, 500, validBiomes, new Random(world.getSeed()));
		return new ChunkPosition(biomePos.x, world.getHeightValue(biomePos.x, biomePos.z), biomePos.z);
	}

}
