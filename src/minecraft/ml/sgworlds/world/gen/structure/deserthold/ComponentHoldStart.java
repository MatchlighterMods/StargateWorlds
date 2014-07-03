package ml.sgworlds.world.gen.structure.deserthold;

import net.minecraft.util.ChunkCoordinates;


public abstract class ComponentHoldStart extends ComponentDesertHold {

	public ComponentHoldStart() {}
	
	public ComponentHoldStart(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	public ComponentDesertHold getNextStructureRoom(ComponentDesertHold prev, int oRotation, ChunkCoordinates absDoorPos) {
		int nextRot = (prev.rotation + oRotation) % 4;
		return null; // TODO
	}
}
