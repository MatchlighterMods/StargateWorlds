package ml.sgworlds.window;

import ml.core.book.BlankPage;
import ml.core.book.TitlePage;
import ml.core.book.WindowBook;
import ml.core.gui.event.EventDataPacketReceived;
import ml.core.gui.event.GuiEvent;
import ml.core.item.StackUtils;
import ml.sgworlds.SGWPlayerData;
import ml.sgworlds.bookpages.AlphabetPage;
import ml.sgworlds.bookpages.SGWorldPage;
import ml.sgworlds.item.DelegateJournal;
import ml.sgworlds.world.SGWorldData;
import ml.sgworlds.world.SGWorldManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.relauncher.Side;

public class WindowBookJournal extends WindowBook {

	public WindowBookJournal(EntityPlayer epl, Side side) {
		super(epl, side);
	}

	@Override
	public void initBookContents() {
		addPage(new BlankPage(this));
		addPage(new TitlePage(this, "Personal Journal", StackUtils.getTag(player.getHeldItem(), "", "owner")));
		
		// History
		addChapter("History", "I have discovered remnants of an ancient race of people. " +
				"I believe that their formal name was \"Alteran\", but for now I will refer to them as \"Ancients\". " +
				"From the information I have gathered, I believe that they were the first humanoid evolution that occured on our planet, " +
				"but their civilization was destroyed by a virulent plague long before humans evolved. " +
				"I have found some vague references to a lost city where some of the Ancients were able to escape the plague, but I haven't found any real evidence.\n\n" +
				"The Ancients appear to have been a very advanced race in terms of technology. So advanced, in fact, that " +
				"I believe that they are the true creators of the Stargates. ", 0x3590FF);
		
		// Language
		addChapter("Language", "With some effort, I have been able to decipher the language of the ancients. " +
				"Their written language seems to be similar to English in spelling, grammar, and mechanics, but the alphabet is significantly different: " +
				"", 0x35FF90);
		
		addPage(new AlphabetPage(this));
		
		// Worlds
		addChapter("Worlds", "", 0xFF3590);
	}
	
	@Override
	public void initControls() {
		super.initControls();
		if (side == Side.SERVER) {
			NBTTagCompound ptag = new NBTTagCompound();
			NBTTagList plst = new NBTTagList();
			
			if (DelegateJournal.isCreativeSpawned(player.getHeldItem())) {
				for (SGWorldData sgwd : SGWorldManager.instance.worlds) {
					NBTTagCompound wtag = new NBTTagCompound();
					sgwd.writeToNBT(wtag);
					plst.appendTag(wtag);
				}
			} else {
				String pln = DelegateJournal.getOwnerName(player.getHeldItem());
				if (pln != null) {
					SGWPlayerData jsd = SGWPlayerData.getPlayerData(pln);
					for (String des : jsd.discoveredWorlds) {
						SGWorldData sgwd = SGWorldManager.instance.getWorldData(des);
						if (sgwd != null) {
							NBTTagCompound wtag = new NBTTagCompound();
							sgwd.writeToNBT(wtag);
							plst.appendTag(wtag);
						}
					}
				}
			}
			
			ptag.setTag("worlds", plst);
			sendPacket(ptag, Side.CLIENT);
		}
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		super.handleEvent(evt);
		if (evt instanceof EventDataPacketReceived && this.side == Side.CLIENT) {
			EventDataPacketReceived edpr = (EventDataPacketReceived)evt;
			NBTTagCompound tag = edpr.payload;
			
			NBTTagList lst = (NBTTagList)tag.getTag("worlds");
			for (int i=0; i<lst.tagCount(); i++) {
				NBTTagCompound stg = (NBTTagCompound)lst.tagAt(i);
				addPage(new SGWorldPage(this, new SGWorldData(stg)));
			}
		}
	}
}
