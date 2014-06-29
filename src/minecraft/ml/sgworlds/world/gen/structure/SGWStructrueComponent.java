package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public abstract class SGWStructrueComponent extends StructureComponent {

	protected ChunkCoordinates position;
	protected int rotation;
	
	public SGWStructrueComponent() {}
	
	public SGWStructrueComponent(ChunkCoordinates position, int rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	// Save
	@Override
	protected void func_143012_a(NBTTagCompound tag) {
		tag.setInteger("pos_x", position.posX);
		tag.setInteger("pos_y", position.posY);
		tag.setInteger("pos_z", position.posZ);
		
		tag.setInteger("rotation", rotation);
		save(tag);
	}
	protected void save(NBTTagCompound tag) {}

	// Load
	@Override
	protected void func_143011_b(NBTTagCompound tag) {
		this.position = new ChunkCoordinates(tag.getInteger("pos_x"), tag.getInteger("pos_y"), tag.getInteger("pos_z"));
		this.rotation = tag.getInteger("rotation");
		load(tag);
	}
	protected void load(NBTTagCompound tag) {}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox chunkBox) {
		StructureBuilder b = new StructureBuilder(world, position, rotation);
		b.setMinMax(chunkBox);
		
		return addComponentParts(b, world, random, chunkBox);
	}
	
	protected abstract boolean addComponentParts(StructureBuilder bldr, World world, Random rand, StructureBoundingBox chunkBox);

	protected StructureBoundingBox createBoundingBox(int snorth, int ssouth, int seast, int swest, int sup, int sdown) {
		rotation %= 4;
		StructureBoundingBox box = new StructureBoundingBox();
		box.minY = position.posY - sdown;
		box.maxY = position.posY + sup;
		if (rotation == 0) {
			box.minX = position.posX-swest;
			box.maxX = position.posX+seast;
			box.minZ = position.posZ-snorth;
			box.maxZ = position.posZ+ssouth;
			
		} else if (rotation == 1) {
			box.minX = position.posX-ssouth;
			box.maxX = position.posX+snorth;
			box.minZ = position.posZ-swest;
			box.maxZ = position.posZ+seast;
			
		} else if (rotation == 2) {
			box.minX = position.posX-seast;
			box.maxX = position.posX+swest;
			box.minZ = position.posZ-ssouth;
			box.maxZ = position.posZ+snorth;
			
		} else if (rotation == 3) {
			box.minX = position.posX-snorth;
			box.maxX = position.posX+ssouth;
			box.minZ = position.posZ-seast;
			box.maxZ = position.posZ+swest;
			
		}
		return box;
	}
}
