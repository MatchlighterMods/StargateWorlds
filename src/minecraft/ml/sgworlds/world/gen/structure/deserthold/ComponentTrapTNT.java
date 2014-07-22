package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentTrapTNT extends ComponentTrapBase {

	public ComponentTrapTNT() {
		setLocalBoundingBox(-4, -3, -7, 4, 4, 0);
	}
	
	@Override
	protected boolean addTrapComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.fillArea(-1, -2, -6, 1, -1, 0, Block.sandStone, 2);
		b.fillArea(0, -1, -5, 0, -1, -2, Block.tnt, 0);
		return true;
	}

}
