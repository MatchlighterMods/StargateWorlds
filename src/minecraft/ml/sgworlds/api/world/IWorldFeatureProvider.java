package ml.sgworlds.api.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;


/**
 * It is encouraged that implementors of {@link IWorldFeatureProvider} and {@link IWorldFeature} be one-to-one, but one-to-many is possible.
 * @author Matchlighter
 */
public interface IWorldFeatureProvider {

	/**
	 * @return Whether this provider can crate a {@link IWorldFeature} for the identifier.
	 */
	public boolean willProvideFeatureFor(String identifier);
	
	/**
	 * Generate a random instance of this feature.
	 */
	public IWorldFeature generateRandomFeature();
	
	/**
	 * Create the appropriate implementor of {@link IWorldFeature} and assign it data from the passed NBTTag. 
	 */
	public IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag);
	
	/**
	 * Gets the amount of weight this feature will carry in the {@link WeightedRandom} during random generation of worlds.
	 */
	public int getWeight();
	
	public WorldFeatureType getFeatureType();
	
	public static interface IWorldFeature {
		
		/**
		 * This is used to save and load features. Make sure it is unique and do not change it.
		 */
		public String getFeatureIdentifier();
		
		/**
		 * Save this feature's properties to NBT
		 */
		public void writeNBTData(NBTTagCompound tag);
		
	}
}
