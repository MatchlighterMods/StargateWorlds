package ml.sgworlds.dimension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ml.core.util.RandomUtils;
import ml.sgworlds.Registry;
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
	private Map<Address, SGWorldData> worlds = new HashMap<Address, SGWorldData>();
	
	private SGWorldManager() {
		super(FILE_NAME);
	}
	
	/** Constructor used when Loading from NBT */
	public SGWorldManager(String uid) {
		super(uid);
	}

	public boolean addressRegistered(Address address) {
		return (worlds.containsKey(address));
	}
	
	public SGWorldData getWorldData(Address address) {
		return worlds.get(address);
	}
	
	public SGWorldData getWorldData(int dimId) {
		for (SGWorldData data : worlds.values()) {
			if (data.getDimensionId() == dimId) {
				return data;
			}
		}
		return null;
	}
	
	public SGWorldData getWorldData(String designation) {
		for (SGWorldData data : worlds.values()) {
			if (data.getDesignation() == designation) {
				return data;
			}
		}
		return null;
	}
	
	public Collection<SGWorldData> getSGWorlds() {
		return worlds.values();
	}

	public void registerSGWorld(SGWorldData worldData) {
		worlds.put(worldData.getPrimaryAddress(), worldData);
		
		Address addr = worldData.getPrimaryAddress();
		Symbol[] pfx = new Symbol[3];
		for (int i=0; i<3; i++) {
			pfx[i] = addr.getSymbol(i);
		}
		StargateTechAPI.api().getStargateNetwork().reserveDimensionPrefix(this, pfx);
	}
	
	public void registerDimensions() {
		for (SGWorldData data : worlds.values()) {
			if (data.getDimensionId() != 0) {
				DimensionManager.registerDimension(data.getDimensionId(), Registry.config.worldProviderId);
			}
		}
	}
	
	public void unregisterDimensions() {
		for (SGWorldData data : worlds.values()) {
			if (data.getDimensionId() != 0) {
				DimensionManager.unregisterDimension(data.getDimensionId());
			}
		}
	}
	
	@Override
	public boolean willCreateWorldFor(Address address) {
		return addressRegistered(address);
	}

	@Override
	public void loadWorldFor(Address address, IStargatePlacer seedingShip) {
		SGWorldData data = worlds.get(address);
		if (data != null) {
			data.setDimensionId(DimensionManager.getNextFreeDimId());
			data.markDirty();
			
			DimensionManager.registerDimension(data.getDimensionId(), Registry.config.worldProviderId);
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
			// TODO Load WorldDatas
		} else {
			instance = new SGWorldManager();
			overWorld.setItemData(FILE_NAME, instance);
			instance.markDirty();
			for (int i=0; i < (Registry.config.numberWorldsToGenerate + RandomUtils.randomInt(Registry.config.numberWorldsToGenerateRandom+1)); i++) {
				SGWorldData ndata = WorldDataGenerator.instance.generateRandomWorld();
				overWorld.setItemData(ndata.getUID(), ndata);
				ndata.markDirty();
				instance.registerSGWorld(ndata);
			}
		}
	}
}
