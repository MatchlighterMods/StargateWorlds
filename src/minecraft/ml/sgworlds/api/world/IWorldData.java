package ml.sgworlds.api.world;

import java.util.List;

import net.minecraft.world.WorldProvider;

public interface IWorldData {

	public WorldProvider getWorldProvider();

	public List<IWorldFeature> getFeatures(FeatureType type);

	public IWorldFeature getFeature(FeatureType type);
	
}
