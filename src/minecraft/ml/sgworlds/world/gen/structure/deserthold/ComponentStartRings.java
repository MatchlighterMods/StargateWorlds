package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentStartRings extends ComponentHoldStart {

	public ComponentStartRings() {}
	
	public ComponentStartRings(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder bldr, World world, Random rand, StructureBoundingBox chunkBox) {
		// TODO Auto-generated method stub
		return false;
	}

}
