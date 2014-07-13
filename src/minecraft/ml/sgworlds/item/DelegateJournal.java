package ml.sgworlds.item;

import java.util.List;

import ml.core.item.DelegateItem;
import ml.core.item.StackUtils;
import ml.sgworlds.CommonProxy;
import ml.sgworlds.SGWorlds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class DelegateJournal extends DelegateItem {

	public DelegateJournal() {
		setUnlocalizedName("journal");
		setIconString("SGWorlds:book_journal");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par2World.isRemote) return stack;
		
		if (StackUtils.getTag(stack, (String)null, "owner") == null) {
			StackUtils.setTag(stack, par3EntityPlayer.username, "owner");
		}
		par3EntityPlayer.openGui(SGWorlds.instance, CommonProxy.Windows.PERSONAL_JOURNAL.ordinal(), par2World, 0, 0, 0);
		return super.onItemRightClick(stack, par2World, par3EntityPlayer);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(EnumChatFormatting.GRAY.toString() + "Author: " + EnumChatFormatting.ITALIC.toString() + StackUtils.getTag(stack, "None", "owner"));
	}
}
