package ml.sgworlds.book.page;

import ml.sgworlds.client.ClientProxy;
import ml.sgworlds.window.WindowBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class AlphabetPage extends Page {

	protected String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n0123456789";
	
	public AlphabetPage(WindowBook window) {
		super(window);
	}

	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		FontRenderer fr = getFontRenderer();
		FontRenderer afr = ClientProxy.fontRendererAncient;
		y += fr.FONT_HEIGHT;
		
		int dw = 0;
		for (char c : alphabet.toCharArray()) {
			if (dw+8 > w || c=='\n') {
				dw = 0;
				y+= 3*fr.FONT_HEIGHT;
			}
			if (c == '\n') continue;
			
			String l = String.valueOf(c);
			
			fr.drawString(l, x + dw + (8-fr.getStringWidth(l))/2, y, 0x000000);
			afr.drawString(l, x + dw + (8-afr.getStringWidth(l))/2, y+fr.FONT_HEIGHT, 0x000000);
			dw+=8;
		}
	}

}
