package ml.sgworlds.api;

import ml.sgworlds.api.world.IStaticWorld;
import ml.sgworlds.api.world.feature.IFeatureManager;
import ml.sgworlds.api.world.feature.WorldFeature;

public abstract class RegisterEvent extends SGWorldsEvent {

	
	/**
	 * Raised when you should register your custom {@link WorldFeature}s.
	 * @author Matchlighter
	 */
	public static class RegisterFeatures extends RegisterEvent {
		
		public final IFeatureManager featureManager;
		
		public RegisterFeatures(IFeatureManager featureManager) {
			this.featureManager = featureManager;
		}
	}
	
	/**
	 * Raised when you should register {@link IStaticWorld}s.<br/>
	 * See {@link SGWorldsAPI#registerStaticWorld(IStaticWorld)}.
	 * @author Matchlighter
	 */
	public static class RegisterStaticWorlds extends RegisterEvent {
		
	}
}
