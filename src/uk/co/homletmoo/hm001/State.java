package uk.co.homletmoo.hm001;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class State {
	
	public Vector<Renderable> stack = new Vector<Renderable>();
	private Vector<Entity> entities = new Vector<Entity>();
	
	public State()
	{
		Random rand = new Random();
		
		for(int i = 0; i < 2500; i++)
		{
			Renderable r = new Renderable(Attr.TYPE.CUBE, -24, -24, -24, 48, 48, 48, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), Tex.logo);
			entities.addElement(new Entity(rand.nextFloat() * Attr.SIZE, rand.nextFloat() * Attr.SIZE, rand.nextFloat() * Attr.SIZE, r));
		}
	}
	
	public void update(int delta)
	{
		stack.clear();
		
		Iterator<Entity> i = entities.iterator();
		while(i.hasNext())
		{
			Entity e = i.next();
			e.update(delta);
			e.sendRenderables(stack);
		}
	}
}
