package ml.sgworlds.world.cities;

import net.minecraft.block.Block;
import net.minecraft.util.Tuple;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BlockAndMeta extends Tuple {

	public BlockAndMeta(Block block, int meta) {
		super(block, meta);
	}

	public Block get() {
		return Block.class.cast(getFirst());
	}

	public int getMeta() {
		return Integer.class.cast(getSecond());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		return obj instanceof BlockAndMeta && this.getMeta() == ((BlockAndMeta) obj).getMeta() && this.get() == ((BlockAndMeta) obj).get();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getMeta()).append(get()).toHashCode();
	}

	public Block toStair() {
		if (get() == Block.cobblestone || get() == Block.cobblestoneMossy) {
			return Block.stairsCobblestone;
		} else if (get() == Block.netherBrick) {
			return Block.stairsNetherBrick;
		} else if (get() == Block.stoneBrick || get() == Block.stone) {
			return Block.stairsStoneBrick;
		} else if (get() == Block.brick) {
			return Block.stairsBrick;
		} else if (get() == Block.sandStone) {
			return Block.stairsSandStone;
		} else if (get() == Block.blockNetherQuartz) {
			return Block.stairsNetherQuartz;
		} else if (get() == Block.planks) {
			int tempdata = getMeta();
			switch (tempdata) {
			case 0:
				return Block.stairsWoodOak;
			case 1:
				return Block.stairsWoodSpruce;
			case 2:
				return Block.stairsWoodBirch;
			case 3:
				return Block.stairsWoodJungle;
				// case 4:
				// return Block.acacia_stairs;
				// case 5:
				// return Block.dark_oak_stairs;
			}
		}
		return get();
	}

	public BlockAndMeta toStep() {
		if (!BlockProperties.get(get()).isArtificial)
			return this;
		if (get() == Block.sandStone) {
			return new BlockAndMeta(Block.stoneSingleSlab, 1);
		} else if (get() == Block.planks) {
			return new BlockAndMeta(Block.stoneSingleSlab, 2);
		} else if (get() == Block.cobblestone) {
			return new BlockAndMeta(Block.stoneSingleSlab, 3);
		} else if (get() == Block.brick) {
			return new BlockAndMeta(Block.stoneSingleSlab, 4);
		} else if (get() == Block.stoneBrick) {
			return new BlockAndMeta(Block.stoneSingleSlab, 5);
		} else if (get() == Block.netherBrick) {
			return new BlockAndMeta(Block.stoneSingleSlab, 6);
		} else if (get() == Block.blockNetherQuartz) {
			return new BlockAndMeta(Block.stoneSingleSlab, 7);

		} else if (get() == Block.stoneSingleSlab || get() == Block.stoneDoubleSlab) {
			return new BlockAndMeta(Block.stoneSingleSlab, getMeta());
		} else if (get() == Block.woodDoubleSlab || get() == Block.woodSingleSlab) {
			return new BlockAndMeta(Block.woodSingleSlab, getMeta());
		} else {
			return new BlockAndMeta(get(), 0);
		}
	}

	public BlockAndMeta stairToSolid() {
		Block block = get();
		int meta = 0;
		if (block == Block.stairsCobblestone) {
			block = Block.cobblestone;
		} else if (block == Block.stairsWoodOak) {
			block = Block.planks;
		} else if (block == Block.stairsWoodSpruce) {
			block = Block.planks;
			meta = 1;
		} else if (block == Block.stairsWoodBirch) {
			block = Block.planks;
			meta = 2;
		} else if (block == Block.stairsWoodJungle) {
			block = Block.planks;
			meta = 3;
			// }else if(block==Block.acacia_stairs) {
			// block = Block.planks;
			// meta = 4;
			// }else if(block==Block.dark_oak_stairs) {
			// block = Block.planks;
			// meta = 5;
		} else if (block == Block.stairsBrick) {
			block = Block.brick;
		} else if (block == Block.stairsStoneBrick) {
			block = Block.stoneBrick;
		} else if (block == Block.stairsNetherBrick) {
			block = Block.netherBrick;
		} else if (block == Block.stairsSandStone) {
			block = Block.sandStone;
		} else if (block == Block.stairsNetherQuartz) {
			block = Block.blockNetherQuartz;
		}
		return new BlockAndMeta(block, meta);
	}
}
