package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

import java.util.HashMap;
import java.util.Map;

public class World {
	
	private Map<Integer, Chunk> m;
	private int size;
	
	public World(int size)
	{
		this.size = size;
		m = new HashMap<Integer, Chunk>((int) pow(size, 3));
		
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++)
				for(int z = 0; z < size; z++)
				{
					m.put(new String("" + x + y + z).hashCode(), new Chunk(x, y, z));
				}
	}
	
	public void update(int delta, Input input)
	{
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++)
				for(int z = 0; z < size; z++)
				{
					m.get(new String("" + x + y + z).hashCode()).update(delta, input);
				}
	}
	
	public Block[] getBlocks()
	{
		Block[] blocks = new Block[(int) pow(size * Attr.B_CHUNK_SIZE, 3)];
		int index = 0;
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++)
				for(int z = 0; z < size; z++)
				{
					Block[] temp = m.get(new String("" + x + y + z).hashCode()).getBlocks(); 
					for(int w = 0; w < temp.length; w++)
						blocks[index++] = temp[w];
				}
		return blocks;
	}
}
