package ml.sgworlds.world.gen.structure;

import net.minecraft.util.WeightedRandomItem;

public class WeightedComponent extends WeightedRandomItem {

	public Class<? extends SGWStructureComponent> cls;
	
	public WeightedComponent(Class<? extends SGWStructureComponent> cls, int weight) {
		super(weight);
	}
	
}
