package ml.sgworlds.world.gen.structure;

import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IStructureProvider;
import ml.sgworlds.world.dimension.SGWorldProvider;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenStructureExternal extends MapGenStructure {

	protected IStructureProvider.StructureStrata genStrata;
	
	public MapGenStructureExternal(IStructureProvider.StructureStrata genStrata) {
		this.genStrata = genStrata;
	}
	
	@Override
	public String func_143025_a() {
		return "SGWStructure" + genStrata.toString();
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int i, int j) {
		SGWorldProvider pvdr = (SGWorldProvider)worldObj.provider;
		for (WorldFeature ft : pvdr.getWorldData().getFeatures(FeatureType.STRUCTURE_PROVIDER)) {
			IStructureProvider isp = (IStructureProvider)ft;
			if (isp.getStrata() == genStrata && isp.willProvideStructureFor(worldObj, i, j)) return true;
		}
		return false;
	}

	@Override
	protected StructureStart getStructureStart(int i, int j) {
		SGWorldProvider pvdr = (SGWorldProvider)worldObj.provider;
		for (WorldFeature ft : pvdr.getWorldData().getFeatures(FeatureType.STRUCTURE_PROVIDER)) {
			IStructureProvider isp = (IStructureProvider)ft;
			if (isp.getStrata() == genStrata && isp.willProvideStructureFor(worldObj, i, j)) {
				StructureStart start = isp.getStructureStart(worldObj, rand, i, j);;
				if (start == null) throw new RuntimeException(String.format("Structure provider \"%s\" reported that it would provide a structure for chunk (%d, %d) but did not!", isp.getClass().getName(), i, j));
				return start;
			}
		}
		return null; // This should never happen.
	}

}
