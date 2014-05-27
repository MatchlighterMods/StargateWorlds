package ml.sgworlds.api;

import net.minecraft.creativetab.CreativeTabs;
import ml.sgworlds.api.world.IFeatureAPI;

public abstract class SGWorldsAPI {
	protected static SGWorldsAPI sgworldsAPI;
	
	public static SGWorldsAPI getSGWorldsAPI() {
		return sgworldsAPI;
	}
	
	public abstract IFeatureAPI getWorldFeatureAPI();
	
	public abstract CreativeTabs getSGWorldsCreativeTab();
}
