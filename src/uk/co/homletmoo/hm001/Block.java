package uk.co.homletmoo.hm001;

import java.util.Random;

public class Block {

	public int x, y, z;
	public float r, g, b;
	public boolean active;
	public boolean changed = true;
	private Random rand = new Random();
	
	public Block(int x, int y, int z, float r, float g, float b, boolean active)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = (float) (r + rand.nextFloat() / 10 - 0.05);
		this.g = (float) (g + rand.nextFloat() / 10 - 0.05);
		this.b = (float) (b + rand.nextFloat() / 10 - 0.05);
		this.active = active;
	}
}
