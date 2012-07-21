package uk.co.homletmoo.hm001;

public class Block { 
	
	public int type;
	public int x, y, z;
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
		this.active = active;
	}
	
	public void update(int type, boolean active, boolean xp, boolean xn, boolean yp, boolean yn, boolean zp, boolean zn)
	{
		this.type = type;
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
