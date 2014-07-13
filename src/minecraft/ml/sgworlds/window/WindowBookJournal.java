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
import ml.sgworlds.world.SGWorldData;
import ml.sgworlds.world.SGWorldManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;
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
		addChapter("History", "The Ancients, or more correctly, the Alterans, were an ancient race of people that evolved on this planet long before humans did. " +
				"", 0x3590FF);
		
		// Language
		addChapter("Language", "The written language of the Ancients is quite similar to English in grammar and spelling. However, the alphabet is significantly different.", 0x35FF90);
		
		addPage(new AlphabetPage(this));
		
		// Worlds
		addChapter("Worlds", "", 0xFF3590);
	}
	
	@Override
	public void initControls() {
		super.initControls();
		if (side == Side.SERVER) {
			String pln = StackUtils.getTag(player.getHeldItem(), "", "owner");
			
			MapStorage ms = DimensionManager.getWorld(0).mapStorage;
			SGWPlayerData jsd = SGWPlayerData.getPlayerData(pln);
			if (jsd != null) {
				NBTTagCompound ptag = new NBTTagCompound();
				NBTTagList plst = new NBTTagList();
				
				for (String des : jsd.discoveredWorlds) {
					SGWorldData sgwd = SGWorldManager.instance.getWorldData(des);
					if (sgwd != null) {
						NBTTagCompound wtag = new NBTTagCompound();
						sgwd.writeToNBT(wtag);
						plst.appendTag(wtag);
					}
				}
				
				ptag.setTag("worlds", plst);
				sendPacket(ptag, Side.CLIENT);
			}
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
