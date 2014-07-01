package ml.sgworlds.book.page;

import java.util.List;

import ml.sgworlds.window.WindowBook;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class TitlePage extends Page {

	protected String title;
	protected String subtitle;
	
	public TitlePage(WindowBook window, String title, String subtitle) {
		super(window);
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		List<String> lines = mc.fontRenderer.listFormattedStringToWidth(title, w);
		y = y + (h-lines.size()*mc.fontRenderer.FONT_HEIGHT)/2;
		for (String ln : lines) {
			mc.fontRenderer.drawString(ln, x+(w-mc.fontRenderer.getStringWidth(ln))/2, y, 0x000000);
			y += mc.fontRenderer.FONT_HEIGHT;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x + w/2, y , 0);
		GL11.glScalef(0.5F, 0.5F, 1);
		mc.fontRenderer.drawString(subtitle, -mc.fontRenderer.getStringWidth(subtitle)/2, 0, 0x000000);
		GL11.glPopMatrix();
	}

}
