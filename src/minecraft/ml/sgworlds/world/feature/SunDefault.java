package ml.sgworlds.world.feature;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.FeatureType;
import ml.sgworlds.api.world.feature.prefab.BaseCelestialObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SunDefault extends FeatureProvider {
	
	private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");

	public SunDefault() {
		super("NormalSun", FeatureType.SUN, Feature.class);
		// TODO Auto-generated constructor stub
	}

	private class Feature extends BaseCelestialObject {

		public Feature(FeatureProvider provider, IWorldData worldData) {
			super(provider, worldData);
			this.textureLocation = locationSunPng;
		}
		
		@Override
		public void writeNBTData(NBTTagCompound tag) {
			// TODO Auto-generated method stub
		}

		@Override
		public FeatureProvider getProvider() {
			return SunDefault.this;
		}
	}
}
