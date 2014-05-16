package ml.sgworlds.dimension;

import net.minecraft.world.World;
import stargatetech2.api.stargate.Address;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SGWorldController {

	private Address sgAddress;
	private SGWorldData worldData;
	
	@SideOnly(Side.CLIENT)
	private SGWorldSkyRenderer skyRenderer;
	
	public SGWorldController(World world) {
		sgAddress = SGWorldsList.instance.findAddressForDim(world.provider.dimensionId);
		worldData = SGWorldsList.instance.getWorldData(sgAddress);
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return worldData.getDisplayName();
	}
	

	
}
