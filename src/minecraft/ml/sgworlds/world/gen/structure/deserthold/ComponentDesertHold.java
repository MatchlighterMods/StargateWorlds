package ml.sgworlds.world.gen.structure.deserthold;

import ml.sgworlds.world.gen.structure.SGWStructrueComponent;
import net.minecraft.util.ChunkCoordinates;

public abstract class ComponentDesertHold extends SGWStructrueComponent {

	public ComponentDesertHold() {}
	
	public ComponentDesertHold(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
}
