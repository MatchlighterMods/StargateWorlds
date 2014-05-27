package ml.sgworlds.network;

import ml.sgworlds.dimension.SGWorldManager;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ServerConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {}

	@Override // Not called when connecting to the Integrated server.
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		manager.addToSendQueue(new PacketRegisterDimensions((ArrayUtils.toPrimitive((Integer[])SGWorldManager.instance.registeredDims.toArray()))).convertToPkt250());
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
		SGWorldManager.instance = new SGWorldManager("client_worldmanager");
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

	@Override
	public void connectionClosed(INetworkManager manager) {
		SGWorldManager.instance.unregisterDimensions();
		SGWorldManager.instance = null;
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {}

}
