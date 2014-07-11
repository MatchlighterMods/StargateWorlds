package ml.sgworlds.window;

import ml.sgworlds.book.page.AlphabetPage;
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
		
		// History
		addChapter("History", "The Ancients, or more correctly, the Alterans, were an ancient race of people that evolved on this planet long before humans did. " +
				"", 0x3590FF);
		
		// Language
		addChapter("Language", "The written language of the Ancients is quite similar to English in grammar and spelling. However, the alphabet is significantly different.", 0x35FF90);
		
		addPage(new AlphabetPage(this));
		
		// Worlds
		addChapter("Worlds", "", 0xFF3590);
		// TODO
	}

	
}
