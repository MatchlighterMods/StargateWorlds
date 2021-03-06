package ml.sgworlds.api.world.feature;

import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.types.IFeatureLocator;
import ml.sgworlds.api.world.feature.types.IPopulate;
import ml.sgworlds.api.world.feature.types.ITerrainGenerator;
import ml.sgworlds.api.world.feature.types.ITerrainModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Read through the JavaDoc of this class to see all requirements for subclasses!
 * <br/><br/>
 * Instead of overriding {@link FeatureProvider#generateRandom(IWorldData, java.util.Random)}, you can add a constructor with the signature ({@link FeatureProvider}, {@link IWorldData}, {@link Random}).<br/><br/>
 * Instead of overriding {@link FeatureProvider#loadFromNBT(IWorldData, NBTTagCompound)}, you can add a constructor with the signature ({@link FeatureProvider}, {@link IWorldData}, {@link NBTTagCompound}).<br/><br/>
 * 
 * @author Matchlighter
 */
public abstract class WorldFeature {
	
	private final FeatureProvider provider;
	protected final IWorldData worldData;
	
	public WorldFeature(FeatureProvider provider, IWorldData worldData) {
		this.provider = provider;
		this.worldData = worldData;
	}
	
	/**
	 * @return The {@link FeatureProvider} associated with this feature.
	 */
	public final FeatureProvider getProvider() {
		return provider;
	}
	
	public final FeatureType getType() {
		return getProvider().type;
	}
	
	/**
	 * Should return a String for inscribing on Stone Tablets and Cartouche walls.<br/>
	 * You should use maxLength as a bracketing key instead of trimming a longer string.<br/>
	 * For example, the maxWords=1 description for WeatherRainy could be "rainy".<br/>
	 * This will only be called for a random selection of features, not every one. 
	 * 
	 * @param maxWords The maximum number of words the description should be.
	 * @return
	 */
	public String getDescription(int maxWords) {
		return null;
	}
	
	/**
	 * Called when the {@link IWorldData} has been linked to a {@link WorldProvider}.
	 */
	public void onProviderCreated(WorldProvider wprovider) {}
	
	/**
	 * Save this feature's properties to NBT
	 */
	public abstract void writeNBTData(NBTTagCompound tag);
	
	/**
	 * Sometimes features need to implement multiple types.<br/>
	 * For instance, {@link ITerrainModifier}, {@link IPopulate}, and {@link IFeatureLocator} commonly work together.
	 * You could implement all three, register your feature as the 'primary' type, and then provide alternate types here.<br/>
	 * A secondary type that matches the primary type will be ignored.<br/><br/>
	 * 
	 * This should only be used for passive feature types (e.g. {@link IFeatureLocator}) and features that are tightly linked.<br/>
	 * Using this with singleton feature types (e.g. {@link ITerrainGenerator}) will throw an Exception!
	 */
	public void getSecondaryTypes(List<FeatureType> types) {}
	
	public static interface WorldFeatureRender {
		@SideOnly(Side.CLIENT)
		public void render(float partialTicks, World world, Minecraft mc);
	}
}