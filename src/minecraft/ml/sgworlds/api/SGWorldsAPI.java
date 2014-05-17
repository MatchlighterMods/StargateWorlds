package ml.sgworlds.api;

import net.minecraft.creativetab.CreativeTabs;
import ml.sgworlds.api.world.IWorldFeatureAPI;

public abstract class SGWorldsAPI {
	protected static SGWorldsAPI sgworldsAPI;
	
	public static SGWorldsAPI getSGWorldsAPI() {
		return sgworldsAPI;
	}
	
	public abstract IWorldFeatureAPI getWorldFeatureAPI();
	
	public abstract CreativeTabs getSGWorldsCreativeTab();
}
