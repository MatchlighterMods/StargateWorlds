package ml.sgworlds.world.feature.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.prefab.BaseBiomeController;
import ml.sgworlds.api.world.feature.types.IBiomeController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.IntCache;

public class BiomeControllerSingle extends BaseBiomeController implements IBiomeController {

	public List<BiomeGenBase> biomeList = new ArrayList<BiomeGenBase>();

	public BiomeControllerSingle(FeatureProvider provider, IWorldData worldData, BiomeGenBase biome) {
		super(provider, worldData);

		this.biomeList.add(biome);
	}

	public BiomeControllerSingle(FeatureProvider provider, IWorldData worldData, Random rnd) {
		super(provider, worldData);
		
		BiomeGenBase[] vbiomes = WorldType.DEFAULT.getBiomesForWorldType();
		this.biomeList.add(vbiomes[rnd.nextInt(vbiomes.length)]);
	}
	
	public BiomeControllerSingle(FeatureProvider provider, IWorldData worldData, NBTTagCompound nbtData) {
		super(provider, worldData);
		
		this.biomeList.add(BiomeGenBase.biomeList[nbtData.getInteger("biomeId")]);
	}
	
	@Override
	public void writeNBTData(NBTTagCompound tag) {
		tag.setInteger("biomeId", biomeList.get(0).biomeID);
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
