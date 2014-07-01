package ml.sgworlds.window;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ml.core.enums.NaturalSide;
import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.button.ControlButton;
import ml.core.gui.controls.tabs.ControlTabManager;
import ml.core.gui.controls.tabs.ControlTabManager.GuiTab;
import ml.core.gui.core.Window;
import ml.core.gui.event.EventGuiClosing;
import ml.core.gui.event.GuiEvent;
import ml.core.gui.event.mouse.EventMouseClicked;
import ml.core.vec.Vector2i;
import ml.sgworlds.book.page.ChapterHeaderPage;
import ml.sgworlds.book.page.Page;
import ml.sgworlds.book.page.TextPage;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class WindowBook extends Window {

	public List<Page> pages = new ArrayList<Page>();
	public Map<Bookmark, Page> bookmarks = new LinkedHashMap<Bookmark, Page>();
	protected int currentPage;
	
	public int pageWidth=127, pageHeight=163, pagePadding = 6;
	
	protected ControlButton btnPrevious, btnNext;
	
	public WindowBook(EntityPlayer epl, Side side) {
		super(epl, side);
		setSize(278, 175);
		
		if (side == Side.CLIENT) {
			initBookContents();
		}
	}
	
	@Override
	public void constructClient() {
		setCustomResource("window", "SGworlds:textures/gui/book_double");
		super.constructClient();
	}

	public int getPageWidth() {
		return pageWidth - 2*pagePadding;
	}
	
	public int getPageHeight() {
		return pageHeight - 2*pagePadding;
	}
	
	@Override
	public ItemStack transferStackFromSlot(EntityPlayer epl, Slot slot) {
		return null;
	}
	
	@Override
	public void drawBackground(float partialTick) {
		bindStyleTexture("window");
		GuiRenderUtils.drawTexturedModalRect(this.getLocalPosition().x, this.getLocalPosition().y, 0, 0, this.getSize().x, this.getSize().y, 512,256);
	}
	
	@Override
	public void drawForeground(float partialTick) {
		getLocalPosition().glTranslate();
		int awidth = pageWidth - 2*pagePadding, aheight = pageHeight - 2*pagePadding;
		
		pages.get(currentPage).drawPage(getMC(), 12 + pagePadding, 6 + pagePadding, awidth, aheight, partialTick);
		
		if (pages.size() > currentPage+1) {
			pages.get(currentPage+1).drawPage(getMC(), 12 + 129 + pagePadding, 6 + pagePadding, awidth, aheight, partialTick);
		}
	}

	@Override
	public void initControls() {
		ControlTabManager ctm = new ControlTabManager(this, NaturalSide.Right);
		for (Bookmark bm : bookmarks.keySet()) {
			new TabBookmark(ctm, bm);
		}
		btnPrevious = new ControlButton(this, new Vector2i(0, 178), new Vector2i(20,20), "<");
		btnNext = new ControlButton(this, new Vector2i(258, 178), new Vector2i(20,20), ">");
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt instanceof EventMouseClicked) {
			if (evt.origin == btnPrevious) {
				changePage(-2);
			} else if (evt.origin == btnNext) {
				changePage(2);
			}
		} else if (evt instanceof EventGuiClosing) {
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return true;
	}
	
	@Override
	public boolean shouldShowNEI() {
		return false;
	}

	public class TabBookmark extends GuiTab {

		protected Bookmark mark;
		
		public TabBookmark(ControlTabManager ctm, Bookmark mark) {
			super(ctm);
			
			this.mark = mark;
			this.tabColor = mark.getColor();
		}
		
		@Override
		public void constructClient() {
			setCustomResource("ledger", "SGWorlds:textures/gui/tab_bookmark");
			super.constructClient();
		}
		
		@Override
		public Vector2i getTargetSize() {
			return new Vector2i(treeHasHover() ? 24 + getMC().fontRenderer.getStringWidth(mark.getName()) : 18, 24);
		}
		
		@Override
		public void drawForeground(float partialTick) {
			getLocalPosition().glTranslate();
			FontRenderer fr = getMC().fontRenderer;
			if (treeHasHover() && getSize().equals(getTargetSize())) {
				fr.drawString(this.mark.getName(), 6, (24-getGui().getMinecraft().fontRenderer.FONT_HEIGHT)/2+1, 0xFFFFFF, true);
			}
		}
		
		@Override
		public void handleEvent(GuiEvent evt) {
			if (evt instanceof EventMouseClicked && evt.origin == this) {
				jumpToBookmark(mark);
			}
			super.handleEvent(evt);
		}
	}
	
	// Book Stuff
	
	public abstract void initBookContents();
	
	public Page addPage(Page page) {
		pages.add(page);
		return page;
	}
	
	public Bookmark addBookmark(Page page, Bookmark mark) {
		this.bookmarks.put(mark, page);
		return mark;
	}
	
	public void changePage(int delta) {
		int oPage = currentPage;
		oPage += delta;
		if (oPage >=0 && oPage < pages.size()) {
			currentPage = oPage;
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
		return new ArrayList<String>(fr.listFormattedStringToWidth(str, getPageWidth()));
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
