package ml.sgworlds.world.gen;

import ml.core.world.feature.StructureGeneratorBase;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public class CartoucheGenerator extends StructureGeneratorBase {

	@Override
	public int getFeatureVersion() {
		return 1;
	}

	@Override
	public boolean allowRetroGen() {
		return true;
	}

	@Override
	public String func_143025_a() {
		return "SGWCartouche";
	}

	@Override
	public boolean canGenerateInWorld(World world) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected StructureStart getStructureStart(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

}
