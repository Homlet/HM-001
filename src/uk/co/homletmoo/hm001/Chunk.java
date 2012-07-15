package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

import java.util.HashMap;
import java.util.Random;

import libnoiseforjava.util.NoiseMap;

public class Chunk {

	private Block[][][] blocks;
	private Block[] cache;
	private boolean changed = true;
	private int cx, cy, cz;
	private Random rand = new Random();
	
	public Chunk(int cx, int cy, int cz, NoiseMap heightmap)
	{
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		
		cache = new Block[(int) pow(Attr.B_CHUNK_SIZE, 3)];
		blocks = new Block[Attr.B_CHUNK_SIZE][Attr.B_CHUNK_SIZE][Attr.B_CHUNK_SIZE];
		for(int x = 0; x < blocks.length; x++)
			for(int y = 0; y < blocks[x].length; y++)
				for(int z = 0; z < blocks[x][y].length; z++)
				{
					if(gP(y, cy) < heightmap.getValue(gP(x, cx), gP(z, cz)) * Attr.B_WORLD_HEIGHT_BL || gP(y, cy) == 0)
						if(rand.nextFloat() > Attr.B_SNOW_LEVEL / (gP(y, cy) + 0.01f) - 0.5f)
							blocks[x][y][z] = new Block(gP(x, cx), gP(y, cy), gP(z, cz), 0.95f, 0.95f, 0.95f, true);
						else if(gP(y, cy) > Attr.B_GRASS_LEVEL)
							blocks[x][y][z] = new Block(gP(x, cx), gP(y, cy), gP(z, cz), 0.1f, Math.min(0.045f * gP(y, cy), 0.45f), 0.2f, true);
						else
							blocks[x][y][z] = new Block(gP(x, cx), gP(y, cy), gP(z, cz), Math.min(0.045f * (gP(y, cy)), 0.1f), Math.min(0.005f * (gP(y, cy)), 0.09f), 0.45f, true);
					else
						blocks[x][y][z] = new Block(gP(x, cx), gP(y, cy), gP(z, cz), 0, 0, 0, false);
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
								&& z > 0 && z < blocks[x][y].length - 1)
								{
									if(blocks[x + 1][y][z].active
									&& blocks[x - 1][y][z].active
									&& blocks[x][y + 1][z].active
									&& blocks[x][y - 1][z].active
									&& blocks[x][y][z + 1].active
									&& blocks[x][y][z - 1].active)
									{
										cache[index++] = null;
									} else
									{
										blocks[x][y][z].update(true,
																!blocks[x + 1][y][z].active,
																!blocks[x - 1][y][z].active,
																!blocks[x][y + 1][z].active,
																!blocks[x][y - 1][z].active,
																!blocks[x][y][z + 1].active,
																!blocks[x][y][z - 1].active);
										cache[index++] = blocks[x][y][z];
									}
								} else
								{
									boolean xp = true, xn = true, yp = true, yn = true, zp = true, zn = true;
									if(m.get(new String(cP(x + 1, cx) + " " + cP(y, cy) + " " + cP(z, cz)).hashCode()) == null
									|| m.get(new String(cP(x + 1, cx) + " " + cP(y, cy) + " " + cP(z, cz)).hashCode()).blocks[edgePos(x)][y][z].active)
										xp = false;
									
									if(m.get(new String(cP(x - 1, cx) + " " + cP(y, cy) + " " + cP(z, cz)).hashCode()) == null
									|| m.get(new String(cP(x - 1, cx) + " " + cP(y, cy) + " " + cP(z, cz)).hashCode()).blocks[edgeNeg(x)][y][z].active)
										xn = false;
									
									if(m.get(new String(cP(x, cx) + " " + cP(y + 1, cy) + " " + cP(z, cz)).hashCode()) == null
									|| m.get(new String(cP(x, cx) + " " + cP(y + 1, cy) + " " + cP(z, cz)).hashCode()).blocks[x][edgePos(y)][z].active)
										yp = false;
									
									if(m.get(new String(cP(x, cx) + " " + cP(y - 1, cy) + " " + cP(z, cz)).hashCode()) == null
									|| m.get(new String(cP(x, cx) + " " + cP(y - 1, cy) + " " + cP(z, cz)).hashCode()).blocks[x][edgeNeg(y)][z].active)
										yn = false;
									
									if(m.get(new String(cP(x, cx) + " " + cP(y, cy) + " " + cP(z + 1, cz)).hashCode()) == null
									|| m.get(new String(cP(x, cx) + " " + cP(y, cy) + " " + cP(z + 1, cz)).hashCode()).blocks[x][y][edgePos(z)].active)
										zp = false;
									
									if(m.get(new String(cP(x, cx) + " " + cP(y, cy) + " " + cP(z - 1, cz)).hashCode()) == null
									|| m.get(new String(cP(x, cx) + " " + cP(y, cy) + " " + cP(z - 1, cz)).hashCode()).blocks[x][y][edgeNeg(z)].active)
										zn = false;
									
									blocks[x][y][z].update(true, xp, xn, yp, yn, zp, zn);
									cache[index++] = blocks[x][y][z];
								}
							} else
								cache[index++] = null;
							
							blocks[x][y][z].changed = false;
						}
					}
			changed = false;
		}
		
		return cache;
	}
	
	private int gP(int p, int cp)
	{
		return p + Attr.B_CHUNK_SIZE * cp;
	}
	
	private int cP(int p, int cp)
	{
		if(p < 0)
			return cp - 1;
		else if(p > Attr.B_CHUNK_SIZE - 1)
			return cp + 1;
		else
			return cp;
	}
	
	private int edgePos(int p)
	{
		if(p == Attr.B_CHUNK_SIZE - 1)
			return 0;
		else
			return p + 1;
	}
	
	private int edgeNeg(int p)
	{
		if(p == 0)
			return Attr.B_CHUNK_SIZE - 1;
		else
			return p - 1;
	}
}
