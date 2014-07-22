package ml.sgworlds.world.gen.structure.deserthold;

import java.util.ArrayList;
import java.util.List;

import ml.core.world.structure.WeightedComponent;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class DesertHoldComponents {

	public static void registerComponentClasses() {
		// Halls
		MapGenStructureIO.func_143031_a(ComponentHallHub.class,			"DHHH");
		MapGenStructureIO.func_143031_a(ComponentHallPassage.class,		"DHHSP");
		MapGenStructureIO.func_143031_a(ComponentHallStraight.class,	"DHHSt");
		MapGenStructureIO.func_143031_a(ComponentHallSimple.class,		"DHHSm");
		
		// Rooms
		MapGenStructureIO.func_143031_a(ComponentMonsterRoom.class,		"DHMR");
		MapGenStructureIO.func_143031_a(ComponentTrapDispenser.class,	"DHTD");
		MapGenStructureIO.func_143031_a(ComponentTrapSandPit.class,		"DHTSP");
		MapGenStructureIO.func_143031_a(ComponentTrapTNT.class,			"DHTTNT");
		
		// Starts
		MapGenStructureIO.func_143031_a(ComponentStartAbydos.class,		"DHSA");
		MapGenStructureIO.func_143031_a(ComponentStartRings.class,		"DHSR");

		// Misc
		MapGenStructureIO.func_143031_a(ComponentPyramidCenter.class,	"DHPC");
	}
	
	public static List<WeightedComponent> getHallWeights() {
		List<WeightedComponent> components = new ArrayList<WeightedComponent>();
		
		components.add(new WeightedComponent(ComponentHallHub.class, 100, 0));
		components.add(new WeightedComponent(ComponentHallSimple.class, 100, 0));
		components.add(new WeightedComponent(ComponentHallStraight.class, 100, 0));
		components.add(new WeightedComponent(ComponentHallPassage.class, 50, 0));
		
		return components;
	}
	
	public static List<WeightedComponent> getRoomWeights() {
		List<WeightedComponent> components = new ArrayList<WeightedComponent>();
		
		components.add(new WeightedComponent(ComponentMonsterRoom.class, 50, 0));
		components.add(new WeightedComponent(ComponentTrapDispenser.class, 100, 0));
		components.add(new WeightedComponent(ComponentTrapSandPit.class, 100, 0));
		components.add(new WeightedComponent(ComponentTrapTNT.class, 100, 0));
		
		return components;
	}
}
