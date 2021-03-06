package ml.sgworlds.world.gen.structure.deserthold;

import java.util.List;

import ml.core.world.structure.MLStructureComponent.InitialStructureComponent;
import ml.core.world.structure.WeightedComponent;
import net.minecraft.util.ChunkCoordinates;


public abstract class ComponentHoldStart extends InitialStructureComponent {

	// TODO
	public List<WeightedComponent> roomWeightedComponents = DesertHoldComponents.getRoomWeights();
	public List<WeightedComponent> hallWeightedComponents = DesertHoldComponents.getHallWeights();
	
	public ComponentHoldStart() {}
	
	public ComponentHoldStart(ChunkCoordinates position, int rotation) {
		super(position, rotation, 200);
	}
	
}
