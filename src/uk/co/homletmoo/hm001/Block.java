package uk.co.homletmoo.hm001;

import java.util.Random;

public class Block {

	public int x, y, z;
	public float r, g, b;
	public boolean active;
	public boolean changed = true;
	public boolean xp, xn, yp, yn, zp, zn;
	public boolean all = true;
	private Random rand = new Random();
	
	public Block(int x, int y, int z, float r, float g, float b, boolean active)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = (float) (r + rand.nextFloat() / 32 - 0.03125);
		this.g = (float) (g + rand.nextFloat() / 32 - 0.03125);
		this.b = (float) (b + rand.nextFloat() / 32 - 0.03125);
		this.active = active;
	}
	
	public void update(boolean active, boolean xp, boolean xn, boolean yp, boolean yn, boolean zp, boolean zn)
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
}
