package ml.sgworlds.api;

import ml.sgworlds.api.world.IStaticWorld;
import ml.sgworlds.api.world.feature.IFeatureManager;
import net.minecraft.creativetab.CreativeTabs;

public abstract class SGWorldsAPI {
	protected static SGWorldsAPI sgworldsAPI;
	
	public static SGWorldsAPI getSGWorldsAPI() {
		return sgworldsAPI;
	}
	
	public abstract IFeatureManager getFeatureManager();
	
	public abstract CreativeTabs getSGWorldsCreativeTab();
	
	/**
	 * Registers a static world that will be added to the list of worlds at generation.
	 */
	public abstract boolean registerStaticWorld(IStaticWorld staticWorld);

}
