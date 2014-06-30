package ml.sgworlds.window;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ml.core.enums.NaturalSide;
import ml.core.gui.MLSlot;
import ml.core.gui.controls.inventory.ControlSlot;
import ml.core.gui.controls.tabs.ControlTabManager;
import ml.core.gui.controls.tabs.TabLedger;
import ml.core.gui.core.Window;
import ml.core.vec.Vector2i;
import ml.sgworlds.client.ClientProxy;

public class WindowTablet extends Window {

	public WindowTablet(EntityPlayer epl, Side side, ItemStack tablet) {
		super(epl, side);
		setCustomResource("window", "SGWorlds:textures/gui/stone_tablet");
		setSize(256, 256);
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
		new TabTranslate(ctm, new Vector2i(90, 60));
	}

	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return true;
	}

	public class TabTranslate extends TabLedger {

		ControlSlot journalSlot;
		
		public TabTranslate(ControlTabManager ctm, Vector2i oSize) {
			super(ctm, oSize);
			this.title = "Translation";
			journalSlot = new ControlSlot(this, new MLSlot(guiInventory, guiInventory.getNextSlot(), 6, 24));
			
		}
		
	}
	
}
