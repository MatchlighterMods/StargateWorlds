package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.world.gen.structure.city.CityComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class ScatteredStructureStart extends StructureStart {

	public ScatteredStructureStart() {}
	
	public ScatteredStructureStart(World world, Random rand, int chunkX, int chunkZ, StructureComponent structComponent) {
		this.components.add(structComponent);
		structComponent.buildComponent(structComponent, components, rand);
	}
	
	public static void registerComponent(Class<? extends CityComponent> componentClass, String ident) {
		MapGenStructureIO.func_143031_a(componentClass, "Scat_" + ident);
	}

	static {
		MapGenStructureIO.func_143034_b(ScatteredStructureStart.class, "ScatStart");
		
	}
}
