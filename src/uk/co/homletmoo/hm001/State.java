package uk.co.homletmoo.hm001;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class State {
	
	public Vector<Renderable> stack = new Vector<Renderable>();
	private Vector<Entity> entities = new Vector<Entity>();
	
	public State()
	{
		for(int i = 0; i < 2500; i++)
		{
			Renderable r = new Renderable(Attr.TYPE.CUBE, -32, -32, -32, 64, 64, 64, new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), Tex.logo);
			entities.addElement(new Entity(new Random().nextFloat() * Attr.SIZE, new Random().nextFloat() * Attr.SIZE, new Random().nextFloat() * Attr.SIZE, r));
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
