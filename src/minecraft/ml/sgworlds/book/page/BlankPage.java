package ml.sgworlds.book.page;

import ml.sgworlds.book.Book;
import net.minecraft.client.Minecraft;

public class BlankPage extends Page {

	public BlankPage(Book book) {
		super(book);
	}

	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {}

}
