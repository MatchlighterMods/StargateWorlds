package ml.sgworlds.api.world;

import java.util.List;
import java.util.Random;

/**
 * A simple, default implementation of {@link IWorldFeature}
 * @author Matchlighter
 */
public abstract class WorldFeature implements IWorldFeature {
	
	private final FeatureProvider provider;
	protected final IWorldData worldData;
	
	public WorldFeature(FeatureProvider provider, IWorldData worldData) {
		this.provider = provider;
		this.worldData = worldData;
	}

	@Override
	public FeatureProvider getProvider() {
		return provider;
	}
	
	@Override
	public void getSecondaryTypes(List<FeatureType> types) {}
	
	@Override
	public void randomizeProperties(Random rand) {}
}