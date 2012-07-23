package uk.co.homletmoo.hm001;

public class Block {
	
	// List of block types:
	public static int TYPE_LENGTH = 5;
	public static final byte TYPE_AIR = -128;
	public static final byte TYPE_GRASS = -127;
	public static final byte TYPE_DIRT = -126;
	public static final byte TYPE_ROCKS = -125;
	public static final byte TYPE_STONE = -124;
	
	public int type;
	public int x, y, z;
	public int sy, sz;
	public boolean active;
	public boolean changed = true;
	public boolean xp, xn, yp, yn, zp, zn;
	public boolean all = true;
	
	public Block(int type, int x, int y, int z, boolean active)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
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
		if(xp && xn && yp && yn && zp && zn)
			all = true;
		else
			all = false;
	}
	
	public void update()
	{
		
	}
}
