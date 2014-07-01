package ml.sgworlds.book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ml.sgworlds.book.page.ChapterHeaderPage;
import ml.sgworlds.book.page.Page;
import ml.sgworlds.book.page.TextPage;
import ml.sgworlds.window.WindowBook;
import net.minecraft.client.gui.FontRenderer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Book {

	public WindowBook window;
	public List<Page> pages = new ArrayList<Page>();
	public Map<Bookmark, Page> bookmarks = new LinkedHashMap<Bookmark, Page>();
	public int currentPage;
	
	public Page addPage(Page page) {
		pages.add(page);
		return page;
	}
	
	public Bookmark addBookmark(Page page, Bookmark mark) {
		this.bookmarks.put(mark, page);
		return mark;
	}
	
	public Page getPage() {
		return pages.get(currentPage);
	}
	
	public Page getPage(int i) {
		return pages.get(i);
	}
	
	public void changePage(int delta) {
		int oPage = currentPage;
		oPage += delta;
		if (oPage >=0 && oPage < pages.size()) {
			this.currentPage = oPage;
		}
	}
	
	public void jumpToPage(Page page) {
		if (pages.contains(page)) {
			int i = pages.indexOf(page);
			if (i%2 == 1) i--;
			currentPage = i;
		}
	}
	
	public void jumpToBookmark(Bookmark mark) {
		if (bookmarks.containsKey(mark))
			jumpToPage(bookmarks.get(mark));
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void initContents(WindowBook window);
	
	@SideOnly(Side.CLIENT)
	public List<Page> addTextPages(String text) {
		List<String> lines = splitStringWidth(text);
		List<Page> pages = new ArrayList<Page>();
		
		while (lines.size() > 0) {
			pages.add(new TextPage(this, lines));
		}
		this.pages.addAll(pages);
		return pages;
	}
	
	@SideOnly(Side.CLIENT)
	public List<Page> addChapter(String title, String text) {
		FontRenderer fr = FMLClientHandler.instance().getClient().fontRenderer;
		
		List<String> lines = splitStringWidth(text);
		List<Page> pages = new ArrayList<Page>();
		
		pages.add(new ChapterHeaderPage(this, title, lines));
		while (lines.size() > 0) {
			pages.add(new TextPage(this, lines));
		}
		this.pages.addAll(pages);
		return pages;
	}
	
	@SideOnly(Side.CLIENT)
	public List<Page> addChapter(String title, String text, int tabColor) {
		List<Page> pages = addChapter(title, text);
		addBookmark(pages.get(0), new Bookmark(title, tabColor));
		return pages;
	}
	
	@SideOnly(Side.CLIENT)
	public List<String> splitStringWidth(String str) {
		FontRenderer fr = FMLClientHandler.instance().getClient().fontRenderer;
		return new ArrayList<String>(fr.listFormattedStringToWidth(str, window.getPageWidth()));
	}
	
	public class Bookmark {
		protected String name;
		protected int color;
		
		public Bookmark(String name, int color) {
			this.name = name;
			this.color = color;
		}

		public String getName() {
			return name;
		}
		
		public int getColor() {
			return color;
		}
	}
}
