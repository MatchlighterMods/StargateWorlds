package ml.sgworlds.api.world;


public interface IFeatureAPI {

	public boolean registerFeatureProvider(FeatureProvider feature);

	public abstract boolean unregisterFeatureProvider(FeatureProvider provider);
}
