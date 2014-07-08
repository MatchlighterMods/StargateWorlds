package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentTrapTNT extends ComponentTrapBase {

	public ComponentTrapTNT() {}
	
	public ComponentTrapTNT(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}

	@Override
	protected boolean addTrapComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.fillArea(-1, -2, -6, 1, -1, 0, Block.sandStone, 2);
		b.fillArea(0, -1, -5, 0, -1, -2, Block.tnt, 0);
		return true;
	}

}
