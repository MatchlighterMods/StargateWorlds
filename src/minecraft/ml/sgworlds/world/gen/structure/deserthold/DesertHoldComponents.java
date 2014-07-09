package ml.sgworlds.world.gen.structure.deserthold;

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
	
}
