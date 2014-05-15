package ml.sgworlds.dimension;

import stargatetech2.api.stargate.Address;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

public class SGWorldData extends WorldSavedData {

	public SGWorldData(String par1Str) {
		super(par1Str);
		// TODO Auto-generated constructor stub
	}

	public static SGWorldData loadData(Address sga) {
		World overWorld = DimensionManager.getWorld(0);
		String uid = "sgworlddata_"+sga.toString().replace(" ", "");
		SGWorldData data = (SGWorldData)overWorld.loadItemData(SGWorldData.class, uid);
		if (data == null) {
			data = new SGWorldData(uid);
			overWorld.setItemData(uid, data);
			// TODO
			data.markDirty();
		}
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		
	}

}
