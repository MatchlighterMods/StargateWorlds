package ml.sgworlds.api;

import ml.sgworlds.api.world.feature.IFeatureAPI;
import net.minecraft.creativetab.CreativeTabs;

public abstract class SGWorldsAPI {
	protected static SGWorldsAPI sgworldsAPI;
	
	public static SGWorldsAPI getSGWorldsAPI() {
		return sgworldsAPI;
	}
	
	public abstract IFeatureAPI getFeatureAPI();
	
	public abstract CreativeTabs getSGWorldsCreativeTab();

}
