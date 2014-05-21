package ml.sgworlds.api.world;

import java.util.Collection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;


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
	 * Determine if this feature is compatible with all already generated. Only features of the {@link WorldFeatureType} are passed.
	 */
	public boolean compatibleWith(Collection<IWorldFeature> existingFeatures);
	
	/**
	 * Generate a random instance of this feature.
	 */
	public IWorldFeature generateRandomFeature();
	
	/**
	 * Create the appropriate implementor of {@link IWorldFeature} and assign it data from the passed NBTTag. 
	 */
	public IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag);
	
	/**
	 * Gets the amount of weight this feature will carry in the {@link WeightedRandom} during random generation of worlds. Average is 100.
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
	
	public static interface IWorldFeatureRender extends IWorldFeature {
		@SideOnly(Side.CLIENT)
		public void render(float partialTicks, World world, Minecraft mc);
	}
}
