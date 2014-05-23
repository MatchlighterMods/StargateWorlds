package ml.sgworlds.api.world;


public interface IWorldFeatureAPI {

	public boolean registerFeatureProvider(WorldFeatureProvider feature);

	public abstract boolean unregisterFeatureProvider(WorldFeatureProvider provider);
}
