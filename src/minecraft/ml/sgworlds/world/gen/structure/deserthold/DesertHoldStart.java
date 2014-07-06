package ml.sgworlds.world.gen.structure.deserthold;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.world.gen.structure.WeightedComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public class DesertHoldStart extends StructureStart {
	
	public DesertHoldStart(World world, Random rnd, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
	}

	public List<WeightedComponent> getComponentWeights() {
		return new ArrayList<WeightedComponent>(); // TODO
	}
}
