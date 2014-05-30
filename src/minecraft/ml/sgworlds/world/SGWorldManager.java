package ml.sgworlds.world;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ml.core.util.RandomUtils;
import ml.sgworlds.Registry;
import ml.sgworlds.SGWorlds;
import ml.sgworlds.api.world.IStaticWorld;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IDynamicWorldLoader;
import stargatetech2.api.stargate.IStargatePlacer;
import stargatetech2.api.stargate.Symbol;
import cpw.mods.fml.common.FMLLog;

public class SGWorldManager implements IDynamicWorldLoader {
	public static final String FILE_NAME = "SGWorldsData";

	public static SGWorldManager instance;
	public static final List<IStaticWorld> staticWorlds = new ArrayList<IStaticWorld>();
	public final List<SGWorldData> worlds = new ArrayList<SGWorldData>();
	public final List<Integer> registeredDims = new ArrayList<Integer>();
	
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
	
	public void addClientData(SGWorldData worldData) {
		if (worlds.contains(worldData)) return;
		worlds.add(worldData);
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
	
	public void saveData() {
		File wmFile = SGWorlds.getSaveFile("SGWorlds");
		for (SGWorldData worldData : worlds) {
			worldData.saveData();
		}
		
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(wmFile);
			fileoutputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadData() {
		File wmFile = SGWorlds.getSaveFile("SGWorlds");
		File wdDir = SGWorlds.getSaveFile("SGWorlds/fake").getParentFile();
		wdDir.mkdirs();
		
		instance = new SGWorldManager();
		
		if (!wmFile.exists()) {
			int genCount = Registry.config.numberWorldsToGenerate + RandomUtils.randomInt(Registry.config.numberWorldsToGenerateRandom+1);
			List<SGWorldData> worlds = WorldDataGenerator.generateRandomWorlds(genCount);
			
			for (IStaticWorld staticWorld : instance.staticWorlds) {
				worlds.add(SGWorldData.fromStaticWorld(staticWorld));
			}
			
			for (SGWorldData sgwd : worlds) {
				instance.registerSGWorld(sgwd);
			}
			
		} else {
			for (String name : wdDir.list()) {
				if (name.startsWith("world_") && name.endsWith(".dat")) {
					String designation = name.substring(6, name.length()-4);
					
					try {
						SGWorldData worldData = SGWorldData.loadData(designation);
						instance.registerSGWorld(worldData);
					} catch (Exception e) {
						FMLLog.severe("The world \"%s\" could not be loaded! (%s)", designation, e.getMessage());
						//e.printStackTrace();
					}
				}
			}
		}
		
	}
}
