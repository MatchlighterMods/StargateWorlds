package ml.sgworlds.api.world.feature.types;

import net.minecraft.world.gen.structure.StructureStart;

/**
 * Feature provides a structure to be generated.<br/>
 * SGWorlds has 3 structure generators: Underground, Aboveground, and Sky.<br/>
 * 2 StructureProvider registered to the same generator will not generate in the same chunk.<br/>
 * You can use this to create structures at random locations, or at exact locations.
 * 
 * @author Matchlighter
 */
public interface IStructureProvider {
	
	public boolean willProvideStructureFor(int chunkX, int chunkZ);

	public StructureStart getStructureStart(int chunkX, int chunkZ);
	
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
