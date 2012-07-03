package uk.co.homletmoo.hm001;

public class Player {
	
	public float x, y, z;
	public float rotX, rotY;
	public double[] vecRotXZ;
	public double[] vecRotY;
	
	public Player(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void update(int delta, Input input)
	{
		rotX -= input.mouseDX * Attr.SENS_X;
		double radRotX = degToRad(rotX);
		vecRotXZ = radToVec(radRotX);
		
		rotY += input.mouseDY * Attr.SENS_Y;
		if(rotY < -89)
			rotY = -89;
		else if(rotY > 89)
			rotY = 89;
		double radRotY = degToRad(rotY);
		vecRotY = radToVec(radRotY);
		
		if(input.keys[Attr.P_C_FORWARD])
		{
			x -= vecRotXZ[0] * vecRotY[1] * Attr.P_SPEED;
			z -= vecRotXZ[1] * vecRotY[1] * Attr.P_SPEED;
			y += vecRotY[0] * Attr.P_SPEED;
		}
		if(input.keys[Attr.P_C_BACKWARD])
		{
			x += vecRotXZ[0] * vecRotY[1] * Attr.P_SPEED;
			z += vecRotXZ[1] * vecRotY[1] * Attr.P_SPEED;
			y -= vecRotY[0] * Attr.P_SPEED;
		}
		if(input.keys[Attr.P_C_LEFT])
		{
			x -= vecRotXZ[1] * Attr.P_SPEED;
			z += vecRotXZ[0] * Attr.P_SPEED;
		}
		if(input.keys[Attr.P_C_RIGHT])
		{
			x += vecRotXZ[1] * Attr.P_SPEED;
			z -= vecRotXZ[0] * Attr.P_SPEED;
		}
		if(input.keys[Attr.P_C_JUMP])
			y += Attr.P_SPEED;
		if(input.keys[Attr.P_C_CROUCH])
			y -= Attr.P_SPEED;
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
