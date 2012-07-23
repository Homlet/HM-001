package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.*;
import libnoiseforjava.util.NoiseMap;
import libnoiseforjava.util.NoiseMapBuilderPlane;

public class World {
	
	private Map<Integer, Chunk> m;
	private NoiseMap heightmap;
	private boolean changed = true;
	private Block[] cache;
	
	public World(Random rand)
	{
		try {
			RidgedMulti rmf = new RidgedMulti();
			rmf.setSeed(rand.nextInt());
			rmf.setOctaveCount(6);
			rmf.setFrequency(5.5);
			rmf.setLacunarity(2);

			Perlin p = new Perlin();
			p.setSeed(rand.nextInt());
			p.setOctaveCount(16);
			p.setFrequency(4);
			p.setPersistence(0.55);
			
			Const c = new Const();
			c.setConstValue(0.05);
			
			Max m = new Max(p, c);
			
			Perlin p1 = new Perlin();
			p1.setSeed(rand.nextInt());
			p1.setOctaveCount(1);
			p1.setFrequency(12);
			p1.setPersistence(0);
			p1.setLacunarity(1);
			
			ScaleBias sb = new ScaleBias(p1);
			sb.setScale(2);
			sb.setBias(-1.2);
			
			Select s = new Select(m, rmf, sb);
			s.setEdgeFalloff(0.29);
			s.setBounds(-1, 1);			
			
			heightmap = new NoiseMap(B_WORLD_SIZEX * B_CHUNK_SIZE, B_WORLD_SIZEZ * B_CHUNK_SIZE);
			
			NoiseMapBuilderPlane heightmapBuilder = new NoiseMapBuilderPlane();
			heightmapBuilder.setSourceModule(s);
			heightmapBuilder.setDestNoiseMap(heightmap);
			heightmapBuilder.setDestSize(B_WORLD_SIZEX * B_CHUNK_SIZE, B_WORLD_SIZEZ * B_CHUNK_SIZE);
			heightmapBuilder.setBounds(0, (double) B_WORLD_SIZEX / 20, 0, (double) B_WORLD_SIZEX / 20);
			heightmapBuilder.build();
		} catch (ExceptionInvalidParam e) {
			e.printStackTrace();
			System.exit(-1);
		}

		cache = new Block[B_WORLD_SIZEX * B_CHUNK_SIZE * B_WORLD_SIZEZ * B_CHUNK_SIZE * B_WORLD_HEIGHT * B_CHUNK_SIZE];
		m = new HashMap<Integer, Chunk>(B_WORLD_SIZEX * B_WORLD_SIZEZ * B_WORLD_HEIGHT);
		
		for(int x = 0; x < B_WORLD_SIZEX; x++)
			for(int y = 0; y < B_WORLD_HEIGHT; y++)
				for(int z = 0; z < B_WORLD_SIZEZ; z++)
				{
					m.put((x + " " + y + " " + z).hashCode(), new Chunk(x, y, z, heightmap));
				}
	}
	
	public void update(int delta, Input input, Random rand)
	{
		for(int x = 0; x < B_WORLD_SIZEX; x++)
			for(int y = 0; y < B_WORLD_HEIGHT; y++)
				for(int z = 0; z < B_WORLD_SIZEZ; z++)
				{
					m.get((x + " " + y + " " + z).hashCode()).update(delta, input, rand);
				}
	}
	
	public Block[] getBlocks(Player p)
	{
		if(changed)
		{
			Block[][][] blocks = new Block[B_WORLD_SIZEX * B_CHUNK_SIZE][B_WORLD_SIZEZ * B_CHUNK_SIZE][B_WORLD_HEIGHT * B_CHUNK_SIZE];
			for(int x = 0; x < B_WORLD_SIZEX; x++)
				for(int z = 0; z < B_WORLD_SIZEZ; z++)
					for(int y = 0; y < B_WORLD_HEIGHT; y++)
					{
						Block[][][] temp = m.get((x + " " + y + " " + z).hashCode()).rebuild((HashMap<Integer, Chunk>) m, p);
						for(int x1 = 0; x1 < B_CHUNK_SIZE; x1++)
							for(int z1 = 0; z1 < B_CHUNK_SIZE; z1++)
								for(int y1 = 0; y1 < B_CHUNK_SIZE; y1++)
								{
									blocks[x1 + (x * B_CHUNK_SIZE)][z1 + (z * B_CHUNK_SIZE)][y1 + (y * B_CHUNK_SIZE)] = temp[x1][z1][y1];
								}
					}
			
			// Group like columns of blocks together
			int index = 0;
			Block column;
			// Sorted in xyz
			Block[][][] columns = new Block[B_WORLD_SIZEX * B_CHUNK_SIZE][B_WORLD_HEIGHT * B_CHUNK_SIZE][B_WORLD_SIZEZ * B_CHUNK_SIZE];
			for(int x = 0; x < blocks.length; x++)
				for(int z = 0; z < blocks[x].length; z++)
				{
					column = blocks[x][z][0];
					if(column == null)
						column = new Block(Block.TYPE_AIR, x, 0, z, false);
					for(int y = 1; y < blocks[x][z].length; y++)
					{
						if(blocks[x][z][y] != null)
						{
							if(column.type == blocks[x][z][y].type)
							{
								column.sy++;
								
								if(blocks[x][z][y].xp)
									column.xp = true;
								if(blocks[x][z][y].xn)
									column.xn = true;
								if(blocks[x][z][y].yp)
									column.yp = true;
								if(blocks[x][z][y].yn)
									column.yn = true;
								if(blocks[x][z][y].zp)
									column.zp = true;
								if(blocks[x][z][y].zn)
									column.zn = true;
							} else
							{
								if(column.type != Block.TYPE_AIR)
									columns[x][column.y][z] = column;
								else
									columns[x][column.y][z] = null;
								column = blocks[x][z][y];
							}
						} else
						{
							if(column.type != Block.TYPE_AIR)
								columns[x][column.y][z] = column;
							else
								columns[x][column.y][z] = null;
							column = new Block(Block.TYPE_AIR, x, y, z, false);
						}
					}
					if(column != null
					&& column.type != Block.TYPE_AIR)
						cache[index++] = column;
				}
			
			// Group like z-rows of blocks together
			index = 0;
			Block slice;
			for(int x = 0; x < columns.length; x++)
				for(int y = 0; y < columns[x].length; y++)
				{
					slice = columns[x][y][0];
					if(slice == null)
						slice = new Block(Block.TYPE_AIR, x, y, 0, false);
					for(int z = 1; z < columns[x][y].length; z++)
					{
						if(columns[x][y][z] != null)
						{
							if(slice.type == columns[x][y][z].type
							&& slice.sy == columns[x][y][z].sy)
							{
								slice.sz++;
								
								if(columns[x][y][z].xp)
									slice.xp = true;
								if(columns[x][y][z].xn)
									slice.xn = true;
								if(columns[x][y][z].yp)
									slice.yp = true;
								if(columns[x][y][z].yn)
									slice.yn = true;
								if(columns[x][y][z].zp)
									slice.zp = true;
								if(columns[x][y][z].zn)
									slice.zn = true;
							} else
							{
								if(slice.type != Block.TYPE_AIR)
									cache[index++] = slice;
								slice = columns[x][y][z];
							}
						} else
						{
							if(slice.type != Block.TYPE_AIR)
								cache[index++] = slice;
							slice = new Block(Block.TYPE_AIR, x, y, z, false);
						}
					}
					if(slice != null
					&& slice.type != Block.TYPE_AIR)
						cache[index++] = slice;
				}
			
			changed = false;
		}
		
		return cache;
	}
}
