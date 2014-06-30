package ml.sgworlds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import ml.core.gui.MLGuiHandler;
import ml.core.gui.core.TopParentGuiElement;
import ml.sgworlds.window.WindowTablet;

public class CommonProxy extends MLGuiHandler {
	
	public enum Windows {
		ANCIENT_TABLET,
	}

	public void load() {

	}

	@Override
	public TopParentGuiElement getTopElement(int ID, EntityPlayer player, World world, int x, int y, int z, Side side) {
		switch (Windows.values()[ID]) {
		case ANCIENT_TABLET:
			return new WindowTablet(player, side, player.getHeldItem());
		}
		return null;
	}
}
