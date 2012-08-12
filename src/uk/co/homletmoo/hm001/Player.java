package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;
import static java.lang.Math.toRadians;
import static java.lang.Math.floor;

public class Player {
	
	public Point p;
	public Point v;
	public float rotX, rotY;
	public Point vecRotXZ;
	public Point vecRotY;
	private Point oP, nP;
	
	public Player(Point p)
	{
		this.p = p;
		v = new Point(0, 0, 0);
	}
	
	public void update(Input input, World w, int delta)
	{		
		if(input.grabbed)
			rotX -= input.mouseDX * SENS_X;
		double radRotX = toRadians(rotX);
		vecRotXZ = radToVec(radRotX);
		
		if(input.grabbed)
			rotY += input.mouseDY * SENS_Y;
		if(rotY < -89)
			rotY = -89;
		else if(rotY > 89)
			rotY = 89;
		double radRotY = toRadians(rotY);
		vecRotY = radToVec(radRotY);
		
		//
		// HANDLE INPUT --------------------------------------------------------------------------------------------------------------------------------------------------
		//
			boolean walking = false;
			v.x = v.z = 0;
			if(input.keys[P_C_FORWARD])
			{
				walking = true;
				v.x += -vecRotXZ.x * P_SPEED * delta;
				v.z += -vecRotXZ.y * P_SPEED * delta;
			}
			if(input.keys[P_C_BACKWARD])
			{
				walking = true;
				v.x += vecRotXZ.x * P_SPEED * delta;
				v.z += vecRotXZ.y * P_SPEED * delta;
			}
			if(input.keys[P_C_LEFT])
			{
				walking = true;
				v.x += -vecRotXZ.y * P_SPEED * delta;
				v.z += vecRotXZ.x * P_SPEED * delta;
			}
			if(input.keys[P_C_RIGHT])
			{
				walking = true;
				v.x += vecRotXZ.y * P_SPEED * delta;
				v.z += -vecRotXZ.x * P_SPEED * delta;
			}
			if(input.pressed(P_C_JUMP))
			{
				v.y = P_SPEED_JUMP;
			}
		//
		// END INPUT -----------------------------------------------------------------------------------------------------------------------------------------------------
		//
		
		if(!walking)
			v.x = v.z = 0;

		v.y -= GRAV * delta;
		
		attemptMove(w);
	}
	
	private void attemptMove(World w)
	{
		oP = p;
		nP = null;
		
		// Check x movement:
		if(v.x != 0)
		{
			nP = new Point(oP.x + v.x, oP.y, oP.z);
			if(w.collide(new AABB(new Point(nP.x - P_SIZE_XZ / 2, nP.y - P_SIZE_Y, nP.z - P_SIZE_XZ / 2), new Point(nP.x + P_SIZE_XZ / 2, nP.y, nP.z + P_SIZE_XZ / 2))))
			{
				v.x = 0;
				if(nP.x > p.x)
					p.x = (float) (floor(p.x / B_SIZE + 1) * B_SIZE - P_SIZE_XZ / 2);
				else
					p.x = (float) (floor(p.x / B_SIZE) * B_SIZE + P_SIZE_XZ / 2);
			}
			else
				p.x = nP.x;
		}
		
		// Check z movement:
		if(v.z != 0)
		{
			nP = new Point(oP.x, oP.y, oP.z + v.z);
			if(w.collide(new AABB(new Point(nP.x - P_SIZE_XZ / 2, nP.y - P_SIZE_Y, nP.z - P_SIZE_XZ / 2), new Point(nP.x + P_SIZE_XZ / 2, nP.y, nP.z + P_SIZE_XZ / 2))))
			{
				v.z = 0;
				if(nP.z > p.z)
					p.z = (float) (floor(p.z / B_SIZE + 1) * B_SIZE - P_SIZE_XZ / 2);
				else
					p.z = (float) (floor(p.z / B_SIZE) * B_SIZE + P_SIZE_XZ / 2);
			}
			else
				p.z = nP.z;
		}
		
		// Check y movement:
		if(v.y != 0)
		{
			nP = new Point(oP.x, oP.y + v.y, oP.z);
			if(w.collide(new AABB(new Point(nP.x - P_SIZE_XZ / 2, nP.y - P_SIZE_Y, nP.z - P_SIZE_XZ / 2), new Point(nP.x + P_SIZE_XZ / 2, nP.y, nP.z + P_SIZE_XZ / 2))))
			{
				v.y = 0;
				if(nP.y > p.y)
					p.y = (float) (floor(p.y / B_SIZE + 1) * B_SIZE);
				else
					p.y = (float) (floor(p.y / B_SIZE - 1) * B_SIZE + P_SIZE_Y);
			}
			else
				p.y = nP.y;
		}
	}
	
	private Point radToVec(double radians)
	{
		return new Point(Math.sin(radians), Math.cos(radians), 0);
	}
}
