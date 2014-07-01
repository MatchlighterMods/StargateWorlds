package ml.sgworlds.network.packet;

import java.io.IOException;

import ml.core.network.MLPacket;
import ml.sgworlds.SGWorlds;
import ml.sgworlds.world.SGWorldData;
import ml.sgworlds.world.SGWorldManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public class PacketWorldData extends MLPacket {

	public @data int dimId;
	public @data NBTTagCompound worldDataNBT;
	
	{
		this.chunkDataPacket = false;
	}
	
	public PacketWorldData(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(pl, dataIn);
	}
	
	public PacketWorldData(SGWorldData worldData) {
		super(SGWorlds.netChannel);
		dimId = worldData.getDimensionId();
		worldDataNBT = new NBTTagCompound();
		worldData.writeToNBT(worldDataNBT);
	}
	
	public PacketWorldData(int dimId) {
		super(SGWorlds.netChannel);
		this.dimId = dimId;
		this.worldDataNBT = new NBTTagCompound();
	}
	
	public PacketWorldData(String designation) {
		super(SGWorlds.netChannel);
		this.worldDataNBT = new NBTTagCompound();
	}
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		SGWorldData worldData = SGWorldManager.instance.getWorldData(dimId);
		if (worldData == null) {
			worldData = new SGWorldData(worldDataNBT);
			SGWorldManager.instance.addClientData(worldData);
		} else {
			worldData.readFromNBT(worldDataNBT); // TODO Some kind of "refreshData" method would be much better than reconstructing *everything*.
			if (dimId != 0 && worldData.getWorldProvider() != null) {
				worldData.setWorldProvider(worldData.getWorldProvider());
			}
		}
	}
	
	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {
		SGWorldData worldData = SGWorldManager.instance.getWorldData(dimId);
		if (worldData != null) {
			new PacketWorldData(worldData).dispatchToPlayer(epl);
		}
	}

}
