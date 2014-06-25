package ml.sgworlds.world.feature.impl.atmosphere;

import ml.sgworlds.api.world.IWorldData;
import ml.sgworlds.api.world.feature.FeatureProvider;
import ml.sgworlds.api.world.feature.WorldFeature;
import ml.sgworlds.api.world.feature.types.ISkyColor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;

public class SkyColorNormal extends WorldFeature implements ISkyColor {

	public SkyColorNormal(FeatureProvider provider, IWorldData worldData) {
		super(provider, worldData);
	}

	@Override
	public Vec3 getSkyColor(Entity viewer, float celestialAngle, float partialTicks) {
		float f2 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

		if (f2 < 0.0F) {
			f2 = 0.0F;
		}

		if (f2 > 1.0F) {
			f2 = 1.0F;
		}

		int colour = ForgeHooksClient.getSkyBlendColour(worldData.getWorldProvider().worldObj, MathHelper.floor_double(viewer.posX), MathHelper.floor_double(viewer.posZ));

		float colorRed = (float)(colour >> 16 & 255) / 255.0F;
		float colorGreen = (float)(colour >> 8 & 255) / 255.0F;
		float colorBlue = (float)(colour & 255) / 255.0F;
		colorRed *= f2;
		colorGreen *= f2;
		colorBlue *= f2;
		
		return Vec3.createVectorHelper(colorRed, colorGreen, colorBlue);
	}

	@Override
	public void writeNBTData(NBTTagCompound tag) {}

}
