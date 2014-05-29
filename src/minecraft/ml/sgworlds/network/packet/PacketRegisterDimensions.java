package ml.sgworlds.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

import ml.core.network.MLPacket;
import ml.sgworlds.SGWorlds;
import ml.sgworlds.world.SGWorldManager;

public class PacketRegisterDimensions extends MLPacket {

	public @data int[] dims;
	
	public PacketRegisterDimensions(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(pl, dataIn);
	}
	
	public PacketRegisterDimensions(int[] dims) {
		super(SGWorlds.netChannel);
		this.dims = dims;
	}
	
	public PacketRegisterDimensions(int dim) {
		super(SGWorlds.netChannel);
		this.dims = new int[]{dim};
	}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		for (int dim : dims) {
			SGWorldManager.instance.registerDimension(dim);
		}
	}
}
