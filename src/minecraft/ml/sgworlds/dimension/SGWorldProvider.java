package ml.sgworlds.dimension;

import ml.sgworlds.api.world.WorldFeatureProvider.IWorldFeature;
import ml.sgworlds.api.world.WorldFeatureType;
import ml.sgworlds.api.world.feature.sky.ICelestialObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class SGWorldProvider extends WorldProvider {

	private SGWorldController worldController;
	private SGWorldSkyRenderer skyRenderer;
	
	@Override
	protected void registerWorldChunkManager() {
		worldController = new SGWorldController(worldObj);
		super.registerWorldChunkManager();
	}
	
	@Override
	public String getDimensionName() {
		return worldController.getName();
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new SGChunkGenerator(worldObj, worldController);
	}
	
	@Override
	public String getSaveFolder() {
		return "SG_WORLD" + this.dimensionId;
	}
	
	@Override
	public boolean canDoLightning(Chunk chunk) {
		// TODO Auto-generated method stub
		return super.canDoLightning(chunk);
	}
	
	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
		// TODO Auto-generated method stub
		return super.canBlockFreeze(x, y, z, byWater);
	}
	
	@Override
	public boolean canDoRainSnowIce(Chunk chunk) {
		// TODO Auto-generated method stub
		return super.canDoRainSnowIce(chunk);
	}
	
	@Override
	public float getCloudHeight() {
		// TODO Auto-generated method stub
		return super.getCloudHeight();
	}
	
	@Override
	public Vec3 getFogColor(float par1, float par2) {
		// TODO Auto-generated method stub
		return super.getFogColor(par1, par2);
	}

	@Override
	public IRenderHandler getSkyRenderer() {
		if (skyRenderer == null) {
			skyRenderer = new SGWorldSkyRenderer(worldController);
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
		float min=0.5F, max=0.5F;
		for (IWorldFeature feat : worldController.getWorldData().getFeatures(WorldFeatureType.SUN)) {
			ICelestialObject sun = (ICelestialObject)feat;
			float s = sun.calculateCelestialAngle(par1, par3);
			if (s<0.0F) s++;
			if (s>1.0F) s--;
			if (s>max) max = s;
			else if (s<min) min=s;
		}
		
		if (1.0F - max < min) return max;
		return min;
	}
}
