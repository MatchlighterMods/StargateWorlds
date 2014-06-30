package ml.sgworlds.network;

import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ServerConnectionHandler implements IConnectionHandler {

	private boolean connectedToRemote = false;
	
	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		EntityPlayerMP emp = (EntityPlayerMP)player;
		if (emp.worldObj.provider instanceof SGWorldProvider) {
			manager.addToSendQueue(new PacketWorldData(((SGWorldProvider)emp.worldObj.provider).getWorldData()).convertToPkt250());
		}
	}

	@Override // Not called when connecting to the Integrated server.
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		manager.addToSendQueue(new PacketRegisterDimensions(SGWorldManager.instance.registeredDims).convertToPkt250());
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
		connectedToRemote = true;
		SGWorldManager.instance = new SGWorldManager();
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

	@Override
	public void connectionClosed(INetworkManager manager) {
		if (connectedToRemote) {
			connectedToRemote = false;
			SGWorldManager.instance.unregisterDimensions();
			SGWorldManager.instance = null;
		}
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {}

}
