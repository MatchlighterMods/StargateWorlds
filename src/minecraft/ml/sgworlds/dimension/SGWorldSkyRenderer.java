package ml.sgworlds.dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class SGWorldSkyRenderer extends IRenderHandler {
	
	private final SGWorldController worldController;
	
	public SGWorldSkyRenderer(SGWorldController wdata) {
		worldController = wdata;
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		// TODO Auto-generated method stub

	}

}
