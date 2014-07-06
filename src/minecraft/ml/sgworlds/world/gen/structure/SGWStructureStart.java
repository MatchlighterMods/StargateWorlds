package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.world.gen.structure.SGWStructureComponent.SGWInitialComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public abstract class SGWStructureStart extends StructureStart {

	public SGWStructureStart() {}
	
	public SGWStructureStart(SGWInitialComponent initialComponent, World world, Random rnd, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
		
		this.components.add(initialComponent);
		initialComponent.buildComponent(initialComponent, this.components, rnd);
		
		while (!initialComponent.unbuiltComponents.isEmpty()) {
			//int i = rnd.nextInt(initialComponent.unbuiltComponents.size());
			SGWStructureComponent nextComponent = initialComponent.unbuiltComponents.remove(0);
			nextComponent.buildComponent(initialComponent, this.components, rnd);
			
			if (this.components.contains(nextComponent)) this.components.add(nextComponent);
		}
		
		updateBoundingBox();
	}
	
}
