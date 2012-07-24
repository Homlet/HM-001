package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Entity {
	
	private Point p;
	private Vector<Renderable> graphics;
	private Random rand;
	private int speed;
	private AABB aabb;
	
	public Entity(Point p, Renderable graphic)
	{

		this.p = p;
		float sx = graphic.width / 2;
		float sy = graphic.height / 2;
		float sz = graphic.depth / 2;
		aabb = new AABB(new Point(p.x - sx, p.y - sy, p.z - sz), new Point(p.x + sx, p.y + sy, p.z + sz));
		graphics = new Vector<Renderable>();
		this.graphics.add(graphic);
		rand = new Random();
		speed = rand.nextInt(20) + 1;
	}
	
	public void update(int delta, Input input)
	{
		if(input.mouseLeft)
			p.z -= speed;
		else if(input.mouseRight)
			p.z += speed;
		
		if(p.z < 0)
		{
			p.z = B_SIZE * B_CHUNK_SIZE;
			p.x = rand.nextFloat() * B_SIZE * B_CHUNK_SIZE;
			p.y = rand.nextFloat() * B_SIZE * B_CHUNK_SIZE;
		}else if(p.z >  B_SIZE * B_CHUNK_SIZE)
		{
			p.z = 0;
			p.x = rand.nextFloat() * B_SIZE * B_CHUNK_SIZE;
			p.y = rand.nextFloat() * B_SIZE * B_CHUNK_SIZE;
		}
	}
	
	public void getRenderables(Vector<Renderable> stack)
	{
		Iterator<Renderable> i = graphics.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			r.x = r.offsetX + p.x;
			r.y = r.offsetY + p.y;
			r.z = r.offsetZ + p.z;
		}
		
		stack.addAll(graphics);
	}
}
