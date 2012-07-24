package uk.co.homletmoo.hm001;

public class Player {
	
	public Point p;
	public Point v;
	public Point a;
	public float rotX, rotY;
	public double[] vecRotXZ;
	public double[] vecRotY;
	public AABB aabb;
	
	public Player(Point p)
	{
		this.p = p;
		a = new Point(0, 0, 0);
		v = new Point(0, 0, 0);
		aabb = new AABB(new Point(p.x - 32, p.y - 128, p.z - 32), new Point(p.x + 32, p.y, p.z + 32));
	}
	
	public void update(int delta, Input input, World w)
	{
		aabb = new AABB(new Point(p.x - 32, p.y - 128, p.z - 32), new Point(p.x + 32, p.y, p.z + 32));
		
		v.zero();
			
		if(input.grabbed)
			rotX -= input.mouseDX * Attr.SENS_X;
		double radRotX = degToRad(rotX);
		vecRotXZ = radToVec(radRotX);
		
		if(input.grabbed)
			rotY += input.mouseDY * Attr.SENS_Y;
		if(rotY < -89)
			rotY = -89;
		else if(rotY > 89)
			rotY = 89;
		double radRotY = degToRad(rotY);
		vecRotY = radToVec(radRotY);
		
		if(input.keys[Attr.P_C_FORWARD])
		{
			v.x = (float) -vecRotXZ[0] * Attr.P_SPEED * delta;
			v.z = (float) -vecRotXZ[1] * Attr.P_SPEED * delta;
		}
		if(input.keys[Attr.P_C_BACKWARD])
		{
			v.x = (float) vecRotXZ[0] * Attr.P_SPEED * delta;
			v.z = (float) vecRotXZ[1] * Attr.P_SPEED * delta;
		}
		if(input.keys[Attr.P_C_LEFT])
		{
			v.x = (float) -vecRotXZ[1] * Attr.P_SPEED * delta;
			v.z = (float) vecRotXZ[0] * Attr.P_SPEED * delta;
		}
		if(input.keys[Attr.P_C_RIGHT])
		{
			v.x = (float) vecRotXZ[1] * Attr.P_SPEED * delta;
			v.z = (float) -vecRotXZ[0] * Attr.P_SPEED * delta;
		}
		
		if(a.y >= -Attr.P_MAX_SPEEDY)
		{
			a.y -= Attr.GRAV;
		}
		else
		{
			a.y = -Attr.P_MAX_SPEEDY;
		}

		if(w.collide(aabb))
		{
			if(a.y < -Attr.GRAV)
				p.y = (float) Math.floor(p.y / Attr.B_SIZE) * Attr.B_SIZE + 128;
			v.y = 0;
			a.y = 0;
		}
		
		if(input.pressed(Attr.P_C_JUMP))
		{
			a.y = 2 * Attr.P_SPEED * delta;
		}
		
		v.y += a.y;

		p.x += v.x;
		p.y += v.y;
		p.z += v.z;
	}
	
	private double degToRad(double degrees)
	{
		double radians = degrees * (Math.PI / 180);
		return radians;
	}
	
	private double[] radToVec(double radians)
	{
		double[] vector = new double[] {Math.sin(radians), Math.cos(radians)};
		return vector;
	}
}
