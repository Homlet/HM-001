package uk.co.homletmoo.hm001;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class State {
	
	public Vector<Renderable> stack = new Vector<Renderable>();
	public Player player = new Player(Attr.HALFSIZE, Attr.HALFSIZE, Attr.HALFSIZE);
	private Vector<Entity> entities = new Vector<Entity>();
	
	public State()
	{		
		Random rand = new Random();
		
		for(int i = 0; i < 3000; i++)
		{
			Renderable r = new Renderable(Attr.TYPE.CUBE, -32, -32, -32, 64, 64, 64, 0.3f, 0.3f, 0.3f, Tex.logo);
			entities.addElement(new Entity(rand.nextFloat() * Attr.SIZE, rand.nextFloat() * Attr.SIZE, rand.nextFloat() * Attr.SIZE, r));
		}
	}
	
	public void update(int delta, Input input)
	{
		stack.clear();
		
		player.update(delta, input);
		
		Iterator<Entity> i = entities.iterator();
		while(i.hasNext())
		{
			Entity e = i.next();
			e.update(delta, input);
			e.getRenderables(stack);
		}
	}
}
