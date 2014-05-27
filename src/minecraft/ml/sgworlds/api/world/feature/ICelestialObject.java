package ml.sgworlds.api.world.feature;

import ml.sgworlds.api.world.IWorldFeature;

public interface ICelestialObject extends IWorldFeature.IWorldFeatureRender {

	/**
	 * @param worldTime
	 * @param partialTickTime
	 * @return the 0.0F - 1.0F value that represents the position of the sun in its orbit. 0.0 is noon.
	 */
	public float calculateCelestialAngle(long worldTime, float partialTickTime);
	
}
