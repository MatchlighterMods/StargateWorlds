package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentPyramidCenter extends ComponentDesertHold {

	public ComponentPyramidCenter() {}
	
	public ComponentPyramidCenter(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random rnd) {
		SGWInitialComponent ic = (SGWInitialComponent)par1StructureComponent;
		
		this.componentNorth = ic.getNextStructureComponent(this, 0, ManagerDesertHold.holdHalls, par2List, getAbsOffset(0, 0,-7), rnd) != null;
		this.componentEast = ic.getNextStructureComponent(this, 0, ManagerDesertHold.holdHalls, par2List, getAbsOffset( 7, 0, 0), rnd) != null;
		this.componentWest = ic.getNextStructureComponent(this, 0, ManagerDesertHold.holdHalls, par2List, getAbsOffset(-7, 0, 0), rnd) != null;
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		
		// Clear
		b.fillArea(-6, 0, -6, 6, 0, 6, null, 0);
		
		// Floor
		b.fillArea(-6, 0, -6, 6, 0, 6, Block.sandStone, 2);
		b.symmetryX = b.symmetryZ = true;
		
		b.setBlockAt(0, 0, 0, Block.stainedClay, 4);
		b.setBlockAt(1, 0, 1, Block.stainedClay, 4);
		b.setBlockAt(2, 0, 2, Block.stainedClay, 1);
		b.setBlockAt(3, 0, 3, Block.stainedClay,14);
		
		b.setBlockAt(3, 0, 0, Block.stainedClay, 1);
		b.setBlockAt(4, 0, 0, Block.stainedClay, 1);
		b.setBlockAt(5, 0, 0, Block.stainedClay,14);
		
		b.setBlockAt(0, 0, 3, Block.stainedClay, 1);
		b.setBlockAt(0, 0, 4, Block.stainedClay, 1);
		b.setBlockAt(0, 0, 5, Block.stainedClay,14);
		
		b.setBlockAt(3, 0, 3, Block.torchWood, 0);
		
		// Ceiling / Walls
		b.wallArea  (-6, 1, -6, 6, 4, 6, Block.sandStone, 2);
		b.borderArea(-5, 5, -5, 5, 5, 5, Block.sandStone, 2);
		b.borderArea(-4, 6, -4, 4, 6, 4, Block.sandStone, 2);
		b.borderArea(-3, 7, -3, 3, 7, 3, Block.sandStone, 2);
		b.borderArea(-2, 8, -2, 2, 8, 2, Block.sandStone, 2);
		b.borderArea(-1, 9, -1, 1, 9, 1, Block.sandStone, 2);
		
		// Corners
		b.fillArea(4, 1, 5, 4, 4, 5, Block.sandStone, 2);
		b.fillArea(5, 1, 4, 5, 4, 4, Block.sandStone, 2);
		b.setBlockAt(4, 4, 4, Block.sandStone, 2);
		b.setBlockAt(3, 4, 5, Block.sandStone, 2);
		b.setBlockAt(5, 4, 3, Block.sandStone, 2);
		
		b.setBlockAt(4, 5, 4, Block.sandStone, 2);
		b.setBlockAt(3, 5, 4, Block.sandStone, 2);
		b.setBlockAt(4, 5, 3, Block.sandStone, 2);
		
		b.setBlockAt(3, 6, 3, Block.sandStone, 2);
		
		b.setBlockAt(2, 7, 2, Block.sandStone, 2);
		
		// Passages
		for (int i=0; i<4; i++) {
			b.rotationOffset = i;
			b.fillArea(-1, 1, -6, 1, 3, -6, null, 0);
			if (!hasComponentSide(i)) {
				b.fillArea(-1, 1, -7, 1, 3, -7, Block.sandStone, 2);
			}
		}
		b.rotationOffset = 0;
		
		return true;
	}

}
