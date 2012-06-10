package uk.co.homletmoo.hm001;

import java.util.Iterator;
import java.util.Vector;

public class Entity {
	
	private float x, y;
	private Vector<Renderable> graphics;
	
	private boolean addition = false;
	
	public Entity(float x, float y, Renderable graphic)
	{
		this.x = x;
		this.y = y;
		graphics = new Vector<Renderable>();
		this.graphics.add(graphic);
	}
	
	public void update(int delta)
	{
		float vX = Attr.DISPLAY_HALFWIDTH - x;
		float vY = Attr.DISPLAY_HALFHEIGHT - y;
		vX = (vX / 100) * delta;
		vY = (vY / 100) * delta;

		if(Math.abs(vX) >= 1000 && Math.abs(vY) >= 1000)
			addition = true;
		else if(Math.abs(vX) <= 1 && Math.abs(vY) <= 1)
			addition = false;
		
		if(addition)
		{
			x += vX;
			y += vY;
		}else
		{
			x -= vX;
			y -= vY;
		}
		
		Iterator<Renderable> i = graphics.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			r.x = r.offsetX + x;
			r.y = r.offsetY + y;
		}
	}
	
	public void sendRenderables(Vector<Renderable> stack)
	{
		stack.addAll(graphics);
	}
}
