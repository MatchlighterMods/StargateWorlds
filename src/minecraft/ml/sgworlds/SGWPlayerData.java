package ml.sgworlds;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

public class SGWPlayerData extends WorldSavedData {

	public List<String> discoveredWorlds = new ArrayList<String>();
	
	public SGWPlayerData(String par1Str) {
		super(par1Str);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		discoveredWorlds.clear();
		NBTTagList lst = tag.getTagList("discoveredWorlds");
		for (int i=0; i<lst.tagCount(); i++) {
			NBTTagString stg = (NBTTagString)lst.tagAt(i);
			discoveredWorlds.add(stg.data);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList tglst = new NBTTagList();
		for (String des : discoveredWorlds) {
			tglst.appendTag(new NBTTagString("", des));
		}
	}

	public static SGWPlayerData getPlayerData(String pln) {
		String fln = "sgwplayer_"+pln;
		MapStorage ms = DimensionManager.getWorld(0).mapStorage;
		WorldSavedData wsd = ms.loadData(SGWPlayerData.class, fln);
		if (wsd == null) {
			wsd = new SGWPlayerData(fln);
			wsd.markDirty();
			ms.setData(fln, wsd);
		}
		return (SGWPlayerData)wsd;
	}
}
