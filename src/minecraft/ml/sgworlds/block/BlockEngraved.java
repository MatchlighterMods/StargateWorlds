package ml.sgworlds.block;

import ml.sgworlds.Registry;
import ml.sgworlds.block.tile.TileEntityFacade;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEngraved extends BlockContainer {

	public BlockEngraved() {
		super(Registry.config.engravedBlockId, Material.rock);
	}

	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		return ((TileEntityFacade)te).getIconFromSide(side);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return null;
	}

}
