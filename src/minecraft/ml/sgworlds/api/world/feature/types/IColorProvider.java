package ml.sgworlds.api.world.feature.types;

import net.minecraft.util.Vec3;


public interface IColorProvider {

	public Vec3 getColor(float partialTicks);
	
}
