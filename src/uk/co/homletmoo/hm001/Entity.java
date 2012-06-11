package uk.co.homletmoo.hm001;

import java.util.Iterator;
import java.util.Vector;

public class Entity {
	
	private float x, y, z;
	private Vector<Renderable> graphics;
	
	private boolean addition = false;
	
	public Entity(float x, float y, float z, Renderable graphic)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		graphics = new Vector<Renderable>();
		this.graphics.add(graphic);
	}
	
	public void update(int delta)
	{
		float vX = Attr.DISPLAY_HALFWIDTH - x;
		float vY = Attr.DISPLAY_HALFHEIGHT - y;
		float vZ = Attr.HALFDEPTH - z;
		vX = (vX / 600) * delta;
		vY = (vY / 600) * delta;
		vZ = (vZ / 600) * delta;

		if(Math.abs(vX) >= 1000 && Math.abs(vY) >= 1000 && Math.abs(vZ) >= 1000)
			addition = true;
		else if(Math.abs(vX) <= 1 && Math.abs(vY) <= 1 && Math.abs(vZ) <= 1)
			addition = false;
		
		if(addition)
		{
			x += vX;
			y += vY;
			z += vZ;
		}else
		{
			x -= vX;
			y -= vY;
			z -= vZ;
		}
		
		Iterator<Renderable> i = graphics.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			r.x = r.offsetX + x;
			r.y = r.offsetY + y;
			r.z = r.offsetZ + z;
		}
	}
	
	public void sendRenderables(Vector<Renderable> stack)
	{
		stack.addAll(graphics);
	}
}
