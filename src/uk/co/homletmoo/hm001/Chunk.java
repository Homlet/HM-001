package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

import java.util.HashMap;

import libnoiseforjava.util.NoiseMap;

public class Chunk {

	private Block[][][] blocks;
	private Block[] cache;
	private boolean changed = true;
	
	public Chunk(int cx, int cy, int cz, NoiseMap heightmap)
	{
		cache = new Block[(int) pow(Attr.B_CHUNK_SIZE, 3)];
		blocks = new Block[Attr.B_CHUNK_SIZE][Attr.B_CHUNK_SIZE][Attr.B_CHUNK_SIZE];
		for(int x = 0; x < blocks.length; x++)
			for(int y = 0; y < blocks[x].length; y++)
				for(int z = 0; z < blocks[x][y].length; z++)
				{
					if(gridPos(y, cy) < heightmap.getValue(gridPos(x, cx), gridPos(z, cz)) * Attr.B_WORLD_HEIGHT * Attr.B_CHUNK_SIZE / 4 * 3)
						if(gridPos(y, cy) > Attr.B_GRASS_LEVEL)
							blocks[x][y][z] = new Block(gridPos(x, cx), gridPos(y, cy), gridPos(z, cz), 0.2f, 0.65f * gridPos(y, cy) / 10, 0.35f, true);
						else
							blocks[x][y][z] = new Block(gridPos(x, cx), gridPos(y, cy), gridPos(z, cz), 0.45f, 0.4f, 0.38f, true);
					else
						blocks[x][y][z] = new Block(gridPos(x, cx), gridPos(y, cy), gridPos(z, cz), 0, 0, 0, false);
				}
	}
	
	public void update(int delta, Input input)
	{
	}
	
	public Block[] getBlocks(HashMap<Integer, Chunk> m)
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
								} else
									cache[index++] = blocks[x][y][z];
							} else
								cache[index++] = null;
							
							blocks[x][y][z].changed = false;
						}
					}
			
			changed = false;
		}
		
		return cache;
	}
	
	private int gridPos(int p, int cp)
	{
		return p + Attr.B_CHUNK_SIZE * cp;
	}
}
