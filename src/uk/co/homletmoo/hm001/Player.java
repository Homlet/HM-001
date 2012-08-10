package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;
import static java.lang.Math.abs;

public class Player {
	
	public Point p;
	public Point v;
	public Point a;
	public float rotX, rotY;
	public Point vecRotXZ;
	public Point vecRotY;
	public AABB aabb;
	
	public Player(Point p)
	{
		this.p = p;
		a = new Point(0, 0, 0);
		v = new Point(0, 0, 0);
	}
	
	public void update(int delta, Input input, World w)
	{
		//
		// HANDLE COLLISION ----------------------------------------------------------------------------------------------------------------------------------------------
		//
			byte col = 0;
			aabb = new AABB(new Point(p.x - P_SIZE_XZ / 2, p.y - P_SIZE_Y, p.z - P_SIZE_XZ / 2), new Point(p.x + P_SIZE_XZ / 2, p.y, p.z + P_SIZE_XZ / 2));
			
			if(w.collide(aabb))
				col |= ANY;
			
			if(w.collide(new AABB(
									new Point(aabb.p1.x + P_SIZE_XZ, aabb.p1.y, aabb.p1.z),
									new Point(aabb.p2.x + P_SIZE_XZ, aabb.p2.y, aabb.p2.z))
								))
				col |= XP;
			
			if(w.collide(new AABB(
									new Point(aabb.p1.x - P_SIZE_XZ, aabb.p1.y, aabb.p1.z),
									new Point(aabb.p2.x - P_SIZE_XZ, aabb.p2.y, aabb.p2.z))
								))
				col |= XN;
			
			if(w.collide(new AABB(
									new Point(aabb.p1.x, aabb.p1.y + P_SIZE_Y, aabb.p1.z),
									new Point(aabb.p2.x, aabb.p2.y + P_SIZE_Y, aabb.p2.z))
								))
				col |= YP;
			
			if(w.collide(new AABB(
									new Point(aabb.p1.x, aabb.p1.y - P_SIZE_Y, aabb.p1.z),
									new Point(aabb.p2.x, aabb.p2.y - P_SIZE_Y, aabb.p2.z))
								))
				col |= YN;
			
			if(w.collide(new AABB(
									new Point(aabb.p1.x, aabb.p1.y, aabb.p1.z + P_SIZE_XZ),
									new Point(aabb.p2.x, aabb.p2.y, aabb.p2.z + P_SIZE_XZ))
								))
				col |= ZP;
			
			if(w.collide(new AABB(
									new Point(aabb.p1.x, aabb.p1.y, aabb.p1.z - P_SIZE_XZ),
									new Point(aabb.p2.x, aabb.p2.y, aabb.p2.z - P_SIZE_XZ))
								))
				col |= ZN;
		//
		// END COLLISION -------------------------------------------------------------------------------------------------------------------------------------------------
		//
		
		//
		// HANDLE INPUT --------------------------------------------------------------------------------------------------------------------------------------------------
		//
			boolean walking = false;
			if(input.keys[P_C_FORWARD])
			{
				walking = true;
				v.x += -vecRotXZ.x * P_SPEED;
				v.z += -vecRotXZ.y * P_SPEED;
			}
			if(input.keys[P_C_BACKWARD])
			{
				walking = true;
				v.x += vecRotXZ.x * P_SPEED;
				v.z += vecRotXZ.y * P_SPEED;
			}
			if(input.keys[P_C_LEFT])
			{
				walking = true;
				v.x += -vecRotXZ.y * P_SPEED;
				v.z += vecRotXZ.x * P_SPEED;
			}
			if(input.keys[P_C_RIGHT])
			{
				walking = true;
				v.x += vecRotXZ.y * P_SPEED;
				v.z += -vecRotXZ.x * P_SPEED;
			}
			if(input.pressed(P_C_JUMP))
			{
				v.y = P_SPEED_JUMP;
			}
		//
		// END INPUT -----------------------------------------------------------------------------------------------------------------------------------------------------
		//
		
		if(!walking && (col & YN) != 0)
		{
			v.divide(1.2f, true, false, true);
		}
		
		if((col & YN) == 0)
		{
			v.y -= GRAV * delta;
		} else if(v.y < 0)
		{
			v.y = 0;
		}

		v.divide(1.025f, false, true, false);
		v.divide(1.2f, true, false, true);
		
		if(input.grabbed)
			rotX -= input.mouseDX * SENS_X;
		double radRotX = degToRad(rotX);
		vecRotXZ = radToVec(radRotX);
		
		if(input.grabbed)
			rotY += input.mouseDY * SENS_Y;
		if(rotY < -89)
			rotY = -89;
		else if(rotY > 89)
			rotY = 89;
		double radRotY = degToRad(rotY);
		vecRotY = radToVec(radRotY);
		
		if(v.x != 0
		&& v.x / abs(v.x) > 0)
		{
			if((col & XP) == 0)
				p.x += v.x;
			else v.x = 0;
		} else
		{
			if((col & XN) == 0)
				p.x += v.x;
			else v.x = 0;
		}

		if(v.y != 0
		&& v.y / abs(v.y) > 0)
		{
			if((col & YP) == 0)
				p.y += v.y;
			else v.y = 0;
		} else
		{
			if((col & YN) == 0)
				p.y += v.y;
			else v.y = 0;
		}

		if(v.z != 0
		&& v.z / abs(v.z) > 0)
		{
			if((col & ZP) == 0)
				p.z += v.z;
			else v.z = 0;
		} else
		{
			if((col & ZN) == 0)
				p.z += v.z;
			else v.z = 0;
		}
	}
	
	private double degToRad(double degrees)
	{
		double radians = degrees * (Math.PI / 180);
		return radians;
	}
	
	private Point radToVec(double radians)
	{
		Point vector = new Point(Math.sin(radians), Math.cos(radians), 0);
		return vector;
	}
}
