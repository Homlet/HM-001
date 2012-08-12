package uk.co.homletmoo.hm001;

public class Point {
	
	public float x, y, z;
	
	public Point(double x, double y, double z)
	{
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}
	
	public Point(Point p)
	{
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	public void zero()
	{
		x = y = z = 0;
	}
}
