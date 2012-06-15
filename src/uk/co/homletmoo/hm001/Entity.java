package uk.co.homletmoo.hm001;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Entity {
	
	private float x, y, z;
	private Vector<Renderable> graphics;
	private Random rand;
	
	public Entity(float x, float y, float z, Renderable graphic)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		graphics = new Vector<Renderable>();
		this.graphics.add(graphic);
		rand = new Random();
	}
	
	public void update(int delta)
	{		
		z -= 120;
		if(z <= 0)
		{
			z = Attr.SIZE;
			x = rand.nextFloat() * Attr.SIZE;
			y = rand.nextFloat() * Attr.SIZE;
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
