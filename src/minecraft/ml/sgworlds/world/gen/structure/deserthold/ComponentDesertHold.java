package ml.sgworlds.world.gen.structure.deserthold;

import ml.sgworlds.world.gen.structure.SGWStructureComponent;
import net.minecraft.util.ChunkCoordinates;

public abstract class ComponentDesertHold extends SGWStructureComponent {

	public ComponentDesertHold() {}
	
	public ComponentDesertHold(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
}
