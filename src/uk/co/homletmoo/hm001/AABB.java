package uk.co.homletmoo.hm001;

import static java.lang.Math.abs;

public class AABB {

	public Point p1, p2;
	
	public AABB(Point p1, Point p2)
	{
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public void update(Point p1, Point p2)
	{
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public boolean collide(AABB box)
	{
		Point d1 = new Point(p1.x - box.p2.x, p1.y - box.p2.y, p1.z - box.p2.z);
		Point d2 = new Point(box.p1.x - p2.x, box.p1.y - p2.y, box.p1.z - p2.z);
		return d1.x / abs(d1.x) == d2.x / abs(d2.x)
			&& d1.y / abs(d1.y) == d2.y / abs(d2.y)
			&& d1.z / abs(d1.z) == d2.z / abs(d2.z);
	}
}
