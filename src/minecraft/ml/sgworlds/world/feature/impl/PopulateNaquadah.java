package ml.sgworlds.world.feature.impl;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IPopulate;
import ml.sgworlds.world.GenEventHandler;

/**
 * See {@link GenEventHandler}
 * @author Matchlighter
 */
public class PopulateNaquadah extends WorldFeature implements IPopulate {

	public PopulateNaquadah(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public void populate(World world, Random rand, int chunkX, int chunkY) {}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
