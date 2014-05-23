package ml.sgworlds.dimension;

import net.minecraft.world.World;

public class SGWorldController {

	private World worldObj;
	private SGWorldData worldData;
	
	public SGWorldController(World world) {
		this.worldObj = world;
		this.worldData = SGWorldManager.instance.getWorldData(world.provider.dimensionId);
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return worldData.getDisplayName();
	}
	
	public SGWorldData getWorldData() {
		return worldData;
	}
	

	
}
