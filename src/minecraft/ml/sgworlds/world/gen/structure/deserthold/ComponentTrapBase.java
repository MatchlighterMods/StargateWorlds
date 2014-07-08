package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public abstract class ComponentTrapBase extends ComponentDesertHold {

	protected boolean placedChest1;
	
	public ComponentTrapBase() {}
	
	public ComponentTrapBase(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	protected void save(NBTTagCompound tag) {
		super.save(tag);
		tag.setBoolean("placedChest1", placedChest1);
	}
	
	@Override
	protected void load(NBTTagCompound tag) {
		super.load(tag);
		placedChest1 = tag.getBoolean("placedChest1");
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		
		// Main Box
		b.wallArea(-3, 0, -6, 3, 3, 0, Block.sandStone, 2);
		b.fillArea(-2, 4, -5, 2, 4, 0, Block.sandStone, 2);
		
		// Inside
		b.fillArea(-2, 1, -5, 2, 3, 0, null, 0);
		
		// Pillars
		b.symmetryX = true;
		b.setBlockAt(2, 1, 0, Block.sandStone, 2);
		b.setBlockAt(2, 1,-2, Block.sandStone, 2);
		b.setBlockAt(2, 1,-4, Block.sandStone, 2);
		b.setBlockAt(1, 1,-5, Block.sandStone, 2);
		
		b.setBlockAt(2, 2, 0, Block.sandStone, 1);
		b.setBlockAt(2, 2,-2, Block.sandStone, 1);
		b.setBlockAt(2, 2,-4, Block.sandStone, 1);
		b.setBlockAt(1, 2,-5, Block.sandStone, 1);
		b.symmetryX = false;
		
		// Torches
		b.setBlockAt( 2, 2,-1, Block.torchWood, b.getRotatedMeta(Block.torchWood, 3));
		b.setBlockAt( 2, 2,-3, Block.torchWood, b.getRotatedMeta(Block.torchWood, 3));
		b.setBlockAt(-2, 2,-1, Block.torchWood, b.getRotatedMeta(Block.torchWood, 1));
		b.setBlockAt(-2, 2,-3, Block.torchWood, b.getRotatedMeta(Block.torchWood, 1));
		
		if (!placedChest1) {
			b.setBlockAt(0, 1, -5, Block.chestTrapped, 0); // TODO Fill Chest
			placedChest1 = true;
		}
		
		return addTrapComponentParts(b, world, rand, chunkBox);
	}

	protected abstract boolean addTrapComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox);
}
