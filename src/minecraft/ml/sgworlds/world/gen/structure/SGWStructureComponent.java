package ml.sgworlds.world.gen.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import org.apache.commons.lang3.reflect.ConstructorUtils;

public abstract class SGWStructureComponent extends StructureComponent {

	public ChunkCoordinates position;
	public int rotation;
	public StructureBoundingBox lboundingbox; // Not setup when loading from file
	
	public boolean componentNorth, componentEast, componentSouth, componentWest;
	
	public SGWStructureComponent() {}
	
	public SGWStructureComponent(ChunkCoordinates position, int rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public void setLocalBoundingBox(StructureBoundingBox nbox) {
		lboundingbox = nbox;
		refreshBoundingBox();
	}
	
	public void refreshBoundingBox() {
		this.boundingBox = globalizeBoundingBox(lboundingbox);
	}
	
	public void setLocalBoundingBox(int nx, int ny, int nz, int px, int py, int pz) {
		setLocalBoundingBox(new StructureBoundingBox(-nx, -ny, -nz, px, py, pz));
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
	
	public ChunkCoordinates getAbsOffset(ChunkCoordinates in) {
		return StructureHelper.addCoords(position, StructureHelper.getRotatedCoords(in, rotation));
	}
	
	public ChunkCoordinates getAbsOffset(int x, int y, int z) {
		return getAbsOffset(new ChunkCoordinates(x, y, z));
	}
	
	public boolean hasComponentSide(int side) {
		side %= 4;
		switch (side) {
		case 0:
			return componentNorth;
		case 1:
			return componentEast;
		case 2:
			return componentSouth;
		case 3:
			return componentWest;
		}
		return false;
	}
	
	/**
	 * Gets the location of the door on the specified side, assuming the door is aligned with the position. 
	 */
	public ChunkCoordinates getOutPos(int dRotation) {
		int x=0,z=0;
		if (dRotation == 0) {
			z = -lboundingbox.minZ;
		} else if (rotation == 1) {
			x = lboundingbox.maxX;
		} else if (rotation == 2) {
			z = lboundingbox.maxZ;
		} else if (rotation == 3) {
			x = -lboundingbox.minX;
		}
		return StructureHelper.addCoords(position, StructureHelper.getRotatedCoords(new ChunkCoordinates(x, 0, z), rotation));
	}
	
	public static abstract class SGWInitialComponent extends SGWStructureComponent {
		
		public List<SGWStructureComponent> unbuiltComponents = new ArrayList<SGWStructureComponent>();

		public SGWInitialComponent() {}
		
		public SGWInitialComponent(ChunkCoordinates position, int rotation) {
			super(position, rotation);
		}
		
		public SGWStructureComponent getNextStructureComponent(SGWStructureComponent prev, int oRotation, List<WeightedComponent> componentWeights, List<StructureComponent> existingComponents, ChunkCoordinates entrancePosition, Random rnd) {
			WeightedComponent chosenWeighted = (WeightedComponent)WeightedRandom.getRandomItem(rnd, componentWeights);
			SGWStructureComponent nComponent = null;
			
			try {
				nComponent = ConstructorUtils.invokeConstructor(chosenWeighted.cls, entrancePosition, (rotation+oRotation) % 4);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			nComponent.rotation = (rotation + oRotation) % 4;
			nComponent.componentSouth = true; // South is entrance
			
			int centerdist = nComponent.lboundingbox.maxZ;
			nComponent.position = new ChunkCoordinates(entrancePosition.posX + StructureHelper.getRotatedX(0, -centerdist, nComponent.rotation), entrancePosition.posY, entrancePosition.posZ + StructureHelper.getRotatedZ(0, -centerdist, nComponent.rotation));
			nComponent.refreshBoundingBox();
			
			StructureComponent intersect = StructureComponent.findIntersecting(existingComponents, nComponent.boundingBox);
			if (intersect != null && intersect != prev) return null;
			
			unbuiltComponents.add(nComponent);
			existingComponents.add(nComponent);
			
			return nComponent;
		}
	}
}
