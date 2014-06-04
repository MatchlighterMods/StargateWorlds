package ml.sgworlds.api.world;

import java.util.List;

import net.minecraft.world.WorldServer;

import ml.sgworlds.api.world.feature.IFeatureManager;
import ml.sgworlds.api.world.feature.IFeatureBuilder;
import ml.sgworlds.api.world.feature.WorldFeature;

import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IStargatePlacer;

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
	 * Skipping a required feature type will result in a) The default feature, or b) A randomly generated feature.<br/>
	 * For basic use, you can use {@link IFeatureBuilder#getFeatureList()} to get the return list.
	 * See {@link IFeatureManager#getFeatureBuilder(IWorldData)}.
	 * @param worldData
	 * @return
	 */
	public List<WorldFeature> getWorldFeatureList(IWorldData worldData);
	
	/**
	 * Optionally perform special steps for adding a Stargate to the world. Return false for default placement and generation.
	 */
	public boolean generateStargate(WorldServer world, IStargatePlacer seedingShip);
	
}
