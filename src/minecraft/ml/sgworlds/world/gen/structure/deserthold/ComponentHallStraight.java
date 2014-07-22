package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.core.world.structure.StructureBuilder;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentHallStraight extends ComponentHallBase {

	public ComponentHallStraight() {}
	
	@Override
	protected boolean addHallComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		return true;
	}

}
