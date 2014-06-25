package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentCartouche extends StructureComponent {

	protected ChunkPosition position;
	protected int rotation;
	
	private boolean generatedChest1;
	
	public ComponentCartouche() {}
	
	public ComponentCartouche(ChunkPosition position, int rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	// Save
	@Override
	protected void func_143012_a(NBTTagCompound tag) {
		tag.setInteger("pos_x", position.x);
		tag.setInteger("pos_y", position.y);
		tag.setInteger("pos_z", position.z);
		
		tag.setInteger("rotation", rotation);
	}

	// Load
	@Override
	protected void func_143011_b(NBTTagCompound tag) {
		this.position = new ChunkPosition(tag.getInteger("pos_x"), tag.getInteger("pos_y"), tag.getInteger("pos_z"));
		this.rotation = tag.getInteger("rotation");
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		StructureBuilder b = new StructureBuilder(world, position, rotation);
		
		return true;
	}

}
