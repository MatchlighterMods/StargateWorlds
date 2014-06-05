package ml.sgworlds.network.packet;

import java.io.IOException;

import ml.core.network.MLPacket;
import ml.sgworlds.SGWorlds;
import ml.sgworlds.world.SGWorldData;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

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
	
	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		SGWorldData worldData = SGWorldManager.instance.getWorldData(dimId);
		if (worldData == null) {
			worldData = new SGWorldData(worldDataNBT);
			SGWorldManager.instance.addClientData(worldData);
		} else {
			worldData.readFromNBT(worldDataNBT); // TODO Some kind of "refreshData" method would be much better than reconstructing *everything*.
			if (dimId != 0 && DimensionManager.getWorld(dimId) != null) {
				worldData.setWorldProvider((SGWorldProvider)DimensionManager.getProvider(dimId));
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
