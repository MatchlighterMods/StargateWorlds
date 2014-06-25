package ml.sgworlds.world.gen.structure.city;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.gen.structure.StructureComponent;

public abstract class CityComponent extends StructureComponent {

	protected CityStructureStart start;
	
	public CityComponent() {}
	
	public CityComponent(CityStructureStart start, int ctype, int coordMode) {
		super(ctype);
		this.start = start;
		this.coordBaseMode = coordMode;
	}
	
	public ChunkPosition getAbsPosition(int x, int y, int z) {
		return new ChunkPosition(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
	}
	
	@Override
	protected void func_143012_a(NBTTagCompound nbttagcompound) {
		saveData(nbttagcompound);
	}

	@Override
	protected void func_143011_b(NBTTagCompound nbttagcompound) {
		loadData(nbttagcompound);
	}

	public abstract void saveData(NBTTagCompound tag);
	public abstract void loadData(NBTTagCompound tag);
}
