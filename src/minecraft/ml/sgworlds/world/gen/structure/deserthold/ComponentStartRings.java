package ml.sgworlds.world.gen.structure.deserthold;

import java.util.Random;

import ml.sgworlds.world.gen.StructureBuilder;
import ml.sgworlds.world.gen.structure.RingsPlatforms;
import ml.sgworlds.world.gen.structure.RingsPlatforms.PlatformStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentStartRings extends ComponentHoldStart {

	public ComponentStartRings() {}
	
	public ComponentStartRings(ChunkCoordinates position, int rotation) {
		super(position, rotation);
	}
	
	@Override
	protected boolean addComponentParts(StructureBuilder b, World world, Random rand, StructureBoundingBox chunkBox) {
		RingsPlatforms.generateRingsPlatform(world, b.getAbsX(0, 0), b.getAbsZ(0, 0), b.rotation, PlatformStyle.SandstoneInset);
		// TODO Auto-generated method stub
		return true;
	}

}
