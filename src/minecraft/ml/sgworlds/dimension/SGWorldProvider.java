package ml.sgworlds.dimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class SGWorldProvider extends WorldProvider {

	private SGWorldController worldController;
	
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
		return super.createChunkGenerator();
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
		// TODO Auto-generated method stub
		return super.getSkyRenderer();
	}
	
	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		// TODO Auto-generated method stub
		return super.getRespawnDimension(player);
	}
}
