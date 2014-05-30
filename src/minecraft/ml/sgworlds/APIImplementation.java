package ml.sgworlds;

import ml.sgworlds.api.SGWorldsAPI;
import ml.sgworlds.api.world.IStaticWorld;
import ml.sgworlds.api.world.feature.IFeatureManager;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.feature.FeatureManager;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.FMLLog;

public class APIImplementation extends SGWorldsAPI {

	public void expose() {
		FMLLog.info("Exposing SGWorlds API Instance");
		sgworldsAPI = this;
	}
	
	@Override
	public CreativeTabs getSGWorldsCreativeTab() {
		return SGWorlds.sgwTab;
	}
	
	@Override
	public IFeatureManager getFeatureManager() {
		return FeatureManager.instance;
	}

	@Override
	public boolean registerStaticWorld(IStaticWorld staticWorld) {
		if (SGWorldManager.staticWorlds.contains(staticWorld)) return false;
		return SGWorldManager.staticWorlds.add(staticWorld);
	}

}
