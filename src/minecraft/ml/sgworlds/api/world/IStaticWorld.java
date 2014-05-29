package ml.sgworlds.api.world;

import java.util.List;

import ml.sgworlds.api.world.feature.IFeatureAPI;
import ml.sgworlds.api.world.feature.IFeatureBuilder;
import ml.sgworlds.api.world.feature.WorldFeature;

import stargatetech2.api.stargate.Address;

public interface IStaticWorld {

	/**
	 * Gets the world seed for this world.
	 * @return The seed for this static world
	 */
	public long getSeed();
	
	public Address getAddress();
	
	/**
	 * Gets the designation of this world. e.g. P8X-873
	 */
	public String getDesignation();
	
	/**
	 * Gets the formal name of this world. e.g. Abydos
	 */
	public String getName();
	
	/**
	 * Gets a list of features for this static world.<br/>
	 * For basic use, you can use {@link IFeatureBuilder#getFeatureList()} to get the return list.
	 * See {@link IFeatureAPI#getFeatureBuilder(IWorldData)}.
	 * @param worldData
	 * @return
	 */
	public List<WorldFeature> getWorldFeatureList(IWorldData worldData);
}
