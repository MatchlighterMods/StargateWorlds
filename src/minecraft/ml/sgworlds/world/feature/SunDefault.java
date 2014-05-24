package ml.sgworlds.world.feature;

import java.util.Random;

import ml.sgworlds.api.world.WorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;
import ml.sgworlds.api.world.feature.sky.BaseCelestialObject;
import net.minecraft.nbt.NBTTagCompound;

public class SunDefault extends WorldFeatureProvider {

	public SunDefault() {
		super("NormalSun", WorldFeatureType.SUN);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IWorldFeature generateRandomFeature(Random rand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag) {
		// TODO Auto-generated method stub
		return null;
	}

	private class Feature extends BaseCelestialObject {

		@Override
		public void writeNBTData(NBTTagCompound tag) {
			// TODO Auto-generated method stub
		}

		@Override
		public WorldFeatureProvider getProvider() {
			return SunDefault.this;
		}
	}
}
