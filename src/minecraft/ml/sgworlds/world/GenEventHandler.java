package ml.sgworlds.world;

import ml.sgworlds.Registry;
import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.SGWFeatures;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import stargatetech2.api.world.EventWorldGen;
import stargatetech2.api.world.EventWorldGen.GenType;

public class GenEventHandler {

	@ForgeSubscribe
	public void onSGTechGen(EventWorldGen evt) {
		IWorldData worldData = SGWorldManager.instance.getWorldData(evt.world.provider.dimensionId); 
		if (worldData != null) {
			if (evt.type == GenType.STARGATE) evt.setResult(Result.DENY);
			if (evt.type == GenType.VEIN_NAQUADAH && !worldData.hasFeatureIdentifier(SGWFeatures.POPULATE_ORE_NAQUADAH.name()))
				evt.setResult(Result.DENY);
			
		} else if (evt.world.provider.dimensionId == 0 && evt.type == GenType.VEIN_NAQUADAH && Registry.config.preventOverworldNaquadahGen) {
			evt.setResult(Result.DENY);
		}
	}
}
