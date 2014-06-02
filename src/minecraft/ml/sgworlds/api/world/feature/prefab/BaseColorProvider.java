package ml.sgworlds.api.world.feature.prefab;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IColorProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class BaseColorProvider extends WorldFeature implements IColorProvider {

	public Vec3 color;
	
	public BaseColorProvider(FeatureProvider provider, IWorldData worldData, int color) {
		super(provider, worldData);
		this.color = Vec3.createVectorHelper((float)((color>>16)&255) / 255F, (float)((color>>8)&255) / 255F, (float)(color&255) / 255F);
	}
	
	public BaseColorProvider(FeatureProvider provider, IWorldData worldData, Vec3 color) {
		super(provider, worldData);
		this.color = color;
	}
	
	public BaseColorProvider(FeatureProvider provider, IWorldData worldData, Random rand) {
		this(provider, worldData, Vec3.createVectorHelper(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
	}
	
	public BaseColorProvider(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		this(provider, worldData, Vec3.createVectorHelper(tag.getFloat("cloudRed"), tag.getFloat("cloudGreen"), tag.getFloat("cloudBlue")));
	}

	@Override
	public Vec3 getColor(float partialTicks) {
		return color;
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		tag.setFloat("cloudRed", (float)color.xCoord);
		tag.setFloat("cloudGreen", (float)color.yCoord);
		tag.setFloat("cloudBlue", (float)color.zCoord);
	}

}
