package ml.sgworlds.world.prefab.abydos;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import ml.sgworlds.world.gen.structure.SGWStructrueComponent;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentRoomIn extends SGWStructrueComponent {

	public ComponentRoomIn() {}
	
	public ComponentRoomIn(ChunkCoordinates position, int rotation) {
		super(position, rotation);
		setLocalBoundingBox(-4, -1, -4, 4, 4, 4);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		b.fillArea(-4, 0, -4, 4, 4, 4, null, 0);
		
		b.symmetryX = b.symmetryZ = true;
		b.setBlockAt(3, 1, 3, Block.sandStone, 2);
		b.setBlockAt(3, 2, 3, Block.sandStone, 1);
		b.setBlockAt(3, 3, 3, Block.sandStone, 2);
		
		b.setBlockAt(3, 3, 2, Block.torchWood, 0);
		b.setBlockAt(2, 3, 3, Block.torchWood, 0);
		b.symmetryX = b.symmetryZ = false;
		
		b.wallArea(-4, 0, -4, 4, 4, 4, Block.sandStone, 2);
		
		b.symmetryX = b.symmetryZ = true;
		b.setBlockAt(4, 2, 0, Block.sandStone, 1);
		b.setBlockAt(0, 2, 4, Block.sandStone, 1);
		
		b.setBlockAt(0, 0, 0, Block.stainedClay, 3);
		b.setBlockAt(1, 0, 1, Block.stainedClay, 4);
		b.setBlockAt(0, 0, 2, Block.stainedClay, 4);
		b.setBlockAt(0, 0, 3, Block.stainedClay, 4);
		b.setBlockAt(2, 0, 0, Block.stainedClay, 4);
		b.setBlockAt(3, 0, 0, Block.stainedClay, 4);
		b.symmetryX = b.symmetryZ = false;
		
		if (componentNorth) b.fillArea(-1, 1,-4, 1, 3,-4, null, 0);
		if (componentEast)  b.fillArea( 4, 1,-1, 4, 3, 1, null, 0);
		if (componentSouth) b.fillArea(-1, 1, 4, 1, 3, 4, null, 0);
		if (componentWest)  b.fillArea(-4, 1,-1,-4, 3, 1, null, 0);
		
		return true;
	}

}
