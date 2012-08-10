package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.lwjgl.input.Keyboard;

public class State {
	
	public Vector<Renderable> stack = new Vector<Renderable>();
	public Block[] blocks;
	public Player player = new Player(new Point(0, 4092, 0));
	private Vector<Entity> entities = new Vector<Entity>();
	private World w;
	
	public State(Random rand)
	{
		w = new World(rand);
		
		for(int i = 0; i < 0; i++)
		{
			Renderable r = new Renderable(Attr.PRIM.CUBE, 0, 0, 0, 16, 16, 16, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0);
			entities.addElement(new Entity(new Point(rand.nextFloat() * B_SIZE * B_CHUNK_SIZE, rand.nextFloat() * B_SIZE * B_CHUNK_SIZE, rand.nextFloat() * B_SIZE * B_CHUNK_SIZE), r));
		}
	}
	
	public void update(int delta, Input input, Random rand, Player p)
	{
		if(input.pressed(Keyboard.KEY_F11))
			w = new World(rand);
		
		stack.clear();
		
		player.update(delta, input, w);
		w.update(delta, input, rand);
		blocks = w.getBlocks(p);
		
		Iterator<Entity> i = entities.iterator();
		while(i.hasNext())
		{
			Entity e = i.next();
			e.update(delta, input);
			e.getRenderables(stack);
		}
	}
}
