package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.*;
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
			RidgedMulti rmf = new RidgedMulti();
			rmf.setSeed(rand.nextInt());
			rmf.setOctaveCount(6);
			rmf.setFrequency(5.5);
			rmf.setLacunarity(2);

			Perlin p = new Perlin();
			p.setSeed(rand.nextInt());
			p.setOctaveCount(11);
			p.setFrequency(7);
			p.setPersistence(0.65);
			p.setLacunarity(1.9);
			
			Const c = new Const();
			c.setConstValue(0.25);
			
			Turbulence t = new Turbulence(c);
			
			Max m = new Max(p, t);
			
			Perlin p1 = new Perlin();
			p1.setSeed(rand.nextInt());
			p1.setOctaveCount(1);
			p1.setFrequency(12);
			p1.setPersistence(0);
			p1.setLacunarity(1);
			
			ScaleBias sb = new ScaleBias(p1);
			sb.setScale(2);
			sb.setBias(-0.2);
			
			Select s = new Select(m, rmf, sb);
			s.setEdgeFalloff(0.29);
			s.setBounds(-1, 1);
			
			ScalePoint s1 = new ScalePoint(s);
			s1.setScale(0.85);
			
			
			heightmap = new NoiseMap(sizeX * Attr.B_CHUNK_SIZE, sizeZ * Attr.B_CHUNK_SIZE);
			
			NoiseMapBuilderPlane heightmapBuilder = new NoiseMapBuilderPlane();
			heightmapBuilder.setSourceModule(s1);
			heightmapBuilder.setDestNoiseMap(heightmap);
			heightmapBuilder.setDestSize(sizeX * Attr.B_CHUNK_SIZE, sizeZ * Attr.B_CHUNK_SIZE);
			heightmapBuilder.setBounds(0, (double) sizeX / 20, 0, (double) sizeX / 20);
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
