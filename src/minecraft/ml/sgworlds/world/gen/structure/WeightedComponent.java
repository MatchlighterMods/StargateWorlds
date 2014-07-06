package ml.sgworlds.world.gen.structure;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandomItem;

public class WeightedComponent extends WeightedRandomItem {

	public Class<? extends SGWStructureComponent> cls;
	
	public WeightedComponent(Class<? extends SGWStructureComponent> cls, int weight) {
		super(weight);
	}
	
	public SGWStructureComponent constructComponent(ChunkCoordinates entrancePosition, int rotation) {
		try {
			return ConstructorUtils.invokeConstructor(cls, entrancePosition, (rotation)%4);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
