package uk.co.homletmoo.hm001;

public class BlockWater extends Block {

	public BlockWater(Point p)
	{
		super(Block.TYPE_WATER, p, true);
		transparent = true;
	}	
}
