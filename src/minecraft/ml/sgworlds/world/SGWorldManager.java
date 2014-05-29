package ml.sgworlds.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ml.core.util.RandomUtils;
import ml.sgworlds.Registry;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IDynamicWorldLoader;
import stargatetech2.api.stargate.IStargatePlacer;
import stargatetech2.api.stargate.Symbol;

public class SGWorldManager extends WorldSavedData implements IDynamicWorldLoader {
	public static final String FILE_NAME = "SGWorldsData";

	public static SGWorldManager instance;
	public final List<SGWorldData> worlds = new ArrayList<SGWorldData>();
	public final List<Integer> registeredDims = new ArrayList<Integer>();
	// public MapStorage worldDataStorage;
	
	private SGWorldManager() {
		super(FILE_NAME);
	}
	
	/** Constructor used when Loading from NBT */
	public SGWorldManager(String uid) {
		super(uid);
	}
	
	public SGWorldData getWorldData(Address address) {
		for (SGWorldData data : worlds) {
			if (data.getPrimaryAddress().equals(address)) {
				return data;
			}
		}
		return null;
	}
	
	public SGWorldData getWorldData(int dimId) {
		for (SGWorldData data : worlds) {
			if (data.getDimensionId() == dimId) {
				return data;
			}
		}
		return null;
	}
	
	public IWorldData getWorldData(String designation) {
		for (SGWorldData data : worlds) {
			if (data.getDesignation() == designation) {
				return data;
			}
		}
		return null;
	}
	
	public SGWorldData getClientWorldData(int dimId) {
		SGWorldData worldData = getWorldData(dimId);
		if (worldData == null) {
			worldData = SGWorldData.generateRandom(); // TODO Default Data?
			worlds.add(worldData);
			new PacketWorldData(dimId).dispatchToServer();
		}
		return worldData;
	}
	
	public Collection<SGWorldData> getSGWorlds() {
		return worlds;
	}

	public void registerSGWorld(SGWorldData worldData) {
		if (worlds.contains(worldData)) return;
		worlds.add(worldData);
		
		Address addr = worldData.getPrimaryAddress();
		Symbol[] pfx = new Symbol[3];
		for (int i=0; i<3; i++) {
			pfx[i] = addr.getSymbol(i);
		}
		StargateTechAPI.api().getStargateNetwork().reserveDimensionPrefix(this, pfx);
	}
	
	public void registerDimension(int dimId) {
		if (registeredDims.contains(dimId)) return;
		registeredDims.add(dimId);
		DimensionManager.registerDimension(dimId, Registry.config.worldProviderId);
	}
	
	public void registerDimensions() {
		for (SGWorldData data : worlds) {
			if (data.getDimensionId() != 0) {
				registerDimension(data.getDimensionId());
			}
		}
	}
	
	public void unregisterDimensions() {
		for (int dimId : registeredDims) {
			DimensionManager.unregisterDimension(dimId);
		}
		registeredDims.clear();
	}
	
	@Override
	public boolean willCreateWorldFor(Address address) {
		return getWorldData(address) != null;
	}

	@Override
	public void loadWorldFor(Address address, IStargatePlacer seedingShip) {
		SGWorldData data = getWorldData(address);
		if (data != null) {
			data.setDimensionId(DimensionManager.getNextFreeDimId());
			data.markDirty();
			
			DimensionManager.registerDimension(data.getDimensionId(), Registry.config.worldProviderId);
			registeredDims.add(data.getDimensionId());
			new PacketRegisterDimensions(data.getDimensionId()).dispatchToAll();
			
			WorldServer world = MinecraftServer.getServer().worldServerForDimension(data.getDimensionId());
			// TODO Place Stargate
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub
		
	}
	
	public static void loadData() {
		World overWorld = DimensionManager.getWorld(0);
		instance = (SGWorldManager)overWorld.loadItemData(SGWorldManager.class, FILE_NAME);
		if (instance != null) {
			// File dataDir = overWorld.getSaveHandler().getMapFileFromName("fake").getParentFile();
			// TODO Load WorldDatas
			// SGWorldManager.instance.registerSGWorld();
		} else {
			instance = new SGWorldManager();
			overWorld.setItemData(FILE_NAME, instance);
			instance.markDirty();
			
			int genCount = Registry.config.numberWorldsToGenerate + RandomUtils.randomInt(Registry.config.numberWorldsToGenerateRandom+1);
			List<SGWorldData> worlds = WorldDataGenerator.generateRandomWorlds(genCount);
			for (SGWorldData sgwd : worlds) {
				instance.registerSGWorld(sgwd);
				sgwd.registerForSave();
			}
			// TODO Load static worlds
		}
	}
}
