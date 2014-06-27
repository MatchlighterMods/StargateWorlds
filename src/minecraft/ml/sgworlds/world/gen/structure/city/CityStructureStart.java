package ml.sgworlds.world.gen.structure.city;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public class CityStructureStart extends StructureStart {

	// Minimum and maximum sizes. Based on # of buildings.
	public int minSize = 10, maxSize = 20;
	
	public ArrayList<CityComponent> streets = new ArrayList<CityComponent>();
	public ArrayList<CityComponent> buildings = new ArrayList<CityComponent>();
	
	public CityStructureStart() {}
	
	public CityStructureStart(World world, Random rand, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
		
		// TODO Plan list of components
		CityComponent startingComponent = new ComponentPath(this, -1);
		
		while (streets.size() > 0 || buildings.size() > 0) {
			CityComponent cmp = streets.size() > 0 ? streets.remove(rand.nextInt(streets.size())) : buildings.remove(rand.nextInt(buildings.size()));
			cmp.buildComponent(startingComponent, this.components, rand);
		}
		
		this.updateBoundingBox();
		
		int minX = boundingBox.minX, maxX = boundingBox.maxX, minZ = boundingBox.minZ, maxZ = boundingBox.maxZ;
		// TODO Plan walls
		
		this.updateBoundingBox();
	}
}
