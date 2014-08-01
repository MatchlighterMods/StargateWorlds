package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentHallPassage extends ComponentHallBase {

	public ComponentHallPassage() {
		setLocalBoundingBox(-5, -2, -4, 5, 4, 4);
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random rnd) {
		super.buildComponent(par1StructureComponent, par2List, rnd);
		
		ComponentHoldStart ic = (ComponentHoldStart)par1StructureComponent;
		componentEast = ic.getNextStructureComponent(this, 1, ic.roomWeightedComponents, par2List, getAbsOffset( 6, 0, 0), rnd) != null;
		componentWest = ic.getNextStructureComponent(this, 3, ic.roomWeightedComponents, par2List, getAbsOffset(-6, 0, 0), rnd) != null;
	}
	
	@Override
	protected boolean addHallComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		
		if (componentEast) { // +X
			buildSecretPassage(b);
			
			b.setBlockAt(4, 2, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 1));
			b.setBlockAt(3,-1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 3));
			b.setBlockAt(4, 1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 0));
		}
		
		if (componentWest) { // -X
			b.invertX = b.invertZ = true;
			buildSecretPassage(b);
			
			b.setBlockAt(4, 2, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 3));
			b.setBlockAt(3,-1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 1));
			b.setBlockAt(4, 1, 2, Block.torchRedstoneActive, b.getRotatedMeta(Block.torchRedstoneActive, 2));
			b.invertX = b.invertZ = false;
		}
		
		return true;
	}
	
	private void buildSecretPassage(StructureBuilder b) {
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
