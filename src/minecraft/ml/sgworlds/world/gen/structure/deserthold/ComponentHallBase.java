package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public abstract class ComponentHallBase extends ComponentDesertHold {

	public ComponentHallBase() {}
	
	public ComponentHallBase(ChunkCoordinates position, int rotation) {
		super(position, rotation);
		setLocalBoundingBox(-3, -1, -4, 3, 4, 4);
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
		ComponentHoldStart ic = (ComponentHoldStart)par1StructureComponent;
		componentNorth = ic.getNextStructureComponent(this, 0, ic.hallWeightedComponents, par2List, getAbsOffset(0, 0, -5), par3Random) != null;
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		// Floor
		b.fillArea(-3, 0, -4, 3, 0, 4, Block.sandStone, 2);
		
		// Roof
		b.fillArea(-2, 4, -4, 2, 4, 4, Block.sandStone, 2);
		
		// Walls
		b.symmetryX = true;
		b.fillArea(3, 1, -4, 3, 3, 4, Block.sandStone, 2);
		b.fillArea(2, 3, -4, 2, 3, 4, Block.sandStone, 2);
		
		for (int z=-3; z<=3; z+=2) {
			b.setBlockAt(2, 1, z, Block.sandStone, 2);
			b.setBlockAt(2, 2, z, Block.sandStone, 1);
		}
		b.symmetryX = false;
		
		// Torches
		b.setBlockAt( 2, 2, 2, Block.torchWood, b.getRotatedMeta(Block.torchWood, 3));
		b.setBlockAt( 2, 2,-2, Block.torchWood, b.getRotatedMeta(Block.torchWood, 3));
		b.setBlockAt(-2, 2, 2, Block.torchWood, b.getRotatedMeta(Block.torchWood, 1));
		b.setBlockAt(-2, 2,-2, Block.torchWood, b.getRotatedMeta(Block.torchWood, 1));
		
		if (!componentNorth) b.fillArea(-2, 1,-4, 2, 3,-4, Block.sandStone, 2);
		if (!componentSouth) b.fillArea(-2, 1, 4, 2, 3, 4, Block.sandStone, 2);
		
		return addHallComponentParts(b, world, rand, chunkBox);
	}

	protected abstract boolean addHallComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox);
}
