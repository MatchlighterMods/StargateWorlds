package ml.sgworlds.world.prefab.abydos;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import ml.sgworlds.world.gen.structure.SGWStructrueComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentHallSP extends SGWStructrueComponent {

	public ComponentHallSP() {}
	
	public ComponentHallSP(ChunkCoordinates position, int rotation) {
		super(position, rotation);
		this.boundingBox = createBoundingBox(4, 4, 4, 4, 4, 1);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		
		return false;
	}
	
}
