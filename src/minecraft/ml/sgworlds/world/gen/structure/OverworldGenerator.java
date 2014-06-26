package ml.sgworlds.world.gen.structure;

import ml.core.world.feature.StructureGeneratorBase;
import ml.sgworlds.world.feature.impl.structure.StructureCartouche;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public class OverworldGenerator extends StructureGeneratorBase {

	@Override
	public String func_143025_a() {
		return "SGWOverworld";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int i, int j) {
		return StructureHelper.shouldGenerateAt(worldObj, i, j, 4, 10) &&
				StructureCartouche.validBiomes.contains(worldObj.getWorldChunkManager().getBiomeGenAt(i * 16 + 8, j * 16 + 8));
	}

	@Override
	protected StructureStart getStructureStart(int i, int j) {
		return new ScatteredStructureStart(worldObj, rand, i, j, new ComponentCartouche(new ChunkCoordinates(i*16+8, 40, j*16+8), rand.nextInt(4)));
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
