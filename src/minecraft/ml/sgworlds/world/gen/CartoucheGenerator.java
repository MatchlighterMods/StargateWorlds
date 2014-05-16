package ml.sgworlds.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import ml.core.world.feature.IFeatureGenerator;

public class CartoucheGenerator implements IFeatureGenerator {

	@Override
	public boolean doGeneration(Random rand, int chunkX, int chunkZ,
			World world, boolean isRetroGen, int oldVersion) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSubIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFeatureVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean allowRetroGen() {
		// TODO Auto-generated method stub
		return false;
	}

}
