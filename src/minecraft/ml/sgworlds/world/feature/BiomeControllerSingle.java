package ml.sgworlds.world.feature;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.api.world.FeatureProvider;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.IBiomeController;
import ml.sgworlds.api.world.feature.prefab.BaseBiomeController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.IntCache;

public class BiomeControllerSingle extends BaseBiomeController implements IBiomeController {

	public List<BiomeGenBase> biomeList;

	public BiomeControllerSingle(FeatureProvider provider, IWorldData worldData, BiomeGenBase biome) {
		super(provider, worldData);

		this.biomeList = new ArrayList<BiomeGenBase>();
		this.biomeList.add(biome);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readNBTData(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BiomeGenBase> getBiomesForSpawn() {
		return biomeList;
	}

	@Override
	public BiomeGenBase getBiomeAt(int x, int z) {
		return biomeList.get(0);
	}

	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] reuseArray, int x, int z, int width, int length) {
		IntCache.resetIntCache();

		if (reuseArray == null || reuseArray.length < width * length) {
			reuseArray = new BiomeGenBase[width * length];
		}

		for (int lx=0; lx<width; lx++) {
			for (int lz=0; lz<length; lz++) {
				reuseArray[lx + lz*width] = getBiomeAt(x+lx, z+lz);
			}
		}

		return reuseArray;
	}
}
