package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;
import static java.lang.Math.pow;
import static java.lang.Math.floor;

import java.util.HashMap;
import java.util.Random;

import libnoiseforjava.util.NoiseMap;

public class Chunk {
	
	private Block[][][] blocks;
	private Block[] cache;
	private boolean changed = true;
	private int cx, cy, cz;
	
	public Chunk(int cx, int cy, int cz, NoiseMap heightmap)
	{
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		
		cache = new Block[(int) pow(B_CHUNK_SIZE, 3)];
		blocks = new Block[B_CHUNK_SIZE][B_CHUNK_SIZE][B_CHUNK_SIZE];
		for(int x = 0; x < blocks.length; x++)
			for(int y = 0; y < blocks[x].length; y++)
				for(int z = 0; z < blocks[x][y].length; z++)
				{
					if(gP(y, cy) == floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 4 * 3.5f))
						blocks[x][y][z] = new Block(Attr.TYPE_GRASS, gP(x, cx), gP(y, cy), gP(z, cz), true);
					else if(gP(y, cy) < floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 4 * 3.5f)
					&& gP(y, cy) >= floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 2))
						blocks[x][y][z] = new Block(Attr.TYPE_DIRT, gP(x, cx), gP(y, cy), gP(z, cz), true);
					else if(gP(y, cy) < floor(heightmap.getValue(gP(x, cx), gP(z, cz)) * B_WORLD_HEIGHT_BL / 2) || gP(y, cy) == 0)
						blocks[x][y][z] = new Block(Attr.TYPE_STONE, gP(x, cx), gP(y, cy), gP(z, cz), true);
					else
						blocks[x][y][z] = new Block(Attr.TYPE_AIR, gP(x, cx), gP(y, cy), gP(z, cz), false);
				}
	}
	
	public void update(int delta, Input input, Random rand)
	{
	}
	
	public Block[] rebuild(HashMap<Integer, Chunk> m, Player p, float[][] frus)
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
										blocks[x][y][z].update(blocks[x][y][z].type, true,
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
									
									blocks[x][y][z].update(blocks[x][y][z].type, true, xp, xn, yp, yn, zp, zn);
									cache[index++] = blocks[x][y][z];
								}
							} else
								cache[index++] = null;
							
							blocks[x][y][z].changed = false;
						}
					}
			changed = false;
		}
		
		Block[] tmp = cache.clone();
		
		/* Pathetic, misunderstood, failed frustum culling method: ------------------------------------------------------------------------
		for(int i = 0; i < tmp.length; i++)
		{
			if(tmp[i] != null)
			{
				if(frus[0][0] * (tmp[i].z * B_SIZE) + frus[0][1] * (-tmp[i].y * B_SIZE) + frus[0][2] * (tmp[i].x * B_SIZE) + frus[0][3] <= -1
				|| frus[1][0] * (tmp[i].z * B_SIZE) + frus[1][1] * (-tmp[i].y * B_SIZE) + frus[1][2] * (tmp[i].x * B_SIZE) + frus[1][3] <= -1
				|| frus[2][0] * (tmp[i].z * B_SIZE) + frus[2][1] * (-tmp[i].y * B_SIZE) + frus[2][2] * (tmp[i].x * B_SIZE) + frus[2][3] <= -1
				|| frus[3][0] * (tmp[i].z * B_SIZE) + frus[3][1] * (-tmp[i].y * B_SIZE) + frus[3][2] * (tmp[i].x * B_SIZE) + frus[3][3] <= -1
				|| frus[4][0] * (tmp[i].z * B_SIZE) + frus[4][1] * (-tmp[i].y * B_SIZE) + frus[4][2] * (tmp[i].x * B_SIZE) + frus[4][3] <= -1
				|| frus[5][0] * (tmp[i].z * B_SIZE) + frus[5][1] * (-tmp[i].y * B_SIZE) + frus[5][2] * (tmp[i].x * B_SIZE) + frus[5][3] <= -1)
				{
					tmp[i] = null;
				}
			}
		}
		----------------------------------------------------------------------------------------------------------------------------------- */
		
		return tmp;
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
