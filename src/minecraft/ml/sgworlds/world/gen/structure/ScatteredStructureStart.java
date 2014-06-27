package ml.sgworlds.world.gen.structure;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class ScatteredStructureStart extends StructureStart {

	public ScatteredStructureStart() {}
	
	public ScatteredStructureStart(World world, Random rand, int chunkX, int chunkZ, StructureComponent structComponent) {
		super(chunkX, chunkZ);
		this.components.add(structComponent);
		structComponent.buildComponent(structComponent, components, rand);
		this.updateBoundingBox();
	}
	
	
	@Override
	public void generateStructure(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
		super.generateStructure(par1World, par2Random, par3StructureBoundingBox);
	}
}
