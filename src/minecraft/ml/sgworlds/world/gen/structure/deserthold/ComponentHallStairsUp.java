package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentHallStairsUp extends ComponentDesertHold {

	public ComponentHallStairsUp() {
		setLocalBoundingBox(-4, 0, -9, 4, 10, 0);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		
		// Steps
		for (int i=-2; i>=-7; i--) {
			int y = -i - 2;
			
			b.fillArea(-3, y, i, 3, y + 7, i, Block.sandStone, 2);
			b.fillArea(-1, y+2, i, 1, y + 6, i, null, 0);
			
			// Pillar
			if (i % 2 == 0) {
				b.setBlockAt(-2, 3, i, Block.sandStone, 1);
				b.setBlockAt(2, 3, i, Block.sandStone, 1);
			} else {
				b.fillArea(-2, y+2, i, 2, y + 5, i, null, 0);
			}
		}
		
		return true;
	}

	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
		// TODO Auto-generated method stub
		super.buildComponent(par1StructureComponent, par2List, par3Random);
	}
}
