package ml.sgworlds.book.page;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.sgworlds.book.Book;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

@SideOnly(Side.CLIENT)
public abstract class Page {

	public final Book book;
	
	public Page(Book book) {
		super();
		this.book = book;
	}

	public abstract void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick);
	
	public FontRenderer getFontRenderer() {
		return FMLClientHandler.instance().getClient().fontRenderer;
	}
}
