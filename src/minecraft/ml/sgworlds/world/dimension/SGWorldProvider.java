package ml.sgworlds.world.dimension;

import ml.sgworlds.api.world.feature.FeatureType;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.ICelestialObject;
import ml.sgworlds.api.world.feature.types.IColorProvider;
import ml.sgworlds.api.world.feature.types.ILightingController;
import ml.sgworlds.api.world.feature.types.ISkyColor;
import ml.sgworlds.api.world.feature.types.IWeatherController;
import ml.sgworlds.world.SGWorldData;
import ml.sgworlds.world.SGWorldManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class SGWorldProvider extends WorldProvider {

	private SGWorldData worldData;
	private SGWorldSkyRenderer skyRenderer;
	
	@Override
	protected void registerWorldChunkManager() {
		worldData = SGWorldManager.instance.getClientWorldData(dimensionId);
		this.worldChunkMgr = new SGChunkManager(worldData);
		worldData.setWorldProvider(this);
	}
	
	@Override
	public String getDimensionName() {
		return worldData.getDisplayName();
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new SGChunkGenerator(worldObj, worldData);
	}
	
	@Override
	public String getSaveFolder() {
		return worldData.getSaveFolderName();
	}
	
	@Override
	public long getWorldTime() {
		return worldData.getWorldTime();
	}

	@Override
	public void setWorldTime(long time) {
		worldData.setWorldTime(time);
	}
	
	@Override
	public long getSeed() {
		return worldData.getWorldSeed();
	}

	@Override
	public IRenderHandler getSkyRenderer() {
		if (skyRenderer == null) {
			skyRenderer = new SGWorldSkyRenderer(worldData);
		}
		return skyRenderer;
	}
	
	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		// TODO Auto-generated method stub
		return super.getRespawnDimension(player);
	}
	
	// Celestial angle of 0 or 1 = Noon
	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		return getCelestialAngle(par3);
	}
	
	public float getCelestialAngle(float partialTicks) {
		float min=0.5F, max=0.5F;
		for (WorldFeature feat : worldData.getFeatures(FeatureType.SUN)) {
			ICelestialObject sun = (ICelestialObject)feat;
			float s = sun.calculateCelestialAngle(getWorldTime(), partialTicks);
			if (s<0.0F) s++;
			if (s>1.0F) s--;
			if (s>max) max = s;
			else if (s<min) min=s;
		}
		
		if (1.0F - max < min) return max;
		return min;
	}
	
	public long getTimeToSunrise(long worldTime) {
		long dtime = -1;
		for (WorldFeature feature : worldData.getFeatures(FeatureType.SUN)) {
			ICelestialObject sun = (ICelestialObject)feature;
			long tmToRise = sun.getTimeToRise(worldTime);
			if (dtime < 0 || tmToRise < dtime) dtime = tmToRise;
		}
		return dtime >= 0 ? dtime : 0;
	}
	
	@Override
	public void calculateInitialWeather() {
		updateWeather();
	}

	// Once per tick
	@Override
	public void updateWeather() {
		IWeatherController weatherController = (IWeatherController)worldData.getFeature(FeatureType.WEATHER_CONTROLLER);
		weatherController.updateWeather();
		
		this.worldObj.prevRainingStrength = this.worldObj.rainingStrength;
		this.worldObj.rainingStrength = weatherController.getRainStrength();
		this.worldObj.prevThunderingStrength = this.worldObj.thunderingStrength;
		this.worldObj.thunderingStrength = weatherController.getThunderStrength();
		
		onTick();
	}
	
	@Override
	public void toggleRain() {
		((IWeatherController)worldData.getFeature(FeatureType.WEATHER_CONTROLLER)).toggleWeather();
	}
	
	@Override
	public void resetRainAndThunder() {
		((IWeatherController)worldData.getFeature(FeatureType.WEATHER_CONTROLLER)).clearWeather();
	}
	
	// Occurs as part of tickBlocksAndAmbience
	@Override
	public boolean canDoLightning(Chunk chunk) {
		((IWeatherController)worldData.getFeature(FeatureType.WEATHER_CONTROLLER)).tickLightning(chunk);
		return false;
	}
	
	@Override
	public float getCloudHeight() { //TODO IWeatherController?
		return super.getCloudHeight();
	}
	
	@Override
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		return null;
	}
	
	@Override
	public Vec3 drawClouds(float partialTicks) {
		float celestialAngle = getCelestialAngle(partialTicks);
		float f2 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

		if (f2 < 0.0F) {
			f2 = 0.0F;
		}

		if (f2 > 1.0F) {
			f2 = 1.0F;
		}

		Vec3 cloudColor = ((IColorProvider)worldData.getFeature(FeatureType.CLOUD_COLOR)).getColor(partialTicks);
		float cloudRed = (float)cloudColor.xCoord;
		float cloudGreen = (float)cloudColor.yCoord;
		float cloudBlue = (float)cloudColor.zCoord;
		float rainStrength = worldObj.getRainStrength(partialTicks);
		float f7;
		float f8;

		if (rainStrength > 0.0F) {
			f7 = (cloudRed * 0.3F + cloudGreen * 0.59F + cloudBlue * 0.11F) * 0.6F;
			f8 = 1.0F - rainStrength * 0.95F;
			cloudRed = cloudRed * f8 + f7 * (1.0F - f8);
			cloudGreen = cloudGreen * f8 + f7 * (1.0F - f8);
			cloudBlue = cloudBlue * f8 + f7 * (1.0F - f8);
		}

		cloudRed *= f2 * 0.9F + 0.1F;
		cloudGreen *= f2 * 0.9F + 0.1F;
		cloudBlue *= f2 * 0.85F + 0.15F;
		f7 = worldObj.getWeightedThunderStrength(partialTicks);

		if (f7 > 0.0F) {
			f8 = (cloudRed * 0.3F + cloudGreen * 0.59F + cloudBlue * 0.11F) * 0.2F;
			float f9 = 1.0F - f7 * 0.95F;
			cloudRed = cloudRed * f9 + f8 * (1.0F - f9);
			cloudGreen = cloudGreen * f9 + f8 * (1.0F - f9);
			cloudBlue = cloudBlue * f9 + f8 * (1.0F - f9);
		}

		return worldObj.getWorldVec3Pool().getVecFromPool((double)cloudRed, (double)cloudGreen, (double)cloudBlue);
	}
	
	@Override
	public Vec3 getFogColor(float celestialAngle, float partialTicks) {
		return ((IColorProvider)worldData.getFeature(FeatureType.FOG_COLOR)).getColor(partialTicks);
	}
	
	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		float celestialAngle = getCelestialAngle(partialTicks);
		
		Vec3 skyColor = ((ISkyColor)worldData.getFeature(FeatureType.SKY_COLOR)).getSkyColor(cameraEntity, celestialAngle, partialTicks);
		float colorRed = (float)skyColor.xCoord;
		float colorGreen = (float)skyColor.yCoord;
		float colorBlue = (float)skyColor.zCoord;
		
		float rainStrength = worldObj.getRainStrength(partialTicks);
		float f8;
		float f9;

		if (rainStrength > 0.0F) {
			f8 = (colorRed * 0.3F + colorGreen * 0.59F + colorBlue * 0.11F) * 0.6F;
			f9 = 1.0F - rainStrength * 0.75F;
			colorRed = colorRed * f9 + f8 * (1.0F - f9);
			colorGreen = colorGreen * f9 + f8 * (1.0F - f9);
			colorBlue = colorBlue * f9 + f8 * (1.0F - f9);
		}

		f8 = worldObj.getWeightedThunderStrength(partialTicks);

		if (f8 > 0.0F) {
			f9 = (colorRed * 0.3F + colorGreen * 0.59F + colorBlue * 0.11F) * 0.2F;
			float f10 = 1.0F - f8 * 0.75F;
			colorRed = colorRed * f10 + f9 * (1.0F - f10);
			colorGreen = colorGreen * f10 + f9 * (1.0F - f10);
			colorBlue = colorBlue * f10 + f9 * (1.0F - f10);
		}

		if (worldObj.lastLightningBolt > 0) {
			f9 = (float)worldObj.lastLightningBolt - partialTicks;

			if (f9 > 1.0F) {
				f9 = 1.0F;
			}

			f9 *= 0.45F;
			colorRed = colorRed * (1.0F - f9) + 0.8F * f9;
			colorGreen = colorGreen * (1.0F - f9) + 0.8F * f9;
			colorBlue = colorBlue * (1.0F - f9) + 1.0F * f9;
		}

		return worldObj.getWorldVec3Pool().getVecFromPool(colorRed, colorGreen, colorBlue);
	}
	
	@Override
	protected void generateLightBrightnessTable() {
		((ILightingController)worldData.getFeature(FeatureType.LIGHTING_CONTROLLER)).populateBrightnessTable(lightBrightnessTable);
	}
	
	public void onTick() {
		if (worldObj.isRemote) {
			
		} else if (worldObj instanceof WorldServer) {
			if (((WorldServer)worldObj).areAllPlayersAsleep()) {
				setWorldTime(getWorldTime() + getTimeToSunrise(getWorldTime()));
			}
			
			setWorldTime(getWorldTime() + 1L);
		}
	}
	
}
