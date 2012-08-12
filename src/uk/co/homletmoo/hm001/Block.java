package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.B_SIZE;

public class Block extends AABB implements Comparable<Block> {
	
	// List of block types:
	public static final byte TYPE_LENGTH = 6;
	public static final byte TYPE_AIR = -128;
	public static final byte TYPE_GRASS = -127;
	public static final byte TYPE_DIRT = -126;
	public static final byte TYPE_ROCKS = -125;
	public static final byte TYPE_STONE = -124;
	public static final byte TYPE_WATER = -123;
	
	public int type;
	public Point p;
	public int sy, sz;
	public boolean active;
	public boolean changed = true;
	public boolean xp, xn, yp, yn, zp, zn;
	public boolean transparent = false;
	
	private boolean scaling;
	
	public Block(int type, Point p, boolean active)
	{
		super(new Point(p.x * B_SIZE, p.y * B_SIZE, p.z * B_SIZE), new Point((p.x + 1) * B_SIZE, (p.y + 1) * B_SIZE, (p.z + 1) * B_SIZE));
		this.type = type;
		this.p = new Point(p);
		this.sy = 1;
		this.sz = 1;
		this.active = active;
	}
	
	public void visibility(boolean active, boolean xp, boolean xn, boolean yp, boolean yn, boolean zp, boolean zn)
	{
		this.active = active;
		this.xp = xp;
		this.xn = xn;
		this.yp = yp;
		this.yn = yn;
		this.zp = zp;
		this.zn = zn;
	}
	
	public void startScaleWithReset()
	{
		sy = 1;
		sz = 1;
		startScale();
	}
	
	public void startScale()
	{
		scaling = true;
	}
	
	public void endScale()
	{
		p1 = new Point(p.x * B_SIZE, p.y * B_SIZE, p.z * B_SIZE);
		p2 = new Point((p.x + 1) * B_SIZE, (p.y + sy) * B_SIZE, (p.z + sz) * B_SIZE);
		scaling = false;
	}
	
	public boolean scaling()
	{
		return scaling;
	}

	@Override
	public int compareTo(Block o)
	{
		if(o instanceof Block)
			if(o.type == type)
	            return 0;
	        else if(o.type < type)
	            return 1;
	        else
	        	return -1;
		else
			return 99;
	}
}
