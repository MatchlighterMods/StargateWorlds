package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;
import java.util.Random;

import ml.core.world.structure.MLStructureComponent;
import ml.core.world.structure.WeightedComponent;
import ml.core.world.structure.MLStructureComponent.InitialStructureComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.gen.structure.StructureComponent;


public abstract class ComponentHoldStart extends InitialStructureComponent {

	// TODO
	public List<WeightedComponent> roomWeightedComponents;
	public List<WeightedComponent> hallWeightedComponents;
	
	public ComponentHoldStart() {}
	
	public ComponentHoldStart(ChunkCoordinates position, int rotation) {
		super(position, rotation, 50);
	}
	
	@Override
	public MLStructureComponent getNextStructureComponent(MLStructureComponent prev, int oRotation, List<WeightedComponent> componentWeights, List<StructureComponent> existingComponents, ChunkCoordinates entrancePosition, Random rnd) {
		MLStructureComponent newComponent = super.getNextStructureComponent(prev, oRotation, componentWeights, existingComponents, entrancePosition, rnd);
		if (oRotation == 0 && prev instanceof ComponentHallBase && newComponent instanceof ComponentHallBase) {
			newComponent.position = newComponent.getAbsOffset(0, 0, 1);
			newComponent.refreshBoundingBox();
		}
		return newComponent;
	}
	
}
