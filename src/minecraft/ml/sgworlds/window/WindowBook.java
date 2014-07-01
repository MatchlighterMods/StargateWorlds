package ml.sgworlds.window;

import ml.core.enums.NaturalSide;
import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.button.ControlButton;
import ml.core.gui.controls.tabs.ControlTabManager;
import ml.core.gui.controls.tabs.ControlTabManager.GuiTab;
import ml.core.gui.core.Window;
import ml.core.gui.event.GuiEvent;
import ml.core.gui.event.mouse.EventMouseClicked;
import ml.core.vec.Vector2i;
import ml.sgworlds.book.Book;
import ml.sgworlds.book.Book.Bookmark;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;

public class WindowBook extends Window {

	public Book book;
	public int pageWidth=127, pageHeight=163, pagePadding = 6;
	
	protected ControlButton btnPrevious, btnNext;
	
	public WindowBook(EntityPlayer epl, Side side, Book book) {
		super(epl, side);
		setCustomResource("window", "SGworlds:textures/gui/book_double");
		setSize(278, 175);
		
		this.book = book;
		this.book.window = this;
		if (side == Side.CLIENT) {
			this.book.initContents(this);
		}
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
		
		book.getPage().drawPage(getMC(), 12 + pagePadding, 6 + pagePadding, awidth, aheight, partialTick);
		
		if (book.pages.size() > book.currentPage+1) {
			book.getPage(book.currentPage+1).drawPage(getMC(), 12 + 129 + pagePadding, 6 + pagePadding, awidth, aheight, partialTick);
		}
	}

	@Override
	public void initControls() {
		ControlTabManager ctm = new ControlTabManager(this, NaturalSide.Right);
		for (Bookmark bm : book.bookmarks.keySet()) {
			new TabBookmark(ctm, bm);
		}
		btnPrevious = new ControlButton(this, new Vector2i(0, 178), new Vector2i(20,20), "<");
		btnNext = new ControlButton(this, new Vector2i(258, 178), new Vector2i(20,20), ">");
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt instanceof EventMouseClicked) {
			if (evt.origin == btnPrevious) {
				book.changePage(-2);
			} else if (evt.origin == btnNext) {
				book.changePage(2);
			}
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
			setCustomResource("ledger", "SGWorlds:textures/gui/tab_bookmark");
			this.mark = mark;
			this.tabColor = mark.getColor();
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
				book.jumpToBookmark(mark);
			}
			super.handleEvent(evt);
		}
	}
}
