package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentHallStraight extends ComponentHallBase {

	public ComponentHallStraight() {}
	
	public ComponentHallStraight(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	protected boolean addHallComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		return true;
	}

}
