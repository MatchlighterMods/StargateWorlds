package ml.sgworlds.api.world;

import java.util.List;

import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import net.minecraft.world.WorldProvider;

public interface IWorldData {

	public WorldProvider getWorldProvider();

	public List<WorldFeature> getFeatures(FeatureType type);

	public WorldFeature getFeature(FeatureType type);

	public abstract long getWorldSeed();
	
}
