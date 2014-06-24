package ml.sgworlds.world.gen.structure.city;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class CityStructureStart extends StructureStart {

	public ArrayList<StructureComponent> streets = new ArrayList<StructureComponent>();
	public ArrayList<StructureComponent> buildings = new ArrayList<StructureComponent>();
	
	public CityStructureStart() {}
	
	public CityStructureStart(World world, Random rand, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
		
		// TODO Plan list of components
		
		int minX = boundingBox.minX, maxX = boundingBox.maxX, minZ = boundingBox.minZ, maxZ = boundingBox.maxZ;
		// TODO Plan walls
	}
}
