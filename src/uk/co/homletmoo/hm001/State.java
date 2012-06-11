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
			Renderable r = new Renderable(Attr.TYPE.CUBE, -16, -16, -16, 32, 32, 32, new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), Tex.logo);
			entities.addElement(new Entity(Attr.DISPLAY_HALFWIDTH + (new Random().nextFloat() - 0.5f) * 10, Attr.DISPLAY_HALFHEIGHT + (new Random().nextFloat() - 0.5f) * 10, Attr.HALFDEPTH + (new Random().nextFloat() - 0.5f) * 10, r));
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
