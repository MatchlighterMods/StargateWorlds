package ml.sgworlds.world.gen.structure.city;

import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentPath extends CityComponent {

	public ComponentPath(CityStructureStart start, int coordMode) {
		super(start, coordMode, 0);
		
	}
	
	@Override
	public void saveData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
		// TODO Auto-generated method stub
		super.buildComponent(par1StructureComponent, par2List, par3Random);
	}
	
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		// TODO Auto-generated method stub
		return false;
	}

}
