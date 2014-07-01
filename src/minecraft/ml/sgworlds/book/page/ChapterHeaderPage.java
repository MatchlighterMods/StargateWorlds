package ml.sgworlds.book.page;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.book.Book;
import net.minecraft.client.Minecraft;

public class ChapterHeaderPage extends Page {

	public List<String> title;
	public List<String> text = new ArrayList<String>();
	
	public ChapterHeaderPage(Book book, String title, List<String> text) {
		super(book);

		this.title = getFontRenderer().listFormattedStringToWidth(title, book.window.getPageWidth());
		
		int h = (int)(book.window.pageHeight * 0.25F) + (this.title.size()*getFontRenderer().FONT_HEIGHT)/2 + getFontRenderer().FONT_HEIGHT;
		
		while ((h+=getFontRenderer().FONT_HEIGHT) < book.window.getPageHeight() && text.size()>0) {
			this.text.add(text.remove(0));
		}
	}

	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		
		y = y + (int)(h*0.25F) - (title.size()*mc.fontRenderer.FONT_HEIGHT)/2;
		
		for (String ln : title) {
			mc.fontRenderer.drawString(ln, x+(w-mc.fontRenderer.getStringWidth(ln))/2, y, 0x000000);
			y += mc.fontRenderer.FONT_HEIGHT;
		}
		
		y += mc.fontRenderer.FONT_HEIGHT;
		for (String ln : text) {
			mc.fontRenderer.drawString(ln, x, y, 0x000000);
			y += mc.fontRenderer.FONT_HEIGHT;
		}
	}

}
