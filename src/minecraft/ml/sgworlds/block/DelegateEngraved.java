package ml.sgworlds.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.block.DelegateBlock;
import ml.sgworlds.block.tile.TileEntityEngraved;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class DelegateEngraved extends DelegateBlock {

	@SideOnly(Side.CLIENT)
	public Icon[] icons = new Icon[2];
	
	@Override
	public int getMetaLength() {
		return 2;
	}
	
	@Override
	public Icon getIcon(int side, int meta) {
		return icons[meta];
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		TileEntityEngraved tee = new TileEntityEngraved();
		return tee;
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return false;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "engraved";
	}
	
	@Override
	public void registerIcons(IconRegister ireg) {
		icons[0] = ireg.registerIcon("SGWorlds:engraved_sandstone_row");
		icons[1] = ireg.registerIcon("SGWorlds:engraved_sandstone_column");
	}
	
}
