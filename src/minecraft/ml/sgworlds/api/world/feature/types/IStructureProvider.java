package ml.sgworlds.api.world.feature.types;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureStart;

/**
 * Feature provides a structure to be generated.<br/>
 * SGWorlds has 3 common structure generators: Underground, Aboveground, and Sky.<br/>
 * 2 StructureProviders registered to the same generator will not generate in the same chunk.<br/>
 * You can use this to create structures at random locations, or at exact locations.<br/>
 * Make sure you register any Components/StructureStarts with {@link MapGenStructureIO}.
 * 
 * @author Matchlighter
 */
public interface IStructureProvider {
	
	public boolean willProvideStructureFor(World world, int chunkX, int chunkZ);

	public StructureStart getStructureStart(World world, Random rand, int chunkX, int chunkZ);
	
	/**
	 * Used to select which generator/strata the provider will generate in.
	 * @return An {@link StructureStrata} indicating where the structure generates.
	 */
	public StructureStrata getStrata();
	
	public static enum StructureStrata {
		Underground,
		Aboveground,
		Sky;
	}
}
