package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class SGWStructrueComponent extends StructureComponent {

	protected ChunkPosition position;
	protected int rotation;
	
	public SGWStructrueComponent() {}
	
	public SGWStructrueComponent(ChunkPosition position, int rotation) {
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
		save(tag);
	}
	protected void save(NBTTagCompound tag) {}

	// Load
	@Override
	protected void func_143011_b(NBTTagCompound tag) {
		this.position = new ChunkPosition(tag.getInteger("pos_x"), tag.getInteger("pos_y"), tag.getInteger("pos_z"));
		this.rotation = tag.getInteger("rotation");
		load(tag);
	}
	protected void load(NBTTagCompound tag) {}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox chunkBox) {
		StructureBuilder b = new StructureBuilder(world, position, rotation);
		b.setMinMax(chunkBox);
		
		return true;
	}

	protected StructureBoundingBox createBoundingBox(int snorth, int ssouth, int seast, int swest, int sup, int sdown) {
		rotation %= 4;
		StructureBoundingBox box = new StructureBoundingBox();
		box.minY = position.y - sdown;
		box.maxY = position.y + sup;
		if (rotation == 0) {
			box.minX = position.x-swest;
			box.maxX = position.x+seast;
			box.minZ = position.z-snorth;
			box.maxZ = position.z+ssouth;
			
		} else if (rotation == 1) {
			box.minX = position.x-ssouth;
			box.maxX = position.x+snorth;
			box.minZ = position.z-swest;
			box.maxZ = position.z+seast;
			
		} else if (rotation == 2) {
			box.minX = position.x-seast;
			box.maxX = position.x+swest;
			box.minZ = position.z-ssouth;
			box.maxZ = position.z+snorth;
			
		} else if (rotation == 3) {
			box.minX = position.x-snorth;
			box.maxX = position.x+ssouth;
			box.minZ = position.z-seast;
			box.maxZ = position.z+swest;
			
		}
		return box;
	}
}
