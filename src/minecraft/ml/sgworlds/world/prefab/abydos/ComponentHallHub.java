package ml.sgworlds.world.prefab.abydos;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import ml.sgworlds.world.gen.StructureBuilder;
import ml.sgworlds.world.gen.structure.SGWStructrueComponent;


public class ComponentHallHub extends SGWStructrueComponent {

	public ComponentHallHub() {}
	
	public ComponentHallHub(ChunkCoordinates position, int rotation) {
		super(position, rotation);
		this.boundingBox = createBoundingBox(3, 3, 3, 3, 4, 1);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.fillArea(-3, 0, -3, 3, 3, 3, null, 0);
		
		b.wallArea(-3, 0, -3, 3, 4, 3, Block.sandStone, 2);
		b.borderArea(-2, 3, -2, 2, 3, 2, Block.sandStone, 2);
		
		b.symmetryX = b.symmetryZ = true;
		b.setBlockAt(1, 1, 2, Block.sandStone, 2);
		b.setBlockAt(1, 2, 2, Block.sandStone, 1);
		b.setBlockAt(2, 2, 3, Block.sandStone, 1);
		
		b.setBlockAt(2, 1, 1, Block.sandStone, 2);
		b.setBlockAt(2, 2, 1, Block.sandStone, 1);
		b.setBlockAt(3, 2, 2, Block.sandStone, 1);
		b.symmetryX = b.symmetryZ = false;
		
		if (componentNorth) b.fillArea(-1, 1, 2, 1, 3, 3, null, 0);
		if (componentSouth) b.fillArea(-1, 1,-3, 1, 3,-2, null, 0);
		if (componentEast) b.fillArea( 2, 1,-1, 3, 3, 1, null, 0);
		if (componentWest) b.fillArea(-3, 1,-1,-2, 3, 1, null, 0);
		
		return true;
	}

}
