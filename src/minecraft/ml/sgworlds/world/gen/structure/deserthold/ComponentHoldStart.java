package ml.sgworlds.world.gen.structure.deserthold;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.world.gen.structure.SGWStructureComponent;
import ml.sgworlds.world.gen.structure.WeightedComponent;
import ml.sgworlds.world.gen.structure.SGWStructureComponent.SGWInitialComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.gen.structure.StructureComponent;


public abstract class ComponentHoldStart extends SGWInitialComponent {

	public static final List<Class<? extends ComponentDesertHold>> VALID_ROOMS = new ArrayList<Class<? extends ComponentDesertHold>>();
	
	public ComponentHoldStart() {}
	
	public ComponentHoldStart(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	public SGWStructureComponent getNextStructureComponent(SGWStructureComponent prev, int oRotation, List<WeightedComponent> componentWeights, List<StructureComponent> existingComponents, ChunkCoordinates entrancePosition, Random rnd) {
		SGWStructureComponent newComponent = super.getNextStructureComponent(prev, oRotation, componentWeights, existingComponents, entrancePosition, rnd);
		if (oRotation == 0 && prev instanceof ComponentHallBase && newComponent instanceof ComponentHallBase) {
			newComponent.position = newComponent.getAbsOffset(0, 0, 1);
			newComponent.refreshBoundingBox();
		}
		return newComponent;
	}
	
}
