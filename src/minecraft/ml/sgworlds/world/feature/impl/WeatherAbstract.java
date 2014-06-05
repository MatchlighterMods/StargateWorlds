package ml.sgworlds.world.feature.impl;

import java.util.Random;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IWeatherController;

public abstract class WeatherAbstract extends WorldFeature implements IWeatherController {

	protected Random rand = new Random();
	protected int updateLCG = (new Random()).nextInt();
	
	protected int resetTime = 12000;
	protected int resetTicker = 0;
	
	protected int lightningRarity = 100000;
	protected float rainStrength = 0.0F;
	protected float thunderStrength = 0.0F;
	protected boolean enableLightning = false;
	
	public WeatherAbstract(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
		startWeather();
	}
	
	public WeatherAbstract(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData);
		resetTicker = tag.getInteger("resetTicker");
		if (resetTicker==0) startWeather();
	}

	@Override
	public float getThunderStrength() {
		return thunderStrength;
	}

	@Override
	public float getRainStrength() {
		return rainStrength;
	}

	@Override
	public void toggleWeather() {
		if (resetTicker > 0) {
			startWeather();
		} else {
			clearWeather();
		}
	}

	@Override
	public void clearWeather() {
		rainStrength = 0.0F;
		thunderStrength = 0.0F;
		enableLightning = false;
		resetTicker = resetTime;
	}

	@Override
	public void updateWeather() {
		if (resetTicker > 0 && --resetTicker==0) {
			startWeather();
		}
	}

	@Override
	public void tickLightning(Chunk chnk) {
		World world = worldData.getWorldProvider().worldObj;
		this.updateLCG = this.updateLCG * 3 + 1013904223;
		int i1 = this.updateLCG >> 2;
		int j1 = (chnk.xPosition * 16) + (i1 & 15);
		int k1 = (chnk.zPosition * 16) + (i1 >> 8 & 15);
		int l1 = world.getPrecipitationHeight(j1, k1);

		if (enableLightning && canLightningStrikeAt(j1, l1, k1) && this.rand.nextInt(lightningRarity) == 0) {
			world.addWeatherEffect(new EntityLightningBolt(world, (double)j1, (double)l1, (double)k1));
		}
	}
	
	public boolean canLightningStrikeAt(int par1, int par2, int par3) {
		World world = worldData.getWorldProvider().worldObj;
		
		if (!world.canBlockSeeTheSky(par1, par2, par3)) {
			return false;
			
		} else {
			BiomeGenBase biomegenbase = world.getBiomeGenForCoords(par1, par3);
			return biomegenbase.getEnableSnow() ? false : biomegenbase.canSpawnLightningBolt();
		}
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		tag.setInteger("resetTicker", resetTicker);
	}
	
	public abstract void startWeather();

}
