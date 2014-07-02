package ml.sgworlds.window;

import ml.sgworlds.book.page.BlankPage;
import ml.sgworlds.book.page.TitlePage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;

public class WindowBookJournal extends WindowBook {

	public WindowBookJournal(EntityPlayer epl, Side side) {
		super(epl, side);
	}

	@Override
	public void initBookContents() {
		ItemStack bookStack = player.getHeldItem();
		NBTTagCompound tag = bookStack.hasTagCompound() ? bookStack.getTagCompound() : new NBTTagCompound();

		addPage(new BlankPage(this));
		addPage(new TitlePage(this, "Personal Journal", tag.getString("owner")));
		
		// Worlds
		addChapter("Worlds", "", 0x3590FF);
		// TODO
	}

	
}
