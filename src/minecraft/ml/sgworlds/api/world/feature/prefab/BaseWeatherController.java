package ml.sgworlds.api.world.feature.prefab;

import java.util.Random;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.IWeatherController;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BaseWeatherController extends WorldFeature implements IWeatherController {

	private Random rand = new Random();
	protected int updateLCG = (new Random()).nextInt();
	
	// Rain
	protected int rain_time_base = 12000;
	protected int rain_time_variance = 12000;
	protected int rain_cooldown_base = 168000;
	protected int rain_cooldown_variance = 12000;
	
	protected boolean isRaining;
	protected int rain_time;
	protected float rainingStrength;

	// Thunder
	protected int thunder_time_base = 12000;
	protected int thunder_time_variance = 3600;
	protected int thunder_cooldown_base = 168000;
	protected int thunder_cooldown_variance = 12000;
	
	protected boolean isThundering;
	protected int thunder_time;
	protected float thunderingStrength;
	
	
	public BaseWeatherController(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	public BaseWeatherController(FeatureProvider provider, IWorldData worldData, Random rand) {
		super(provider, worldData);
	}

	public BaseWeatherController(FeatureProvider provider, IWorldData worldData, NBTTagCompound tag) {
		super(provider, worldData);

		// Rain
		isRaining = tag.getBoolean("isRaining");
		rain_time = tag.getInteger("rainTime");
		
		rain_time_base = tag.getInteger("rainTimeBase");
		rain_time_variance = tag.getInteger("rainTimeVari");
		rain_cooldown_base = tag.getInteger("rainCoolBase");
		rain_cooldown_variance = tag.getInteger("rainCoolVari");
		
		// Thunder
		isThundering = tag.getBoolean("isThundering");
		thunder_time = tag.getInteger("thunderTime");
		
		thunder_time_base = tag.getInteger("thunderTimeBase");
		thunder_time_variance = tag.getInteger("thunderTimeVari");
		thunder_cooldown_base = tag.getInteger("thunderCoolBase");
		thunder_cooldown_variance = tag.getInteger("thunderCoolVari");
	}

	@Override
	public void updateWeather() {

		if (rain_time <= 0) {
			if (isRaining) {
				rain_time = this.rand.nextInt(rain_time_base) + rain_time_variance;
			} else {
				rain_time = this.rand.nextInt(rain_cooldown_base) + rain_cooldown_variance;
			}
		} else {
			--rain_time;
			if (rain_time <= 0) {
				isRaining = !isRaining;
			}
		}

		if (thunder_time <= 0) {
			if (isThundering) {
				thunder_time = (this.rand.nextInt(thunder_time_base) + thunder_time_variance);
			} else {
				thunder_time = (this.rand.nextInt(thunder_cooldown_base) + thunder_cooldown_variance);
			}
		} else {
			--thunder_time;
			if (thunder_time <= 0) {
				isThundering = !isThundering;
			}
		}

		if (isRaining) {
			this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
		} else {
			this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
		}

		if (this.rainingStrength < 0.0F) {
			this.rainingStrength = 0.0F;
		}

		if (this.rainingStrength > 1.0F) {
			this.rainingStrength = 1.0F;
		}

		if (isThundering) {
			this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
		} else {
			this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
		}

		if (this.thunderingStrength < 0.0F) {
			this.thunderingStrength = 0.0F;
		}

		if (this.thunderingStrength > 1.0F) {
			this.thunderingStrength = 1.0F;
		}
	}

	@Override
	public void clearWeather() {
		isRaining = false;
		rain_time = 0;
		isThundering = false;
		thunder_time = 0;
	}

	@Override
	public void toggleWeather() {
		rain_time = 1;
	}

	@Override
	public float getRainStrength() {
		return rainingStrength;
	}

	@Override
	public float getThunderStrength() {
		return thunderingStrength;
	}

	@Override
	public void tickLightning(Chunk chnk) {
		World world = worldData.getWorldProvider().worldObj;
		this.updateLCG = this.updateLCG * 3 + 1013904223;
		int i1 = this.updateLCG >> 2;
		int j1 = (chnk.xPosition * 16) + (i1 & 15);
		int k1 = (chnk.zPosition * 16) + (i1 >> 8 & 15);
		int l1 = world.getPrecipitationHeight(j1, k1);

		if (world.canLightningStrikeAt(j1, l1, k1)) {
			world.addWeatherEffect(new EntityLightningBolt(world, (double)j1, (double)l1, (double)k1));
		}
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {
		
		// Rain
		tag.setBoolean("isRaining", isRaining);
		tag.setInteger("rainTime", rain_time);
		
		tag.setInteger("rainTimeBase", rain_time_base);
		tag.setInteger("rainTimeVari", rain_time_variance);
		tag.setInteger("rainCoolBase", rain_cooldown_base);
		tag.setInteger("rainCoolVari", rain_cooldown_variance);
		
		// Thunder
		tag.setBoolean("isThundering", isThundering);
		tag.setInteger("thunderTime", thunder_time);
		
		tag.setInteger("thunderTimeBase", thunder_time_base);
		tag.setInteger("thunderTimeVari", thunder_time_variance);
		tag.setInteger("thunderCoolBase", thunder_cooldown_base);
		tag.setInteger("thunderCoolVari", thunder_cooldown_variance);
		
	}
}
