package ml.sgworlds.api.world.feature;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;

import org.apache.commons.lang3.reflect.ConstructorUtils;


/**
 * @author Matchlighter
 */
public class FeatureProvider {
	
	public final String identifier;
	public final FeatureType type;
	public final Class clazz;
	public int weight = 100;
	
	public FeatureProvider(String identifier, FeatureType type, Class<? extends WorldFeature> cls) {
		this.identifier = identifier;
		this.type = type;
		this.clazz = cls;
	}
	
	public FeatureProvider(String identifier, FeatureType type, Class<? extends WorldFeature> cls, int weight) {
		this(identifier, type, cls);
		this.weight = weight;
	}
	
	protected FeatureProvider(String identifier, FeatureType type) {
		this(identifier, type, null);
	}
	
	/**
	 * Checks if this provider can create a feature for the specified identifier.<br/>
	 * This is intended for updating and converting old features.<br/><br/>
	 * <b><i>Do not even think about using this dynamically!</i></b>
	 */
	public boolean willLoadFeatureId(String ident) {
		return identifier.equals(ident);
	}
	
	/**
	 * Determine if this feature is compatible with all already generated. Only features of the {@link FeatureType} are passed.
	 */
	public boolean compatibleWith(FeatureProvider provider) {
		return true;
	}
	
	
	/**
	 * Construct a new Feature of the associated type. Override if you need a constructor with additional parameters.
	 */
	public WorldFeature constructFeature(IWorldData worldData, Object...params) {
		try {
			List<Object> fParams = new ArrayList<Object>(Arrays.asList(params));
			fParams.add(0, this);
			fParams.add(1, worldData);
			
			List<Class> paramTypes = new ArrayList<Class>();
			for (Object prm : fParams) {
				if (prm == null) paramTypes.add(null);
				else paramTypes.add(prm.getClass());
			}
			
			Constructor<WorldFeature> constructor = ConstructorUtils.getMatchingAccessibleConstructor(clazz, paramTypes.toArray(new Class[0]));
			
			if (constructor != null) {
				return constructor.newInstance(fParams.toArray());
			} else {
				throw new NoSuchMethodException(
						String.format("Could not find an appropriate constructor for Feature \"%s\"!", clazz.getName()));
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Should generate a random instance of the feature.<br/>
	 * Defaults to {@link #constructFeature(IWorldData, Object...)}.<br/>
	 * See {@link WorldFeature}.
	 * @param worldData
	 * @param rnd
	 * @return
	 */
	public WorldFeature generateRandom(IWorldData worldData, Random rnd) {
		try {
			return constructFeature(worldData, rnd);
		} catch (Exception e) {}
		return constructFeature(worldData);
	}
	
	/**
	 * Passes an {@link NBTTagCompound} to load a feature from.<br/>
	 * Defaults to {@link #constructFeature(IWorldData, Object...)}.<br/>
	 * See {@link WorldFeature}.
	 * @param worldData
	 * @param nbtData
	 * @return
	 */
	public WorldFeature loadFromNBT(IWorldData worldData, NBTTagCompound nbtData) {
		try {
			return constructFeature(worldData, nbtData);
		} catch (Exception e) {}
		return constructFeature(worldData);
	}
	
	/**
	 * Gets the amount of weight this feature will carry in the {@link WeightedRandom} during random generation of worlds. Average is 100.
	 */
	public int getWeight() {
		return weight;
	}
	
}
