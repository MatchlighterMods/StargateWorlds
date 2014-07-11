package ml.sgworlds.window;

import ml.core.enums.NaturalSide;
import ml.core.gui.MLSlot;
import ml.core.gui.controls.box.ControlAutoSizedBox;
import ml.core.gui.controls.inventory.ControlSlot;
import ml.core.gui.controls.tabs.ControlTabManager;
import ml.core.gui.controls.tabs.TabDynamicLedger;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.Window;
import ml.core.vec.Vector2i;
import ml.sgworlds.client.ClientProxy;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;

public class WindowTablet extends Window {

	public WindowTablet(EntityPlayer epl, Side side, ItemStack tablet) {
		super(epl, side);
		
		setSize(256, 256);
	}

	@Override
	public void constructClient() {
		setCustomResource("window", "SGWorlds:textures/gui/stone_tablet");
		super.constructClient();
	}
	
	@Override
	public void drawForeground(float partialTick) {
		FontRenderer fr = ClientProxy.fontRendererAncient;
		super.drawForeground(partialTick);
	}
	
	@Override
	public ItemStack transferStackFromSlot(EntityPlayer epl, Slot slot) {
		return null;
	}

	@Override
	public void initControls() {
		ControlTabManager ctm = new ControlTabManager(this, NaturalSide.Right);
		new TabTranslate(ctm);
	}

	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return true;
	}

	public class TabTranslate extends TabDynamicLedger {

		ControlSlot journalSlot;
		
		public TabTranslate(ControlTabManager ctm) {
			super(ctm);
			this.title = "Translation";
			GuiElement box = new ControlAutoSizedBox(this, new Vector2i(0, 24), true, false);
			journalSlot = new ControlSlot(box, new MLSlot(guiInventory, guiInventory.getNextSlot(), 3, 3));
			
		}
		
	}
	
}
