package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.Perlin;
import libnoiseforjava.util.NoiseMap;
import libnoiseforjava.util.NoiseMapBuilderPlane;

public class World {
	
	private Map<Integer, Chunk> m;
	private int sizeX, sizeZ;
	private NoiseMap heightmap;
	private Random rand = new Random();
	
	public World(int sizeX, int sizeZ)
	{
		try {
			Perlin perlin = new Perlin();
			perlin.setSeed(rand.nextInt());
			perlin.setOctaveCount(16);
			perlin.setPersistence(0.6);
			heightmap = new NoiseMap(sizeX * Attr.B_CHUNK_SIZE, sizeZ * Attr.B_CHUNK_SIZE);
			NoiseMapBuilderPlane heightmapBuilder = new NoiseMapBuilderPlane();
			heightmapBuilder.setSourceModule(perlin);
			heightmapBuilder.setDestNoiseMap(heightmap);
			heightmapBuilder.setDestSize(sizeX * Attr.B_CHUNK_SIZE, sizeZ * Attr.B_CHUNK_SIZE);
			heightmapBuilder.setBounds(0, 1, 0, 1);
			heightmapBuilder.build();
		} catch (ExceptionInvalidParam e) {
			e.printStackTrace();
			System.exit(-1);
		}

		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
		m = new HashMap<Integer, Chunk>(sizeX * sizeZ * Attr.B_WORLD_HEIGHT);
		
		for(int x = 0; x < sizeX; x++)
			for(int y = 0; y < Attr.B_WORLD_HEIGHT; y++)
				for(int z = 0; z < sizeZ; z++)
				{
					m.put(new String(x + " " + y + " " + z).hashCode(), new Chunk(x, y, z, heightmap));
				}
	}
	
	public void update(int delta, Input input)
	{
		for(int x = 0; x < sizeX; x++)
			for(int y = 0; y < Attr.B_WORLD_HEIGHT; y++)
				for(int z = 0; z < sizeZ; z++)
				{
					m.get(new String(x + " " + y + " " + z).hashCode()).update(delta, input);
				}
	}
	
	public Block[] getBlocks()
	{
		Block[] blocks = new Block[(int) (sizeX * sizeZ * Attr.B_WORLD_HEIGHT * pow(Attr.B_CHUNK_SIZE, 3))];
		int index = 0;
		for(int x = 0; x < sizeX; x++)
			for(int y = 0; y < Attr.B_WORLD_HEIGHT; y++)
				for(int z = 0; z < sizeZ; z++)
				{
					Block[] temp = m.get(new String(x + " " + y + " " + z).hashCode()).getBlocks((HashMap<Integer, Chunk>) m); 
					for(int w = 0; w < temp.length; w++)
						blocks[index++] = temp[w];
				}
		return blocks;
	}
}
