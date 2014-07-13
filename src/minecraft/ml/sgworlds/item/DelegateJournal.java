package ml.sgworlds.item;

import java.util.List;

import ml.core.item.DelegateItem;
import ml.core.item.StackUtils;
import ml.sgworlds.CommonProxy;
import ml.sgworlds.SGWorlds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
		
		if (!isCreativeSpawned(stack) && getOwnerName(stack) == null) {
			StackUtils.setTag(stack, par3EntityPlayer.username, "owner");
		}
		par3EntityPlayer.openGui(SGWorlds.instance, CommonProxy.Windows.PERSONAL_JOURNAL.ordinal(), par2World, 0, 0, 0);
		return super.onItemRightClick(stack, par2World, par3EntityPlayer);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (isCreativeSpawned(stack)) {
			par3List.add(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString() + "Creative Spawned");
		} else {
			par3List.add(EnumChatFormatting.GRAY.toString() + "Author: " + EnumChatFormatting.ITALIC.toString() + StackUtils.getTag(stack, "None", "owner"));
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack stack, int pass) {
		return isCreativeSpawned(stack);
	}
	
	@Override
	public void addCreativeStacks(CreativeTabs ctab, List stackList) {
		super.addCreativeStacks(ctab, stackList);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("creativeSpawned", true);
		stackList.add(StackUtils.create(parent(), 1, getMetaId(), tag));
	}
	
	public static boolean isCreativeSpawned(ItemStack is) {
		return StackUtils.getTag(is, false, "creativeSpawned");
	}
	
	public static String getOwnerName(ItemStack is) {
		return StackUtils.getTag(is, (String)null, "owner");
	}
}
