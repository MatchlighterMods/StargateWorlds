package ml.sgworlds.world.cities;

import java.util.HashMap;
import java.util.Map;

import ml.sgworlds.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockGlowStone;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockMushroomCap;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

/**
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class BlockProperties {
	private final static Map<Block, BlockProperties> props = new HashMap<Block, BlockProperties>();
	/**
	 * All the studied block properties
	 */
	public final boolean isWater, isStair, isDoor, isTree, isFlowing, isWallable, isOre, isGround, isArtificial, isLight, isLoaded, isDelayed;

	/**
	 * Build the properties for the given block and store them into the internal map
	 * 
	 * @param block
	 */
	public BlockProperties(Block block) {
		// Lava is considered to NOT be a liquid, and is therefore not
		// wallable. This is so we can build cities on the lava surface.
		isWater = block.blockMaterial == Material.water || block == Block.ice;
		isStair = block instanceof BlockStairs;
		isDoor = block instanceof BlockDoor;
		isTree = block instanceof BlockLog || block instanceof IShearable || block instanceof BlockSnow;
		isFlowing = isWater || block.blockMaterial == Material.lava; // || block instanceof BlockDynamicLiquid || block instanceof BlockFalling;
		isWallable = isWater || block == Registry.blockAir || isTree || block instanceof BlockWeb || block instanceof BlockPumpkin || block instanceof BlockMelon || block instanceof BlockMushroomCap || block instanceof IPlantable;
		isOre = block == Block.blockClay || block instanceof BlockRedstoneOre || block instanceof BlockOre;
		isGround = block == Block.stone || block instanceof BlockDirt || block instanceof BlockGrass || block instanceof BlockGravel || block instanceof BlockSand || block instanceof BlockNetherrack || block instanceof BlockSoulSand || block instanceof BlockMycelium;
		// Define by what it is not. Not IS_WALLABLE and not a naturally
		// occurring solid block (obsidian/bedrock are exceptions)
		isArtificial = !(isWallable || isOre || isGround);
		isLight = block instanceof BlockTorch || block instanceof BlockGlowStone;
		isDelayed = isStair || isFlowing || isLight || block == Registry.blockAir || block instanceof BlockLever || block instanceof BlockSign || block instanceof BlockFire || block instanceof BlockButton || block instanceof BlockVine || block instanceof BlockRedstoneWire || block instanceof BlockDispenser || block instanceof BlockFurnace;
		// Define by what it is not.
		isLoaded = !(isWallable || isFlowing || block instanceof BlockTorch || block instanceof BlockLadder);
		props.put(block, this);
	}

	/**
	 * Retrieve the properties for the given block
	 * 
	 * @param block
	 * @return
	 */
	public static BlockProperties get(Block block) {
		BlockProperties p = props.get(block);
		if (p != null) {
			return p;
		}
		return new BlockProperties(block);
	}

	public static BlockProperties get(int blockId) {
		return get(Block.blocksList[blockId]);
	}
}
