package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

import java.util.Random;

public class Chunk {
	
	private Block[] cache;
	private Block[][][] blocks;
	private boolean changed = true;
	private Random rand = new Random();
	
	public Chunk(int cx, int cy, int cz)
	{
		cache = new Block[(int) pow(Attr.B_CHUNK_SIZE, 3)];
		blocks = new Block[Attr.B_CHUNK_SIZE][Attr.B_CHUNK_SIZE][Attr.B_CHUNK_SIZE];
		for(int x = 0; x < blocks.length; x++)
			for(int y = 0; y < blocks[x].length; y++)
				for(int z = 0; z < blocks[x][y].length; z++)
				{
					blocks[x][y][z] = new Block(x + cx * Attr.B_CHUNK_SIZE, y + cy * Attr.B_CHUNK_SIZE, z + cz * Attr.B_CHUNK_SIZE, 0.5f, 0.5f, 0.5f, rand.nextFloat() > 0.85f);
				}
	}
	
	public void update(int delta, Input input)
	{
	}
	
	public Block[] getBlocks()
	{
		if(changed)
		{
			int index = 0;
			for(int x = 0; x < blocks.length; x++)
				for(int y = 0; y < blocks[x].length; y++)
					for(int z = 0; z < blocks[x][y].length; z++)
					{
						if(blocks[x][y][z].changed)
						{
							if(blocks[x][y][z].active)
							{
								if(x > 0 && x < blocks.length - 1
								&& y > 0 && y < blocks[x].length - 1
								&& z > 0 && z < blocks[x][y].length - 1
								&& blocks[x + 1][y][z].active
								&& blocks[x - 1][y][z].active
								&& blocks[x][y + 1][z].active
								&& blocks[x][y - 1][z].active
								&& blocks[x][y][z + 1].active
								&& blocks[x][y][z - 1].active)
								{
									cache[index++] = null;
								}
								else
									cache[index++] = blocks[x][y][z];
							}
							else
								cache[index++] = null;
							blocks[x][y][z].changed = false;
						}
					}
			
			changed = false;
		}
		
		return cache;
	}
}
