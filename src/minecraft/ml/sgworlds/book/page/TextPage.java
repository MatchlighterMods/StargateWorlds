package ml.sgworlds.book.page;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.book.Book;
import net.minecraft.client.Minecraft;

public class TextPage extends Page {

	protected List<String> text = new ArrayList<String>();
	
	public TextPage(Book book, List<String> text) {
		super(book);
		
		int h = 0;
		while ((h+=getFontRenderer().FONT_HEIGHT) < book.window.getPageHeight() && text.size()>0) {
			this.text.add(text.remove(0));
		}
	}
	
	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		for (String ln : text) {
			mc.fontRenderer.drawString(ln, x, y, 0x000000);
			y+=mc.fontRenderer.FONT_HEIGHT;
		}
	}

}
