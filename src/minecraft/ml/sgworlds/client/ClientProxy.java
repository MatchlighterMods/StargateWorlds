package ml.sgworlds.client;

import ml.sgworlds.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.ReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy {

	public static FontRenderer fontRendererAncient;
	
	public ClientProxy() {
	}
	
	@Override
	public void load() {
		super.load();
		
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		fontRendererAncient = new FontRenderer(mc.gameSettings, new ResourceLocation("SGWorlds:textures/font/ancient.png"), mc.renderEngine, false) {
			@Override
			public int getCharWidth(char par1) {
				if (Character.isLetterOrDigit(par1)) return 4;
				if (par1 == ' ') return 4;
				return 4;
			}
		};
		((ReloadableResourceManager)mc.getResourceManager()).registerReloadListener(fontRendererAncient);
		
	}
}
