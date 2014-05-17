package ml.sgworlds.dimension;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SGWorldController {

	private World worldObj;
	private SGWorldData worldData;
	
	@SideOnly(Side.CLIENT)
	private SGWorldSkyRenderer skyRenderer;
	
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
