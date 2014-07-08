package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentHallSP extends ComponentDesertHold {

	public boolean genPassage1=true;
	public boolean genPassage2=true;
	
	public ComponentHallSP() {}
	
	public ComponentHallSP(ChunkCoordinates position, int rotation) {
		super(position, rotation);
		setLocalBoundingBox(-4, -1, -4, 4, 4, 4);
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random rnd) {
		ComponentHoldStart strt = (ComponentHoldStart)par1StructureComponent;
		//componentNorth = strt.getNextStructureRoomPath(this, 0, getOutPos(0), rnd) != null;
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
		
		if (componentEast && genPassage1) { // +X
			buildHall(b);
			
			b.setBlockAt(4, 2, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 1));
			b.setBlockAt(3,-1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 3));
			b.setBlockAt(4, 1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 0));
		}
		
		if (componentWest && genPassage2) { // -X
			b.invertX = b.invertZ = true;
			buildHall(b);
			
			b.setBlockAt(4, 2, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 3));
			b.setBlockAt(3,-1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 1));
			b.setBlockAt(4, 1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 2));
			b.invertX = b.invertZ = false;
		}
		
		if (!componentNorth) b.fillArea(-2, 1,-4, 2, 3,-4, Block.sandStone, 2);
		if (!componentSouth) b.fillArea(-2, 1, 4, 2, 3, 4, Block.sandStone, 2);
		
		return true;
	}
	
	private void buildHall(StructureBuilder b) {
		b.fillArea(4, 0, -1, 7, 4, 1, Block.sandStone, 2);
		b.fillArea(4, 1, 0, 7, 2, 0, null, 0);
		b.setBlockAt(3, 0, 0, null, 0);
		b.setBlockAt(3, 3, 0, null, 0);
		
		b.setBlockAt(3, 0, 2, null, 2);
		
		b.setBlockAt(3,-2, 0, Block.sandStone, 2);
		b.setBlockAt(3,-2, 2, Block.sandStone, 2);
		b.setBlockAt(4,-2, 2, Block.sandStone, 2);
		
		b.setBlockAt(4, 3, 2, Block.sandStone, 2);
		b.setBlockAt(4, 4, 2, Block.redstoneWire, 0);
		
		b.setBlockAt(4, 1, 3, Block.sandStone, 2);
		b.setBlockAt(4, 2, 3, Block.redstoneWire, 0);
		
		b.setBlockAt(3,-2, 1, Block.sandStone, 2);
		b.setBlockAt(3,-1, 1, Block.redstoneWire, 0);
		
		b.setBlockAt(4,-1, 2, Block.sandStone, 2);
		b.setBlockAt(4, 0, 2, Block.redstoneWire, 0);
		
		b.setBlockAt(3,-1, 0, Block.pistonStickyBase, 1);
		b.setBlockAt(3, 4, 0, Block.pistonStickyBase, 0);
		
		b.setBlockAt(3, 4, 1, Block.redstoneWire, 0);
		b.setBlockAt(3, 4, 2, Block.redstoneWire, 0);
		
	}
}
