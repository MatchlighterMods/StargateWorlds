package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.core.world.structure.MLStructureComponent;
import ml.core.world.structure.StructureBuilder;
import ml.core.world.structure.WeightedComponent;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentStartAbydos extends ComponentHoldStart {

	public ComponentStartAbydos() {}
	
	public ComponentStartAbydos(ChunkCoordinates position, int rotation) {
		super(position, rotation);
		setLocalBoundingBox(-4, -1, -4, 4, 4, 4);
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List existingComponents, Random rnd) {
		InitialStructureComponent ic = (InitialStructureComponent)par1StructureComponent;
		this.componentNorth = true;
		
		for (int i=0; i<2; i++) {
			ComponentHallStraight hall = new ComponentHallStraight(getAbsOffset(0, 0, -9 - 8*i), this.rotation);
			hall.componentSouth = hall.componentNorth = true;
			existingComponents.add(hall);
			//ic.unbuiltComponents.add(hall);
		}
		
		ComponentPyramidCenter center = new ComponentPyramidCenter(getAbsOffset(0, 0, -24), rotation);
		center.componentSouth = true;
		existingComponents.add(center);
		ic.unbuiltComponents.add(center);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.fillArea(-4, 0, -4, 4, 4, 4, null, 0);
		
		b.symmetryX = b.symmetryZ = true;
		b.setBlockAt(3, 1, 3, Block.sandStone, 2);
		b.setBlockAt(3, 2, 3, Block.sandStone, 1);
		b.setBlockAt(3, 3, 3, Block.sandStone, 2);
		
		b.setBlockAt(3, 3, 2, Block.torchWood, 0);
		b.setBlockAt(2, 3, 3, Block.torchWood, 0);
		b.symmetryX = b.symmetryZ = false;
		
		b.wallArea(-4, 0, -4, 4, 4, 4, Block.sandStone, 2);
		
		b.symmetryX = b.symmetryZ = true;
		b.setBlockAt(4, 2, 0, Block.sandStone, 1);
		b.setBlockAt(0, 2, 4, Block.sandStone, 1);
		
		b.setBlockAt(0, 0, 0, Block.stainedClay, 3);
		b.setBlockAt(1, 0, 1, Block.stainedClay, 4);
		b.setBlockAt(0, 0, 2, Block.stainedClay, 4);
		b.setBlockAt(0, 0, 3, Block.stainedClay, 4);
		b.setBlockAt(2, 0, 0, Block.stainedClay, 4);
		b.setBlockAt(3, 0, 0, Block.stainedClay, 4);
		b.symmetryX = b.symmetryZ = false;
		
		if (componentNorth) b.fillArea(-1, 1,-4, 1, 3,-4, null, 0);
		if (componentEast)  b.fillArea( 4, 1,-1, 4, 3, 1, null, 0);
		if (componentSouth) b.fillArea(-1, 1, 4, 1, 3, 4, null, 0);
		if (componentWest)  b.fillArea(-4, 1,-1,-4, 3, 1, null, 0);
		
		return true;
	}

	@Override
	public MLStructureComponent getNextStructureComponent(MLStructureComponent prev, int oRotation, List<WeightedComponent> componentWeights, List<StructureComponent> existingComponents, ChunkCoordinates entrancePosition, Random rnd) {
		MLStructureComponent next = super.getNextStructureComponent(prev, oRotation, componentWeights, existingComponents, entrancePosition, rnd);
		// TODO Check if next is inside the pyramid
		return next;
	}
}
