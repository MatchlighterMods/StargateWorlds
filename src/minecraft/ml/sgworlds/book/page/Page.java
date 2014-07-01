package ml.sgworlds.book.page;

import ml.sgworlds.window.WindowBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Page {

	public final WindowBook window;

	public Page(WindowBook window) {
		this.window = window;
	}

	@SideOnly(Side.CLIENT)
	public abstract void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick);
	
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer() {
		return FMLClientHandler.instance().getClient().fontRenderer;
	}
}
