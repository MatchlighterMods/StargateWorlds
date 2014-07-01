package ml.sgworlds.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ml.core.item.DelegateItem;
import ml.sgworlds.CommonProxy;
import ml.sgworlds.SGWorlds;

public class DelegateAncientBook extends DelegateItem {

	public DelegateAncientBook() {
		setUnlocalizedName("book_translate");
		setIconString("SGWorlds:book_translate");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.openGui(SGWorlds.instance, CommonProxy.Windows.ANCIENT_LANGUAGE.ordinal(), par2World, 0, 0, 0);
		return super.onItemRightClick(stack, par2World, par3EntityPlayer);
	}
}
