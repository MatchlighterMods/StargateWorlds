package ml.sgworlds.world.gen.structure.deserthold;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.gen.structure.MapGenStructureIO;

import ml.sgworlds.world.gen.structure.WeightedComponent;

public class ManagerDesertHold {

	public static final List<WeightedComponent> holdRooms = new ArrayList<WeightedComponent>();
	public static final List<WeightedComponent> holdHalls = new ArrayList<WeightedComponent>();
	
	public static void registerRoomComponent(String id, Class<? extends ComponentDesertHold> cls, int weight) {
		MapGenStructureIO.func_143031_a(cls, id);
		holdRooms.add(new WeightedComponent(cls, weight));
	}
	
	public static void registerHallComponent(String id, Class<? extends ComponentDesertHold> cls, int weight) {
		MapGenStructureIO.func_143031_a(cls, id);
		holdHalls.add(new WeightedComponent(cls, weight));
	}
	
	static {
		registerHallComponent("DHHH", ComponentHallHub.class, 100);
		registerHallComponent("DHHSP", ComponentHallSP.class, 100);
		
		registerRoomComponent("DHMR", ComponentMonsterRoom.class, 100);
		registerRoomComponent("DHRT", ComponentRoomTrapped.class, 100);
		
		MapGenStructureIO.func_143031_a(ComponentPyramidCenter.class, "DHPC");
		
		MapGenStructureIO.func_143031_a(ComponentStartAbydos.class, "DHSA");
		MapGenStructureIO.func_143031_a(ComponentStartRings.class, "DHSR");
	}
}
