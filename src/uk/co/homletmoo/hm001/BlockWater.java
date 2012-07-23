package uk.co.homletmoo.hm001;

public class BlockWater extends Block {

	public BlockWater(int x, int y, int z)
	{
		super(Block.TYPE_WATER, x, y, z, true);
		transparent = true;
	}	
}
