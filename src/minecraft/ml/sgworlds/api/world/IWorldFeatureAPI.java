package ml.sgworlds.api.world;


public interface IWorldFeatureAPI {

	public boolean registerFeatureProvider(WorldFeatureType type, IWorldFeatureProvider feature);

	public abstract boolean unregisterFeatureProvider(WorldFeatureType type, IWorldFeatureProvider provider);
}
