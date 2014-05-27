package ml.sgworlds.api.world;

import java.lang.reflect.Constructor;

import net.minecraft.util.WeightedRandom;


/**
 * @author Matchlighter
 */
public class FeatureProvider {
	
	public final String identifier;
	public final FeatureType type;
	public final Class clazz;
	public int weight = 100;
	
	public FeatureProvider(String identifier, FeatureType type, Class<? extends IWorldFeature> cls) {
		this.identifier = identifier;
		this.type = type;
		this.clazz = cls;
	}
	
	public FeatureProvider(String identifier, FeatureType type, Class<? extends IWorldFeature> cls, int weight) {
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
	public IWorldFeature constructFeature(IWorldData worldData) {
		if (clazz == null) throw new IllegalStateException(
				String.format("FeatureProvider for \"%s\" does not have a feature class associated with it! Did you mean to override constructFeature()?", identifier));
		try {
			Constructor<IWorldFeature> constructor = clazz.getConstructor(FeatureProvider.class, IWorldData.class);
			IWorldFeature nfeat = constructor.newInstance(this, worldData);
			return nfeat;
			
		} catch (NoSuchMethodError e) {
			throw new IllegalStateException(
					String.format("Could not find an appropriate constructor for Feature \"%s\"! Did you mean to override constructFeature()?", clazz.getName()), e);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the amount of weight this feature will carry in the {@link WeightedRandom} during random generation of worlds. Average is 100.
	 */
	public int getWeight() {
		return weight;
	}
	
}
