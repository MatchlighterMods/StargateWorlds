package ml.sgworlds.world.gen.structure;

import ml.core.world.feature.StructureGeneratorBase;
import ml.sgworlds.world.feature.impl.structure.StructureCartouche;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public class OverworldStructureGen extends StructureGeneratorBase {

	@Override
	public String func_143025_a() {
		return "SGWOverworld";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int i, int j) {
		return StructureCartouche.canSpawnStructureAtCoords(worldObj, i, j, 4, 10);
	}

	@Override
	protected StructureStart getStructureStart(int i, int j) {
		return StructureCartouche.createStructureStart(worldObj, rand, i, j);
	}

	@Override
	public int getFeatureVersion() {
		return 0;
	}

	@Override
	public boolean allowRetroGen(World world, int chunkX, int chunkZ) {
		return true;
	}

	@Override
	public boolean canGenerateInWorld(World world) {
		return world.provider.dimensionId == 0;
	}

}
