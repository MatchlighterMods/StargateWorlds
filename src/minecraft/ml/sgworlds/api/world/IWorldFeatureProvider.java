package ml.sgworlds.api.world;

import net.minecraft.nbt.NBTTagCompound;


/**
 * These and their subclasses will be created be Reflection. Constructors will be ignored.
 * @author Matchlighter
 */
public interface IWorldFeatureProvider {

	/**
	 * This is what is saved and used to reload {@link IWorldFeature}s
	 * @return
	 */
	public String getUniqueIdentifier();
	
	public IWorldFeature generateRandomFeature();
	
	public IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag);
	
	public static interface IWorldFeature {
		
		public NBTTagCompound writeNBTData();
		
	}
}
