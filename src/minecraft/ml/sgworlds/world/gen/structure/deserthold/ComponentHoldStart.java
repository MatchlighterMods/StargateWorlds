package ml.sgworlds.world.gen.structure.deserthold;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ChunkCoordinates;


public abstract class ComponentHoldStart extends ComponentDesertHold {

	public static final List<Class<? extends ComponentDesertHold>> VALID_ROOMS = new ArrayList<Class<? extends ComponentDesertHold>>();
	
	public ComponentHoldStart() {}
	
	public ComponentHoldStart(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
}
