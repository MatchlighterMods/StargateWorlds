package ml.sgworlds.world.feature;

import ml.sgworlds.api.world.WorldFeatureProvider;
import ml.sgworlds.api.world.WorldFeatureType;
import ml.sgworlds.api.world.feature.sky.BaseCelestialObject;
import net.minecraft.nbt.NBTTagCompound;

public class SunDefault extends WorldFeatureProvider {

	public SunDefault() {
		super("NormalSun", WorldFeatureType.SUN, Feature.class);
		// TODO Auto-generated constructor stub
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
