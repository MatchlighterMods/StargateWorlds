package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentRoomTrapped extends ComponentDesertHold {
	
	protected TrapType trapType;
	
	public ComponentRoomTrapped() {}
	
	public ComponentRoomTrapped(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	protected void save(NBTTagCompound tag) {
		super.save(tag);
		tag.setInteger("trapType", trapType.ordinal());
	}
	
	@Override
	protected void load(NBTTagCompound tag) {
		super.load(tag);
		this.trapType = TrapType.values()[tag.getInteger("trapType")];
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random rnd) {
		this.trapType = TrapType.values()[rnd.nextInt(trapType.values().length)];
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
		
		b.setBlockAt(0, 1, -5, Block.chestTrapped, 0);
		
		switch (trapType) {
		case TNT:
			b.fillArea(-1, -2, -6, 1, -1, 0, Block.sandStone, 2);
			b.fillArea(0, -1, -5, 0, -1, -2, Block.tnt, 0);
			break;
		case Arrows:
			
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
			
			// TODO Place dispensers
			
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
			break;
		case Sand:
			b.fillArea(-2, 5,-5, 2, 20, 0, Block.sand, 0);
			b.setBlockAt(0, 5,-2, Block.tnt, 0);
			
			b.setBlockAt(0, 6,-3, Block.sandStone, 0);
			b.setBlockAt(0, 6,-4, Block.sandStone, 0);
			b.setBlockAt(0, 6,-5, Block.sandStone, 0);
			
			b.setBlockAt(0, 5,-3, Block.redstoneWire, 0);
			b.setBlockAt(0, 5,-4, Block.redstoneWire, 0);
			b.setBlockAt(0, 5,-5, Block.redstoneWire, 0);
			break;
		}
		
		return true;
	}

	public static enum TrapType {
		Arrows,
		TNT,
		Sand;
	}
}
