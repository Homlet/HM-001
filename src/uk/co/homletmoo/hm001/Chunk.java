package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;
import static java.lang.Math.floor;

import java.util.HashMap;
import java.util.Random;

import libnoiseforjava.util.NoiseMap;

public class Chunk {
	
	// Note, blocks are stored in xyz format in blocks, and xzy format in culled
	// y is up
	private Block[][][] blocks, culled;
	private boolean changed = true;
	private int cx, cy, cz;
	
	public Chunk(int cx, int cy, int cz, NoiseMap heightmap)
	{
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		
		culled = new Block[B_CHUNK_SIZE][B_CHUNK_SIZE][B_CHUNK_SIZE];
		blocks = new Block[B_CHUNK_SIZE][B_CHUNK_SIZE][B_CHUNK_SIZE];
		for(int x = 0; x < blocks.length; x++)
			for(int y = 0; y < blocks[x].length; y++)
				for(int z = 0; z < blocks[x][y].length; z++)
				{
					if(gP(y, cy) == floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 2))
						blocks[x][y][z] = new Block(Block.TYPE_GRASS, gP(x, cx), gP(y, cy), gP(z, cz), true);
					
					else if(gP(y, cy) < floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 2)
						&& gP(y, cy) >= floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 3.5))
						blocks[x][y][z] = new Block(Block.TYPE_DIRT, gP(x, cx), gP(y, cy), gP(z, cz), true);
					
					else if(gP(y, cy) < floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 3.5) || gP(y, cy) == 0)
						blocks[x][y][z] = new Block(Block.TYPE_STONE, gP(x, cx), gP(y, cy), gP(z, cz), true);
					
					else
						blocks[x][y][z] = new Block(Block.TYPE_AIR, gP(x, cx), gP(y, cy), gP(z, cz), false);
				}
	}
	
	public void update(int delta, Input input, Random rand)
	{
	}
	
	public Block[][][] rebuild(HashMap<Integer, Chunk> m, Player p)
	{
		if(changed)
		{
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
										culled[x][z][y] = null;
									} else
									{
										blocks[x][y][z].visibility(true,
																!blocks[x + 1][y][z].active,
																!blocks[x - 1][y][z].active,
																!blocks[x][y + 1][z].active,
																!blocks[x][y - 1][z].active,
																!blocks[x][y][z + 1].active,
																!blocks[x][y][z - 1].active);
										culled[x][z][y] = blocks[x][y][z];
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
									
									if(gP(y, cy) != B_WORLD_HEIGHT * B_CHUNK_SIZE - 1
									&& (m.get(new String(cP(x, cx) + " " + cP(y + 1, cy) + " " + cP(z, cz)).hashCode()) == null
									|| m.get(new String(cP(x, cx) + " " + cP(y + 1, cy) + " " + cP(z, cz)).hashCode()).blocks[x][edgePos(y)][z].active))
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
									
									blocks[x][y][z].visibility(true, xp, xn, yp, yn, zp, zn);
									culled[x][z][y] = blocks[x][y][z];
								}
							} else
								culled[x][z][y] = null;
							
							blocks[x][y][z].changed = false;
						}
					}

			changed = false;
		}
		
		return culled;
	}
	
	private int gP(int p, int cp)
	{
		return p + B_CHUNK_SIZE * cp;
	}
	
	private int cP(int p, int cp)
	{
		if(p < 0)
			return cp - 1;
		else if(p > B_CHUNK_SIZE - 1)
			return cp + 1;
		else
			return cp;
	}
	
	private int edgePos(int p)
	{
		if(p == B_CHUNK_SIZE - 1)
			return 0;
		else
			return p + 1;
	}
	
	private int edgeNeg(int p)
	{
		if(p == 0)
			return B_CHUNK_SIZE - 1;
		else
			return p - 1;
	}
}
