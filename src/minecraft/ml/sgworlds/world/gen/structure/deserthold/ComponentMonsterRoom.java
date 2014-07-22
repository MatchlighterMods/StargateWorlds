package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.DungeonHooks;

public class ComponentMonsterRoom extends ComponentDesertHold {

	public ComponentMonsterRoom() {
		setLocalBoundingBox(-2, -1, -2, 2, 4, 2);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		
		b.fillArea(-2,-1,-2, 2, 4, 2, Block.sandStone, 2);
		b.fillArea(-1, 1,-1, 1, 3, 2, null, 0);
		
		b.setBlockAt( 2, 2, 0, Block.sandStone, 1);
		b.setBlockAt(-2, 2, 0, Block.sandStone, 1);
		b.setBlockAt( 0, 2,-2, Block.sandStone, 1);
		
		for (int x=-1; x<=1; x++) {
			for (int z=-1; z<=1; z++) {
				if (rand.nextInt(4) > 0) { 
					b.setBlockAt(x, 0, z, Block.mobSpawner, 0);
					TileEntityMobSpawner tems = (TileEntityMobSpawner) b.getTileEntityAt(x, 0, z);
					tems.getSpawnerLogic().setMobID(DungeonHooks.getRandomDungeonMob(rand));
				}
			}
		}
		
		return true;
	}

}
