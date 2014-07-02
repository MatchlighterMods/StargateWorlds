package ml.sgworlds.world;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ml.core.util.RandomUtils;
import ml.sgworlds.Registry;
import ml.sgworlds.SGWorlds;
import ml.sgworlds.api.world.IGateTempleGenerator;
import ml.sgworlds.api.world.IStaticWorld;
import ml.sgworlds.network.packet.PacketRegisterDimensions;
import ml.sgworlds.network.packet.PacketWorldData;
import ml.sgworlds.world.gen.temples.TemplePlain;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import stargatetech2.api.StargateTechAPI;
import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IDynamicWorldLoader;
import stargatetech2.api.stargate.IStargatePlacer;
import stargatetech2.api.stargate.Symbol;
import cpw.mods.fml.common.FMLLog;

public class SGWorldManager implements IDynamicWorldLoader {

	public static SGWorldManager instance;
	public static final Set<IStaticWorld> staticWorlds = new HashSet<IStaticWorld>();
	public static final List<IGateTempleGenerator> templeGens = new ArrayList<IGateTempleGenerator>();
	public final List<SGWorldData> worlds = new ArrayList<SGWorldData>();
	public final List<Integer> registeredDims = new ArrayList<Integer>();
	
	private Random rand = new Random();
	
	public SGWorldData getWorldData(Address address) {
		for (SGWorldData data : worlds) {
			if (data.getPrimaryAddress().equals(address)) {
				return data;
			}
		}
		return null;
	}
	
	public SGWorldData getWorldData(int dimId) {
		if (dimId == 0) return null;
		for (SGWorldData data : worlds) {
			if (data.getDimensionId() == dimId) {
				return data;
			}
		}
		return null;
	}
	
	public SGWorldData getWorldData(String designation) {
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
			worldData = new SGWorldData("CLIENT", null); // TODO Default Data?
			worldData.fillFeatures(true);
			worldData.setDimensionId(dimId);
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
				
				File saveDir = DimensionManager.getCurrentSaveRootDirectory();
				saveDir = new File(saveDir, data.getSaveFolderName());
				ForgeChunkManager.savedWorldHasForcedChunkTickets(saveDir);
				// TODO
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
		SGWorldData worldData = getWorldData(address);
		try {
			System.out.println(worldData);
			if (worldData != null) {
				worldData.setDimensionId(DimensionManager.getNextFreeDimId());
				worldData.markDirty();
				
				DimensionManager.registerDimension(worldData.getDimensionId(), Registry.config.worldProviderId);
				registeredDims.add(worldData.getDimensionId());
				new PacketRegisterDimensions(worldData.getDimensionId()).dispatchToAll();
				
				WorldServer world = MinecraftServer.getServer().worldServerForDimension(worldData.getDimensionId());
				
				// Gate Spawning
				IGateTempleGenerator templeGenerator = null;
				
				for (IStaticWorld sWorld : staticWorlds) {
					if (address.equals(sWorld.getAddress())) {
						templeGenerator = sWorld.getTempleGenerator(world);
						break;
					}
				}
				
				if (templeGenerator == null) {
					Collections.shuffle(templeGens);
					for (IGateTempleGenerator temple : templeGens) {
						if (temple.getGateCoords(world, 0) != null) {
							templeGenerator = temple;
							break;
						}
					}
				}
				
				if (templeGenerator == null) templeGenerator = new TemplePlain();
				
				int gateRotation = 0;
				ChunkPosition gateCoords = templeGenerator.getGateCoords(world, gateRotation);
				ChunkCoordIntPair gateChunkCoords = new ChunkCoordIntPair(gateCoords.x >> 4, gateCoords.z >> 4);
				GenEventHandler.gateChunkCoords = gateChunkCoords;
				for (int cX=-5; cX<=5; cX++) {
					for (int cZ=-5; cZ<=5; cZ++) {
						world.getChunkFromChunkCoords(cX+gateChunkCoords.chunkXPos, cZ+gateChunkCoords.chunkZPos);
					}	
				}

				gateCoords = templeGenerator.getGateCoords(world, gateRotation);
				templeGenerator.generateGateTemple(world, gateCoords, gateRotation);
					
				if (gateCoords == null) gateCoords = new ChunkPosition(0, 200, 0); // This code should NEVER be run.
				boolean nsr = gateRotation==0 || gateRotation==2;
				for (int y=0; y<5; y++) {
					for (int x=-2; x<=2; x++) {
						world.setBlock(gateCoords.x + (nsr ? x : 0), gateCoords.y + y, gateCoords.z + (nsr ? 0 : x), 0);
					}
				}
				seedingShip.placeStargate(world, gateCoords.x, gateCoords.y, gateCoords.z, gateRotation);
				
				worldData.setGateLocation(gateCoords);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			
			for (int i=0; i < genCount; i++) {
				instance.registerSGWorld(SGWorldData.generateRandom());
			}
			
			for (IStaticWorld staticWorld : instance.staticWorlds) {
				instance.registerSGWorld(SGWorldData.fromStaticWorld(staticWorld));
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
