package ml.sgworlds.book.page;

import ml.sgworlds.window.WindowBook;
import net.minecraft.client.Minecraft;

public class BlankPage extends Page {

	public BlankPage(WindowBook window) {
		super(window);
	}

	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {}

}
