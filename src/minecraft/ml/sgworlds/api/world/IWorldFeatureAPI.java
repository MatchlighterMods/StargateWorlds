package ml.sgworlds.api.world;


public interface IWorldFeatureAPI {

	public boolean registerFeatureProvider(IWorldFeatureProvider feature);

	public abstract boolean unregisterFeatureProvider(IWorldFeatureProvider provider);
}
