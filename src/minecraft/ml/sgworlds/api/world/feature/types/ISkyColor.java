package ml.sgworlds.api.world.feature.types;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public interface ISkyColor {
	
	public Vec3 getSkyColor(Entity viewer, float celestialAngle, float partialTicks);

}
