package ml.sgworlds.bookpages;

import ml.core.book.Page;
import ml.core.book.WindowBook;
import ml.sgworlds.world.SGWorldData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class SGWorldPage extends Page {

	protected final SGWorldData wData;
	
	public SGWorldPage(WindowBook window, SGWorldData world) {
		super(window);
		this.wData = world;
	}

	private int drawLine(String str, int x, int y, int w) {
		FontRenderer fr = getFontRenderer();
		fr.drawSplitString(str, x, y, w, 0x000000);
		return fr.splitStringWidth(str, w);
	}
	
	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		FontRenderer fr = getFontRenderer();
		String tl = wData.getDisplayName();
		if (!tl.equals(wData.getDesignation())) tl += " (" + wData.getDesignation() + ")";
		
		y+= drawLine(tl, x, y, w);
		y+=fr.FONT_HEIGHT/2;
		//y+= drawLine("Designation: " + wData.getDesignation(), x, y, w);
		
		if (wData.getPrimaryAddress() != null)
			y+= drawLine("Address: " + wData.getPrimaryAddress().toString(), x, y, w);
		
		y+=fr.FONT_HEIGHT;
		y+= drawLine(wData.getDescription(3, 75, " "), x, y, w);
	}

}
