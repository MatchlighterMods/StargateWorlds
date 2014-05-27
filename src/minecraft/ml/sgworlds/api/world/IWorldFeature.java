package ml.sgworlds.api.world;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ml.sgworlds.api.world.feature.IFeatureLocator;
import ml.sgworlds.api.world.feature.IPopulate;
import ml.sgworlds.api.world.feature.ITerrainGenerator;
import ml.sgworlds.api.world.feature.ITerrainModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IWorldFeature {
	
	/**
	 * @return The {@link FeatureProvider} associated with this feature.
	 */
	public FeatureProvider getProvider();
	
	/**
	 * Save this feature's properties to NBT
	 */
	public void writeNBTData(NBTTagCompound tag);
	
	/**
	 * Loads feature properties from NBT
	 */
	public void readNBTData(NBTTagCompound tag);
	
	/**
	 * Sometimes features need to implement multiple types.<br/>
	 * For instance, {@link ITerrainModifier}, {@link IPopulate}, and {@link IFeatureLocator} commonly work together.
	 * You could implement all three, register your feature as the 'primary' type, and then provide alternate types here.<br/><br/>
	 * 
	 * This should only be used for passive feature types (e.g. {@link IFeatureLocator}) and features that are tightly linked.<br/>
	 * DO NOT use this with singleton feature types (e.g. {@link ITerrainGenerator})!
	 */
	public void getSecondaryTypes(List<FeatureType> types);
	
	/**
	 * Called to generate random properties for the feature. Generally only called when worlds are being generated.
	 */
	public void randomizeProperties(Random rand);
	
	public static interface IWorldFeatureRender extends IWorldFeature {
		@SideOnly(Side.CLIENT)
		public void render(float partialTicks, World world, Minecraft mc);
	}
}