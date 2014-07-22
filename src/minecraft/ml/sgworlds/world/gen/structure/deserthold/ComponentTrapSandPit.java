package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentTrapSandPit extends ComponentTrapBase {

	public ComponentTrapSandPit() {
		setLocalBoundingBox(-4, -2, -8, 4, 20, 0);
	}
	
	@Override
	protected boolean addTrapComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.fillArea(-2, 5,-5, 2, 20, 0, Block.sand, 0);
		b.setBlockAt(0, 5,-2, Block.tnt, 0);
		
		b.setBlockAt(0, 6,-3, Block.sandStone, 0);
		b.setBlockAt(0, 6,-4, Block.sandStone, 0);
		b.setBlockAt(0, 6,-5, Block.sandStone, 0);
		
		b.setBlockAt(0, 5,-3, Block.redstoneWire, 0);
		b.setBlockAt(0, 5,-4, Block.redstoneWire, 0);
		b.setBlockAt(0, 5,-5, Block.redstoneWire, 0);
		// TODO Finish redstone
		return true;
	}

}
