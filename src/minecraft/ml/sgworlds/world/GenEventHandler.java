package ml.sgworlds.world;

import java.util.ArrayList;
import java.util.List;

import ml.sgworlds.Registry;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.SGWFeature;
import ml.sgworlds.world.dimension.SGWorldProvider;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import stargatetech2.api.world.EventWorldGen;
import stargatetech2.api.world.EventWorldGen.GenType;

public class GenEventHandler {

	@ForgeSubscribe
	public void onSGTechGen(EventWorldGen evt) {
		IWorldData worldData = SGWorldManager.instance.getWorldData(evt.world.provider.dimensionId); 
		if (worldData != null) {
			if (evt.type == GenType.STARGATE) evt.setResult(Result.DENY);
			if (evt.type == GenType.VEIN_NAQUADAH && !worldData.hasFeatureIdentifier(SGWFeature.POPULATE_ORE_NAQUADAH.name()))
				evt.setResult(Result.DENY);
			
		} else if (evt.world.provider.dimensionId == 0 && evt.type == GenType.VEIN_NAQUADAH && Registry.config.preventOverworldNaquadahGen) {
			evt.setResult(Result.DENY);
		}
	}
	
	public static ChunkCoordIntPair gateChunkCoords;
	public static final List<EventType> decorateBlacklist = new ArrayList<DecorateBiomeEvent.Decorate.EventType>();
	static {
		decorateBlacklist.add(EventType.TREE);
	}
	
	@ForgeSubscribe
	public void onBiomeDecorate(DecorateBiomeEvent.Decorate evt) {
		if (evt.world.provider instanceof SGWorldProvider) {
			if (gateChunkCoords != null && (Math.pow(evt.chunkX-(gateChunkCoords.chunkXPos), 2)+Math.pow(evt.chunkZ-(gateChunkCoords.chunkZPos), 2)) <= 9) {
				if (decorateBlacklist.contains(evt.type)) {
					evt.setResult(Result.DENY);
				}
			}
		}
	}
}
