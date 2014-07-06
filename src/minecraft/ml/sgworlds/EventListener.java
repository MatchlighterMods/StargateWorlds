package ml.sgworlds;

import java.util.Random;

import ml.core.util.RandomUtils;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import ml.sgworlds.world.gen.structure.SGWStructureComponent;
import ml.sgworlds.world.gen.structure.deserthold.ComponentStartAbydos;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import stargatetech2.api.stargate.StargateEvent;

public class EventListener {

	@ForgeSubscribe
	public void getBlockHardness(PlayerEvent.BreakSpeed evt) {
		
	}
	
	@ForgeSubscribe
	public void gateWrenched(StargateEvent.StargateWrenched evt) {
		// Make gates created by SGWorlds unbreakable.
		if (evt.world.provider instanceof SGWorldProvider &&
				SGWorldManager.instance.getWorldData(evt.address) != null) evt.setCanceled(true);
	}
	
//	private IGateTempleGenerator tgen = new TemplePyramid();
//	@ForgeSubscribe
//	public void onPopulate(PopulateChunkEvent.Pre evt) {
//		if (evt.chunkX % 4 == 0 && evt.chunkZ % 4 == 0 && tgen != null) {
//			tgen.generateGateTemple(evt.world, new ChunkPosition(evt.chunkX << 4, 150, evt.chunkZ << 4), RandomUtils.randomInt(4));
//		}
//	}
	
	@ForgeSubscribe
	public void onPopulate(PopulateChunkEvent.Pre evt) {
		if (evt.chunkX % 4 == 0 && evt.chunkZ % 4 == 0) {
			SGWStructureComponent cc = new ComponentStartAbydos(new ChunkCoordinates(evt.chunkX << 4, 100, evt.chunkZ << 4), RandomUtils.randomInt(4));
			cc.addComponentParts(evt.world, new Random(), null);
		}
	}
}
