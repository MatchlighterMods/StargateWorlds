package ml.sgworlds.dimension;

import java.util.Map;

import stargatetech2.api.stargate.Address;
import stargatetech2.api.stargate.IDynamicWorldLoader;
import stargatetech2.api.stargate.IStargatePlacer;

public class SGWorldsList implements IDynamicWorldLoader {

	private Map<Address, SGWorldData> worlds;
	
	public boolean addressRegistered(Address address) {
		return (worlds.containsKey(address));
	}
	
	public SGWorldData getWorldData(Address address) {
		return worlds.get(address);
	}
	
	public void save() {
		// TODO
	}
	
	public void load() {
		// TODO
	}

	@Override
	public boolean willCreateWorldFor(Address address) {
		return addressRegistered(address);
	}

	@Override
	public void loadWorldFor(Address address, IStargatePlacer seedingShip) {
		// TODO Auto-generated method stub
		
	}
}
