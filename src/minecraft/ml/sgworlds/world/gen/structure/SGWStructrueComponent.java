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
	protected StructureBoundingBox lboundingbox;
	
	public boolean componentNorth, componentEast, componentSouth, componentWest;
	
	public SGWStructrueComponent() {}
	
	public SGWStructrueComponent(ChunkCoordinates position, int rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	protected void setLocalBoundingBox(StructureBoundingBox nbox) {
		lboundingbox = nbox;
		this.boundingBox = globalizeBoundingBox(nbox);
	}
	
	protected void setLocalBoundingBox(int nx, int ny, int nz, int px, int py, int pz) {
		StructureBoundingBox nbox = new StructureBoundingBox(-nx, -ny, -nz, px, py, pz);
		lboundingbox = nbox;
		this.boundingBox = globalizeBoundingBox(nbox);
	}
	
	// Save
	@Override
	protected void func_143012_a(NBTTagCompound tag) {
		tag.setInteger("pos_x", position.posX);
		tag.setInteger("pos_y", position.posY);
		tag.setInteger("pos_z", position.posZ);
		
		tag.setInteger("rotation", rotation);
		
		tag.setBoolean("cNorth", componentNorth);
		tag.setBoolean("cEast", componentEast);
		tag.setBoolean("cSouth", componentSouth);
		tag.setBoolean("cWest", componentWest);
		save(tag);
	}
	protected void save(NBTTagCompound tag) {}

	// Load
	@Override
	protected void func_143011_b(NBTTagCompound tag) {
		this.position = new ChunkCoordinates(tag.getInteger("pos_x"), tag.getInteger("pos_y"), tag.getInteger("pos_z"));
		this.rotation = tag.getInteger("rotation");
		
		this.componentNorth = tag.getBoolean("cNorth");
		this.componentEast = tag.getBoolean("cEast");
		this.componentSouth = tag.getBoolean("cSouth");
		this.componentWest = tag.getBoolean("cWest");
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

	public int rotatedX(int x, int z) {
		switch (rotation) {
		case 0:
			return x;
		case 1:
			return -z;
		case 2:
			return -x;
		case 3:
			return z;
		}
		return x;
	}
	
	public int rotatedZ(int x, int z) {
		switch (rotation) {
		case 0:
			return z;
		case 1:
			return x;
		case 2:
			return -z;
		case 3:
			return -x;
		}
		return z;
	}
	
	public StructureBoundingBox globalizeBoundingBox(StructureBoundingBox box) {
		int nx, px, nz, pz;
		if (rotation == 1) {
			nx = box.minZ;
			nz = box.maxX;
			px = box.maxZ;
			pz = box.minX;
		} else if (rotation == 2) {
			nx = box.maxX;
			nz = box.maxZ;
			px = box.minX;
			pz = box.minZ;
		} else if (rotation == 3) {
			nx = box.maxZ;
			nz = box.minX;
			px = box.minZ;
			pz = box.maxX;
		} else {
			nx = box.minX;
			nz = box.minZ;
			px = box.maxX;
			pz = box.maxZ;
		}
		return new StructureBoundingBox(position.posX-nx, position.posY-box.minY, position.posZ-nz, position.posX+px, position.posY+box.maxY, position.posZ+pz);
	}
}
