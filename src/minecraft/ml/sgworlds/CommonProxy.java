package ml.sgworlds;

import ml.core.gui.MLGuiHandler;
import ml.core.gui.core.TopParentGuiElement;
import ml.sgworlds.window.WindowBookJournal;
import ml.sgworlds.window.WindowTablet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy extends MLGuiHandler {
	
	public enum Windows {
		ANCIENT_TABLET,
		PERSONAL_JOURNAL,
	}

	public void load() {

	}

	@Override
	public TopParentGuiElement getTopElement(int ID, EntityPlayer player, World world, int x, int y, int z, Side side) {
		switch (Windows.values()[ID]) {
		case ANCIENT_TABLET:
			return new WindowTablet(player, side, player.getHeldItem());
		case PERSONAL_JOURNAL:
			return new WindowBookJournal(player, side);
		}
		return null;
	}
	
}
