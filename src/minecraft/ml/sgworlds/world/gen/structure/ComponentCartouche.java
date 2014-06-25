package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentCartouche extends SGWStructrueComponent {

	private boolean generatedChest1;
	
	public ComponentCartouche() {}
	
	public ComponentCartouche(ChunkPosition position, int rotation) {
		super(position, rotation);
		this.boundingBox = createBoundingBox(8, 5, 5, 5, 6, 0);
	}
	
	@Override
	protected void save(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.save(tag);
	}
	
	@Override
	protected void load(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.load(tag);
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox chunkBox) {
		StructureBuilder b = new StructureBuilder(world, position, rotation);
		b.setMinMax(chunkBox);
		
		b.fillArea(-3, 1,-6, 3, 5, 4, null, 0);
		
		// Floor
		b.fillArea(-3, 0, -6, 3, 0, 5, Block.sandStone, 0);
		b.fillArea(-4, 0, -5, 4, 0, 4, Block.sandStone, 0);
		
		// Entrance
		b.fillArea(-3, 1, 5, 3, 4, 5, Block.sandStone, 2);
		b.fillArea(-1, 1, 5, 1, 3, 5, null, 0);
		
		// Pillars
		b.xSymmetry = true;
		b.fillArea(5, 0, -3, 5, 3, 3, Block.sandStone, 2);
		b.fillArea(4, 4, -4, 4, 4, 4, Block.sandStone, 2);
		
		for (int z=-3; z<=3; z+=2) {
			for (int y=1; y<=3; y++) {
				b.setBlockAt(4, y, z, null, 0);
			}
			b.setBlockAt(5, 2, z, Block.sandStone, 1);
			b.setBlockAt(4, 3, z, Block.torchWood, 0);
		}
		
		for (int z=-4; z<=4; z+=2) {
			for (int y=1; y<=3; y++) {
				b.setBlockAt(4, y, z, Block.sandStone, 2);
			}
		}
		
		// Walls
		b.setBlockAt(3, 4, 4, Block.sandStone, 2);
		b.fillArea(3, 1, -5, 3, 5, -5, Block.sandStone, 2);
		b.fillArea(3, 5, -5, 3, 5, 3, Block.sandStone, 2);
		b.setBlockAt(2, 5, 3, Block.sandStone, 2);
		b.setBlockAt(2, 5,-5, Block.sandStone, 2);
		b.setBlockAt(2, 4,-6, Block.sandStone, 2);
		b.setBlockAt(2, 5,-6, Block.sandStone, 2);
		
		b.xSymmetry = false;
		
		b.fillArea(-2, 5, 4, 2, 5, 4, Block.sandStone, 2);
		b.fillArea(-1, 5,-7, 1, 5,-6, Block.sandStone, 2);
		b.fillArea(-2, 6,-6, 2, 6, 3, Block.sandStone, 2);
		
		b.xSymmetry = true;
		b.setBlockAt(2, 6, 3, null, 0);
		b.setBlockAt(2, 6,-6, null, 0);
		
		b.xSymmetry = false;
		
		// Altar
		b.fillArea(-1, 1, -2, 1, 1, -1, Block.sandStone, 1);
		
		// Engravings // TODO
		b.fillArea(-1, 1, -7, 1, 4, -7, Block.cloth, 11);
		b.fillArea(-2, 1, -6,-2, 3, -6, Block.cloth, 11);
		b.fillArea( 2, 1, -6, 2, 3, -6, Block.cloth, 11);
		
		return true;
	}

}
