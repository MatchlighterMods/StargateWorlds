package ml.sgworlds.api.world;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.feature.earth.IFeatureLocator;
import ml.sgworlds.api.world.feature.earth.IPopulate;
import ml.sgworlds.api.world.feature.earth.ITerrainGenerator;
import ml.sgworlds.api.world.feature.earth.ITerrainModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * It is encouraged that implementors of {@link WorldFeatureProvider} and {@link IWorldFeature} be one-to-one.
 * @author Matchlighter
 */
public abstract class WorldFeatureProvider {
	
	public final String identifier;
	public final WorldFeatureType type;
	
	public WorldFeatureProvider(String identifier, WorldFeatureType type) {
		this.identifier = identifier;
		this.type = type;
	}
	
	/**
	 * Determine if this feature is compatible with all already generated. Only features of the {@link WorldFeatureType} are passed.
	 */
	public boolean compatibleWith(WorldFeatureProvider provider) {
		return true;
	}
	
	/**
	 * Generate a random instance of this feature.
	 * @param rand TODO
	 */
	public abstract IWorldFeature generateRandomFeature(Random rand);
	
	/**
	 * Create the appropriate implementor of {@link IWorldFeature} and assign it data from the passed NBTTag. 
	 * @param featureTag The NBTTag with the data
	 */
	public abstract IWorldFeature loadFeatureFromNBT(NBTTagCompound featureTag);
	
	/**
	 * Gets the amount of weight this feature will carry in the {@link WeightedRandom} during random generation of worlds. Average is 100.
	 */
	public int getWeight() {
		return 100;
	}
	
	/**
	 * A simple, default implementation of {@link IWorldFeature}
	 * @author Matchlighter
	 */
	public abstract class WorldFeature implements IWorldFeature {
		
		protected IWorldData worldData;
		
		@Override
		public WorldFeatureProvider getProvider() {
			return WorldFeatureProvider.this;
		}
		
		@Override
		public void getSecondaryTypes(List<WorldFeatureType> types) {}
		
		@Override
		public void setWorldData(IWorldData data) {
			this.worldData = data;
		}
		
	}
	
	public static interface IWorldFeature {
		
		/**
		 * @return The {@link WorldFeatureProvider} associated with this feature.
		 */
		public WorldFeatureProvider getProvider();
		
		/**
		 * Save this feature's properties to NBT
		 */
		public void writeNBTData(NBTTagCompound tag);
		
		/**
		 * Sometimes features need to implement multiple types.<br/>
		 * For instance, {@link ITerrainModifier}, {@link IPopulate}, and {@link IFeatureLocator} commonly work together.
		 * You could implement all three, register your feature as the 'primary' type, and then provide alternate types here.<br/><br/>
		 * 
		 * This should only be used for passive feature types (e.g. {@link IFeatureLocator}) and features that are tightly linked.<br/>
		 * DO NOT use this with singleton feature types (e.g. {@link ITerrainGenerator})!
		 */
		public void getSecondaryTypes(List<WorldFeatureType> types);
		
		/**
		 * Passes the WorldData object once it has been created with this feature.
		 * @param data
		 */
		public void setWorldData(IWorldData data);
		
	}
	
	public static interface IWorldFeatureRender extends IWorldFeature {
		@SideOnly(Side.CLIENT)
		public void render(float partialTicks, World world, Minecraft mc);
	}
	
}
