package ml.sgworlds.block;

import java.util.ArrayList;

import ml.core.block.Delegate;
import ml.sgworlds.block.tile.TileEntityEngraved;
import ml.sgworlds.block.tile.TileEntityFacade;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DelegateEngraved extends Delegate {

	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		return ((TileEntityFacade)te).getIconFromSide(side);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		TileEntityEngraved tee = new TileEntityEngraved();
		return tee;
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		super.onBlockPlacedBy(world, x, y, z, par5EntityLivingBase, par6ItemStack);
		TileEntityEngraved te = (TileEntityEngraved)world.getBlockTileEntity(x, y, z);
		te.setBlockSide(-1, Block.sandStone, 2);
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> alit = new ArrayList<ItemStack>();
		return alit;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "engraved";
	}
}
