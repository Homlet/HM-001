package uk.co.homletmoo.hm001;

public class Point {
	
	public float x, y, z;
	
	public Point(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(double x, double y, double z)
	{
		this((float) x, (float) y, (float) z);
	}
	
	public void zero()
	{
		x = 0;
		y = 0;
		z = 0;
	}
}
