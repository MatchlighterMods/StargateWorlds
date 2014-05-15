package ml.sgworlds.dimension;

import net.minecraft.world.World;
import stargatetech2.api.stargate.Address;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SGWorldController {

	private Address sgAddress;
	private String name;
	private SGWorldData worldData;
	
	@SideOnly(Side.CLIENT)
	private SGWorldSkyRenderer skyRenderer;
	
	public SGWorldController(World world) {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}
	

	
}
