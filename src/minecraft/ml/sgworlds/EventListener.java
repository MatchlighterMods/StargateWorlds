package ml.sgworlds;

import java.util.LinkedList;
import java.util.Random;

import ml.core.world.structure.MLStructureComponent;
import ml.sgworlds.world.SGWorldManager;
import ml.sgworlds.world.dimension.SGWorldProvider;
import ml.sgworlds.world.gen.structure.deserthold.ComponentStartAbydos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
	
	@ForgeSubscribe
	public void entityJoinedWorld(EntityJoinWorldEvent evt) {
		if (evt.entity instanceof EntityPlayer && !evt.world.isRemote) {
			if (evt.world.provider instanceof SGWorldProvider) {
				SGWorldProvider sgwp = (SGWorldProvider)evt.world.provider;
				EntityPlayer epl = (EntityPlayer)evt.entity;
				SGWPlayerData pld = SGWPlayerData.getPlayerData(epl.username);
				if (pld.discoveredWorlds.add(sgwp.getWorldData().getDesignation())) pld.markDirty();
			}
		}
	}
	
//	private IGateTempleGenerator tgen = new TemplePyramid();
//	@ForgeSubscribe
//	public void onPopulate(PopulateChunkEvent.Pre evt) {
//		if (evt.chunkX % 4 == 0 && evt.chunkZ % 4 == 0 && tgen != null) {
//			tgen.generateGateTemple(evt.world, new ChunkPosition(evt.chunkX << 4, 150, evt.chunkZ << 4), RandomUtils.randomInt(4));
//		}
//	}
	
//	@ForgeSubscribe
//	public void onPopulate(PopulateChunkEvent.Pre evt) {
//		if (evt.chunkX % 4 == 0 && evt.chunkZ % 4 == 0) {
//			SGWStructureComponent cc = new ComponentStartAbydos(new ChunkCoordinates(evt.chunkX << 4, 100, evt.chunkZ << 4), RandomUtils.randomInt(4));
//			cc.addComponentParts(evt.world, new Random(), null);
//		}
//	}
	
	@ForgeSubscribe
	public void onPopulate(PopulateChunkEvent.Pre evt) {
		if (evt.chunkX == 0 && evt.chunkZ == 0) {
			Random rnd = new Random();
			ComponentStartAbydos csa = new ComponentStartAbydos(new ChunkCoordinates(0, 80, 0), 0);
			//csa.constructComponent(null, 0, new ChunkCoordinates(0, 80, 0), rnd);
			
			LinkedList<MLStructureComponent> components = new LinkedList();
			components.add(csa);
			
			csa.buildComponent(csa, components, rnd);
			while (!csa.unbuiltComponents.isEmpty()) {
				//int i = rnd.nextInt(initialComponent.unbuiltComponents.size());
				MLStructureComponent nextComponent = csa.unbuiltComponents.remove(0);
				nextComponent.buildComponent(csa, components, rnd);
				
				if (components.contains(nextComponent)) components.add(nextComponent);
			}
			
			for (MLStructureComponent c : components) {
				c.addComponentParts(evt.world, rnd, c.getBoundingBox());
			}
		}
	}
}
