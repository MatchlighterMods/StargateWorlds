package ml.sgworlds.world.gen.temples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IGateTempleGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class TempleUnderwater implements IGateTempleGenerator {

	private int domeRadius = 9;
	private int floor = 4;
	private int gateOffset = 4;
	
	private static final List<BiomeGenBase> validBiomes = new ArrayList<BiomeGenBase>();
	static {
		validBiomes.add(BiomeGenBase.ocean);
	}
	
	@Override
	public void generateGateTemple(World world, ChunkPosition gpos, int gateRotation) {
		StructureBuilder th = new StructureBuilder(world, gpos, gateRotation);
		
		int radSqr = domeRadius*domeRadius;
		int radISqr = (domeRadius-1)*(domeRadius-1);
		
		for (int x=-domeRadius; x<=domeRadius; x++) {
			for (int z=-domeRadius; z<=domeRadius; z++) {
				for (int y=floor-domeRadius; y<=domeRadius; y++) {
					int dsqr = x*x + y*y + z*z; 
					int ay = y+floor;
					int az = z+gateOffset;
					
					if (dsqr > radISqr && dsqr <= radSqr) {
						int abId = th.getBlockIdAt(x, ay, az);
						if ((abId == Block.waterStill.blockID || abId == Block.waterMoving.blockID || abId == Block.glass.blockID) && ay>1) {
							th.setBlockAt(x, ay, az, Block.glass, 0);
						} else {
							th.setBlockAt(x, ay, az, Block.stoneBrick, 0);
						}
						
					} else if (ay < 1 && dsqr<radSqr) {
						th.setBlockAt(x, ay, az, Block.stoneBrick, 0);
						
					} else if (dsqr < radSqr) {
						th.setBlockAt(x, ay, az, null, 0);
					}
				}
			}
		}
		
		boolean nsr = gateRotation==0 || gateRotation==2;
		th.fillArea(-4, 0, -1, 4, 0, 1, Block.blockNetherQuartz, nsr ? 4 : 3);
		th.fillArea(-3, 0, -2, 3, 0, 2, Block.blockNetherQuartz, nsr ? 3 : 4);
		th.fillArea(-2, 0, 3, 2, 0, 11, Block.blockNetherQuartz, nsr ? 4 : 3);
		
		th.fillArea(-3, 0, -1, 3, 0, 1, Block.blockNetherQuartz, 0);
		th.fillArea(-2, 0, 2, 2, 0, 2, Block.blockNetherQuartz, 0);
		th.fillArea(-1, 0, 3, 1, 0, 11, Block.blockNetherQuartz, 0);

		setBlockSymX(th, 2, 1, -2, Block.torchWood, 5);
		setBlockSymX(th, 4, 1, 0, Block.torchWood, 5);
		setBlockSymX(th, 2, 1, 3, Block.torchWood, 5);
		setBlockSymX(th, 2, 1, 7, Block.torchWood, 5);
		setBlockSymX(th, 5, 1, 9, Block.torchWood, 5);
		setBlockSymX(th, 2, 1, 11, Block.torchWood, 5);
		
		th.fillArea(-2, 0, 0, 2, 0, 0, null, 0);
		
	}
	
	private void setBlockSymX(StructureBuilder th, int x, int y, int z, Block block, int meta) {
		th.setBlockAt(x, y, z, block, meta);
		th.setBlockAt(-x, y, z, block, meta);
	}
	
	private int getOceanFloorHeight(World world, int x, int z) {
		int seaLevel = 48;
		for (int i=seaLevel; i>8; i--) {
			int blid = world.getBlockId(x, i, z);
			if (!(Block.blocksList[blid] instanceof BlockFluid)) {
				return i;
			}
		}
		return 8;
	}

	@Override
	public ChunkPosition getGateCoords(World world, int gateRotation) {
		ChunkPosition biomePos = world.provider.worldChunkMgr.findBiomePosition(0, 0, 500, validBiomes, new Random(world.getSeed()));
		if (biomePos == null) return null;
		return new ChunkPosition(biomePos.x, getOceanFloorHeight(world, biomePos.x, biomePos.z), biomePos.z);
	}

}
