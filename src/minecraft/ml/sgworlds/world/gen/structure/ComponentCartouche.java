package ml.sgworlds.world.gen.structure;

import java.util.Random;

import ml.sgworlds.Registry;
import ml.sgworlds.block.tile.TileEntityEngraved;
import ml.sgworlds.world.gen.StructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentCartouche extends SGWStructrueComponent {
	
	private boolean generatedChest1;
	
	public ComponentCartouche() {}
	
	public ComponentCartouche(ChunkPosition position, int rotation) {
		super(position, rotation);
		this.boundingBox = createBoundingBox(8, 5, 5, 5, 6, 0);
	}
	
	@Override
	protected void save(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.save(tag);
	}
	
	@Override
	protected void load(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		super.load(tag);
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox chunkBox) {
		StructureBuilder b = new StructureBuilder(world, position, rotation);
		b.setMinMax(chunkBox);
		
		b.fillArea(-3, 1,-6, 3, 5, 4, null, 0);
		
		// Floor
		b.fillArea(-3, 0, -6, 3, 0, 5, Block.sandStone, 0);
		b.fillArea(-4, 0, -5, 4, 0, 4, Block.sandStone, 0);
		
		// Entrance
		b.fillArea(-3, 1, 5, 3, 4, 5, Block.sandStone, 2);
		b.fillArea(-1, 1, 5, 1, 3, 5, null, 0);
		
		// Pillars
		b.xSymmetry = true;
		b.fillArea(5, 0, -3, 5, 3, 3, Block.sandStone, 2);
		b.fillArea(4, 4, -4, 4, 4, 4, Block.sandStone, 2);
		
		for (int z=-3; z<=3; z+=2) {
			for (int y=1; y<=3; y++) {
				b.setBlockAt(4, y, z, null, 0);
			}
			b.setBlockAt(5, 2, z, Block.sandStone, 1);
			b.setBlockAt(4, 3, z, Block.torchWood, 0);
		}
		
		for (int z=-4; z<=4; z+=2) {
			for (int y=1; y<=3; y++) {
				b.setBlockAt(4, y, z, Block.sandStone, 2);
			}
		}
		
		// Walls
		b.setBlockAt(3, 4, 4, Block.sandStone, 2);
		b.fillArea(3, 1, -5, 3, 5, -5, Block.sandStone, 2);
		b.fillArea(3, 5, -5, 3, 5, 3, Block.sandStone, 2);
		b.setBlockAt(2, 5, 3, Block.sandStone, 2);
		b.setBlockAt(2, 5,-5, Block.sandStone, 2);
		b.setBlockAt(2, 4,-6, Block.sandStone, 2);
		b.setBlockAt(2, 5,-6, Block.sandStone, 2);
		
		b.xSymmetry = false;
		
		b.fillArea(-2, 5, 4, 2, 5, 4, Block.sandStone, 2);
		b.fillArea(-1, 5,-7, 1, 5,-6, Block.sandStone, 2);
		b.fillArea(-2, 6,-6, 2, 6, 3, Block.sandStone, 2);
		
		b.xSymmetry = true;
		b.setBlockAt(2, 6, 3, null, 0);
		b.setBlockAt(2, 6,-6, null, 0);
		
		b.xSymmetry = false;
		
		// Altar
		b.fillArea(-1, 1, -2, 1, 1, -1, Block.sandStone, 1);
		
		// Engravings MaxLength=240 // TODO
		String text = crapPadTo(" Proclarush Taonas At ", 240);
		int i = 0;
		
		for (int y=4; y>=1; y--) {
			TileEntityEngraved tee;
			if (y<=3) {
				tee = putEngraved(world, b.getAbsCoords(-2, y,-6));
				tee.setString(3, getBlockText(text, i++));
				tee.setString(5, getBlockText(text, i++));
			}
			
			for (int x=-1; x<=1; x++) {
				tee = putEngraved(world, b.getAbsCoords(x, y, -7));
				tee.setString(3, getBlockText(text, i++));
			}
			
			if (y<=3) {
				tee = putEngraved(world, b.getAbsCoords( 2, y,-6));
				tee.setString(4, getBlockText(text, i++));
				tee.setString(3, getBlockText(text, i++));
			}
		}
		
		return true;
	}
	
	private Random crnd = new Random();
	private String crap = "abcdefghijklmnopqrstuvwxyz    ";
	protected String crapPadTo(String original, int minLength) {
		int beginCrap = minLength / 2, endCrap = (minLength+1) / 2;
		for (int i=0; i<beginCrap; i++) {
			original = crap.charAt(crnd.nextInt(crap.length())) + original;
		}
		for (int i=0; i<endCrap; i++) {
			original = original + crap.charAt(crnd.nextInt(crap.length()));
		}
		return original;
	}
	
	private int[] lines = {3,7,7,7};
	protected String getBlockText(String source, int blk) {
		int lnWidth=blk;
		int add = 0;
		for (int ln : lines) {
			lnWidth -= ln;
			if (lnWidth < 0) {
				lnWidth = ln;
				break;
			}
			add += ln;
		}
		int ln1 = (blk+add) * 5;
		int ln2 = ln1 + (lnWidth*5);
		return source.substring(ln1, ln1+5)+"\n"+source.substring(ln2, ln2+5);
	}

	protected TileEntityEngraved putEngraved(World world, ChunkCoordinates abs) {
		Registry.delegateEngraved.setBlockAt(world, abs.posX, abs.posY, abs.posZ, 3);
		TileEntityEngraved tee = (TileEntityEngraved) world.getBlockTileEntity(abs.posX, abs.posY, abs.posZ);
		tee.setBlockSide(-1, Block.sandStone, 2);
		tee.rotation = rotation;
		return tee;
	}
}
