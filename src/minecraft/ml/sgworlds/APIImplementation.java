package ml.sgworlds;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.FMLLog;
import ml.sgworlds.api.SGWorldsAPI;
import ml.sgworlds.api.world.IWorldFeatureAPI;

public class APIImplementation extends SGWorldsAPI {

	public void expose() {
		FMLLog.info("Exposing SGWorlds API Instance");
		sgworldsAPI = this;
	}
	
	@Override
	public IWorldFeatureAPI getWorldFeatureAPI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreativeTabs getSGWorldsCreativeTab() {
		return SGWorlds.sgwTab;
	}

}
