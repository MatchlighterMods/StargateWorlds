package ml.sgworlds.world.gen.structure.deserthold;

import ml.core.world.structure.MLStructureComponent;
import net.minecraft.util.ChunkCoordinates;

public abstract class ComponentDesertHold extends MLStructureComponent {

	public ComponentDesertHold() {}
	
	public ComponentDesertHold(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
}
