package ml.sgworlds.world.prefab;

import java.util.List;

import ml.sgworlds.api.world.IGateTempleGenerator;
import ml.sgworlds.api.world.IStaticWorld;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.IFeatureBuilder;
import ml.sgworlds.api.world.feature.SGWFeature;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.world.feature.FeatureBuilder;
import ml.sgworlds.world.gen.temples.TemplePillars;
import net.minecraft.world.WorldServer;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.Address;

public class WorldTest implements IStaticWorld {

	@Override
	public long getSeed() {
		return 0;
	}

	@Override
	public Address getAddress() {
		return StargateTechAPI.api().getStargateNetwork().parseAddress("Proclarush Taonas At");
	}

	@Override
	public String getDesignation() {
		return "TEST";
	}

	@Override
	public String getName() {
		return "TEST WORLD";
	}

	@Override
	public List<WorldFeature> getWorldFeatureList(IWorldData worldData) {
		IFeatureBuilder fbuilder = new FeatureBuilder(worldData);
		
		fbuilder.createFeatureConstructor(SGWFeature.BIOME_SIZED.name(), 9);
		fbuilder.createFeatureConstructor(SGWFeature.MOD_CAVES.name());
		fbuilder.createFeatureConstructor(SGWFeature.MOD_RAVINES.name());
		
		fbuilder.createFeatureConstructor(SGWFeature.WEATHER_RAINY.name());
		
		return fbuilder.getFeatureList();
	}

	@Override
	public IGateTempleGenerator getTempleGenerator(WorldServer world) {
		return new TemplePillars();
	}

}
