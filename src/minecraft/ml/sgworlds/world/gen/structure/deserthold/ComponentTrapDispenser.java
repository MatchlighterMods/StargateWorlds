package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentTrapDispenser extends ComponentTrapBase {

	public ComponentTrapDispenser() {}
	
	public ComponentTrapDispenser(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	protected boolean addTrapComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.setBlockAt(0,-2,-5, Block.sandStone, 2);
		b.setBlockAt(0,-2,-6, Block.sandStone, 2);
		b.setBlockAt(0,-2,-7, Block.sandStone, 2);
		b.setBlockAt(0,-1,-7, Block.sandStone, 2);
		b.setBlockAt(0, 1,-7, Block.sandStone, 2);
		b.setBlockAt(0, 3,-7, Block.sandStone, 2);
		
		b.setBlockAt(0,-1,-5, Block.redstoneWire, 0);
		b.setBlockAt(0,-1,-6, Block.redstoneRepeaterIdle, b.getRotatedMeta(Block.redstoneRepeaterIdle, 0));
		
		b.setBlockAt(0, 0,-7, Block.torchRedstoneActive, 0);
		b.setBlockAt(0, 2,-7, Block.torchRedstoneIdle, 0);
		b.setBlockAt(0, 4,-6, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 2));
		
		b.setBlockAt( 3, 3,-4, Block.torchRedstoneIdle, b.getRotatedMeta(Block.torchRedstoneIdle, 1));
		b.setBlockAt(-3, 3,-4, Block.torchRedstoneIdle, b.getRotatedMeta(Block.torchRedstoneIdle, 3));
		
		// TODO Place/fill dispensers
		
		b.symmetryX = true;
		// Pistons
		b.setBlockAt(3, 4,-1, Block.pistonStickyBase, 0);
		b.setBlockAt(3, 4,-3, Block.pistonStickyBase, 0);
		b.setBlockAt(3, 3,-1, null, 0);
		b.setBlockAt(3, 3,-3, null, 0);
		b.setBlockAt(3, 5,-2, Block.redstoneWire, 0);
		
		// Dispensers
		b.setBlockAt(4, 2,-2, Block.sandStone, 2);
		b.setBlockAt(4, 2,-4, Block.sandStone, 2);
		
		b.setBlockAt(4, 3,-2, Block.redstoneWire, 0);
		b.setBlockAt(4, 3,-3, Block.redstoneWire, 0);
		b.setBlockAt(4, 3,-4, Block.redstoneWire, 0);
		
		// Top
		b.setBlockAt(2, 4,-2, Block.redstoneWire, 0);
		b.setBlockAt(2, 4,-3, Block.redstoneWire, 0);
		b.setBlockAt(2, 4,-4, Block.redstoneWire, 0);
		b.setBlockAt(2, 4,-5, Block.redstoneWire, 0);
		b.setBlockAt(1, 4,-5, Block.redstoneWire, 0);
		b.setBlockAt(1, 3,-6, Block.redstoneWire, 0);
		
		b.symmetryX = false;
		return true;
	}

}
