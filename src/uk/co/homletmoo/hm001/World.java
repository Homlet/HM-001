package uk.co.homletmoo.hm001;

import static uk.co.homletmoo.hm001.Attr.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.lwjgl.BufferUtils;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.*;
import libnoiseforjava.util.NoiseMap;
import libnoiseforjava.util.NoiseMapBuilderPlane;

public class World {
	
	private Map<Integer, Chunk> m;
	private NoiseMap heightmap;
	
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
			c.setConstValue(0.2);
			
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
			
			ScalePoint s1 = new ScalePoint(s);
			s1.setScale(0.85);
			
			
			heightmap = new NoiseMap(B_WORLD_SIZEX * B_CHUNK_SIZE, B_WORLD_SIZEZ * B_CHUNK_SIZE);
			
			NoiseMapBuilderPlane heightmapBuilder = new NoiseMapBuilderPlane();
			heightmapBuilder.setSourceModule(s1);
			heightmapBuilder.setDestNoiseMap(heightmap);
			heightmapBuilder.setDestSize(B_WORLD_SIZEX * B_CHUNK_SIZE, B_WORLD_SIZEZ * B_CHUNK_SIZE);
			heightmapBuilder.setBounds(0, (double) B_WORLD_SIZEX / 20, 0, (double) B_WORLD_SIZEX / 20);
			heightmapBuilder.build();
		} catch (ExceptionInvalidParam e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		m = new HashMap<Integer, Chunk>(B_WORLD_SIZEX * B_WORLD_SIZEZ * B_WORLD_HEIGHT);
		
		for(int x = 0; x < B_WORLD_SIZEX; x++)
			for(int y = 0; y < B_WORLD_HEIGHT; y++)
				for(int z = 0; z < B_WORLD_SIZEZ; z++)
				{
					m.put(new String(x + " " + y + " " + z).hashCode(), new Chunk(x, y, z, heightmap));
				}
	}
	
	public void update(int delta, Input input, Random rand)
	{
		for(int x = 0; x < B_WORLD_SIZEX; x++)
			for(int y = 0; y < B_WORLD_HEIGHT; y++)
				for(int z = 0; z < B_WORLD_SIZEZ; z++)
				{
					m.get(new String(x + " " + y + " " + z).hashCode()).update(delta, input, rand);
				}
	}
	
	public Block[] getBlocks(Player p)
	{
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		float[] projMatrixA = new float[16];
		
		glGetFloat(GL_PROJECTION_MATRIX, projMatrix);
		
		FloatBuffer modelVMatrix = BufferUtils.createFloatBuffer(16);
		float[] modelVMatrixA = new float[16];
		
		glGetFloat(GL_MODELVIEW_MATRIX, modelVMatrix);
		
		for(int i = 0; i < 16; i++)
		{
			projMatrixA[i] = projMatrix.get(i);
			modelVMatrixA[i] = modelVMatrix.get(i);
		}
		
		float[] modelVProjMatrixA = new float[16];
		
		modelVProjMatrixA[ 0] = modelVMatrixA[ 0] * projMatrixA[ 0] + modelVMatrixA[ 1] * projMatrixA[ 4] + modelVMatrixA[ 2] * projMatrixA[ 8] + modelVMatrixA[ 3] * projMatrixA[12];
		modelVProjMatrixA[ 1] = modelVMatrixA[ 0] * projMatrixA[ 1] + modelVMatrixA[ 1] * projMatrixA[ 5] + modelVMatrixA[ 2] * projMatrixA[ 9] + modelVMatrixA[ 3] * projMatrixA[13];
		modelVProjMatrixA[ 2] = modelVMatrixA[ 0] * projMatrixA[ 2] + modelVMatrixA[ 1] * projMatrixA[ 6] + modelVMatrixA[ 2] * projMatrixA[10] + modelVMatrixA[ 3] * projMatrixA[14];
		modelVProjMatrixA[ 3] = modelVMatrixA[ 0] * projMatrixA[ 3] + modelVMatrixA[ 1] * projMatrixA[ 7] + modelVMatrixA[ 2] * projMatrixA[11] + modelVMatrixA[ 3] * projMatrixA[15];
		
		modelVProjMatrixA[ 4] = modelVMatrixA[ 4] * projMatrixA[ 0] + modelVMatrixA[ 5] * projMatrixA[ 4] + modelVMatrixA[ 6] * projMatrixA[ 8] + modelVMatrixA[ 7] * projMatrixA[12];
		modelVProjMatrixA[ 5] = modelVMatrixA[ 4] * projMatrixA[ 1] + modelVMatrixA[ 5] * projMatrixA[ 5] + modelVMatrixA[ 6] * projMatrixA[ 9] + modelVMatrixA[ 7] * projMatrixA[13];
		modelVProjMatrixA[ 6] = modelVMatrixA[ 4] * projMatrixA[ 2] + modelVMatrixA[ 5] * projMatrixA[ 6] + modelVMatrixA[ 6] * projMatrixA[10] + modelVMatrixA[ 7] * projMatrixA[14];
		modelVProjMatrixA[ 7] = modelVMatrixA[ 4] * projMatrixA[ 3] + modelVMatrixA[ 5] * projMatrixA[ 7] + modelVMatrixA[ 6] * projMatrixA[11] + modelVMatrixA[ 7] * projMatrixA[15];
		
		modelVProjMatrixA[ 8] = modelVMatrixA[ 8] * projMatrixA[ 0] + modelVMatrixA[ 9] * projMatrixA[ 4] + modelVMatrixA[10] * projMatrixA[ 8] + modelVMatrixA[11] * projMatrixA[12];
		modelVProjMatrixA[ 9] = modelVMatrixA[ 8] * projMatrixA[ 1] + modelVMatrixA[ 9] * projMatrixA[ 5] + modelVMatrixA[10] * projMatrixA[ 9] + modelVMatrixA[11] * projMatrixA[13];
		modelVProjMatrixA[10] = modelVMatrixA[ 8] * projMatrixA[ 2] + modelVMatrixA[ 9] * projMatrixA[ 6] + modelVMatrixA[10] * projMatrixA[10] + modelVMatrixA[11] * projMatrixA[14];
		modelVProjMatrixA[11] = modelVMatrixA[ 8] * projMatrixA[ 3] + modelVMatrixA[ 9] * projMatrixA[ 7] + modelVMatrixA[10] * projMatrixA[11] + modelVMatrixA[11] * projMatrixA[15];
		
		modelVProjMatrixA[12] = modelVMatrixA[12] * projMatrixA[ 0] + modelVMatrixA[13] * projMatrixA[ 4] + modelVMatrixA[14] * projMatrixA[ 8] + modelVMatrixA[15] * projMatrixA[12];
		modelVProjMatrixA[13] = modelVMatrixA[12] * projMatrixA[ 1] + modelVMatrixA[13] * projMatrixA[ 5] + modelVMatrixA[14] * projMatrixA[ 9] + modelVMatrixA[15] * projMatrixA[13];
		modelVProjMatrixA[14] = modelVMatrixA[12] * projMatrixA[ 2] + modelVMatrixA[13] * projMatrixA[ 6] + modelVMatrixA[14] * projMatrixA[10] + modelVMatrixA[15] * projMatrixA[14];
		modelVProjMatrixA[15] = modelVMatrixA[12] * projMatrixA[ 3] + modelVMatrixA[13] * projMatrixA[ 7] + modelVMatrixA[14] * projMatrixA[11] + modelVMatrixA[15] * projMatrixA[15];
		
		float[][] frusPlanes = new float[6][4];

		frusPlanes[0][0] = modelVProjMatrixA[ 3] - modelVProjMatrixA[ 0];
		frusPlanes[0][1] = modelVProjMatrixA[ 7] - modelVProjMatrixA[ 4];
		frusPlanes[0][2] = modelVProjMatrixA[11] - modelVProjMatrixA[ 8];
		frusPlanes[0][3] = modelVProjMatrixA[15] - modelVProjMatrixA[12];
		float t = (float) sqrt(frusPlanes[0][0] * frusPlanes[0][0] + frusPlanes[0][1] * frusPlanes[0][1] + frusPlanes[0][2] * frusPlanes[0][2]);
		frusPlanes[0][0] /= t;
		frusPlanes[0][1] /= t;
		frusPlanes[0][2] /= t;
		frusPlanes[0][3] /= t;

		frusPlanes[1][0] = modelVProjMatrixA[ 3] + modelVProjMatrixA[ 0];
		frusPlanes[1][1] = modelVProjMatrixA[ 7] + modelVProjMatrixA[ 4];
		frusPlanes[1][2] = modelVProjMatrixA[11] + modelVProjMatrixA[ 8];
		frusPlanes[1][3] = modelVProjMatrixA[15] + modelVProjMatrixA[12];
		t = (float) sqrt(frusPlanes[1][0] * frusPlanes[1][0] + frusPlanes[1][1] * frusPlanes[1][1] + frusPlanes[1][2] * frusPlanes[1][2]);
		frusPlanes[1][0] /= t;
		frusPlanes[1][1] /= t;
		frusPlanes[1][2] /= t;
		frusPlanes[1][3] /= t;

		frusPlanes[2][0] = modelVProjMatrixA[ 3] - modelVProjMatrixA[ 1];
		frusPlanes[2][1] = modelVProjMatrixA[ 7] - modelVProjMatrixA[ 5];
		frusPlanes[2][2] = modelVProjMatrixA[11] - modelVProjMatrixA[ 9];
		frusPlanes[2][3] = modelVProjMatrixA[15] - modelVProjMatrixA[13];
		t = (float) sqrt(frusPlanes[2][0] * frusPlanes[2][0] + frusPlanes[2][1] * frusPlanes[2][1] + frusPlanes[2][2] * frusPlanes[2][2]);
		frusPlanes[2][0] /= t;
		frusPlanes[2][1] /= t;
		frusPlanes[2][2] /= t;
		frusPlanes[2][3] /= t;

		frusPlanes[3][0] = modelVProjMatrixA[ 3] + modelVProjMatrixA[ 1];
		frusPlanes[3][1] = modelVProjMatrixA[ 7] + modelVProjMatrixA[ 5];
		frusPlanes[3][2] = modelVProjMatrixA[11] + modelVProjMatrixA[ 9];
		frusPlanes[3][3] = modelVProjMatrixA[15] + modelVProjMatrixA[13];
		t = (float) sqrt(frusPlanes[3][0] * frusPlanes[3][0] + frusPlanes[3][1] * frusPlanes[3][1] + frusPlanes[3][2] * frusPlanes[3][2]);
		frusPlanes[3][0] /= t;
		frusPlanes[3][1] /= t;
		frusPlanes[3][2] /= t;
		frusPlanes[3][3] /= t;

		frusPlanes[4][0] = modelVProjMatrixA[ 3] - modelVProjMatrixA[ 2];
		frusPlanes[4][1] = modelVProjMatrixA[ 7] - modelVProjMatrixA[ 6];
		frusPlanes[4][2] = modelVProjMatrixA[11] - modelVProjMatrixA[10];
		frusPlanes[4][3] = modelVProjMatrixA[15] - modelVProjMatrixA[14];
		t = (float) sqrt(frusPlanes[4][0] * frusPlanes[4][0] + frusPlanes[4][1] * frusPlanes[4][1] + frusPlanes[4][2] * frusPlanes[4][2]);
		frusPlanes[4][0] /= t;
		frusPlanes[4][1] /= t;
		frusPlanes[4][2] /= t;
		frusPlanes[4][3] /= t;

		frusPlanes[5][0] = modelVProjMatrixA[ 3] + modelVProjMatrixA[ 2];
		frusPlanes[5][1] = modelVProjMatrixA[ 7] + modelVProjMatrixA[ 6];
		frusPlanes[5][2] = modelVProjMatrixA[11] + modelVProjMatrixA[10];
		frusPlanes[5][3] = modelVProjMatrixA[15] + modelVProjMatrixA[14];
		t = (float) sqrt(frusPlanes[5][0] * frusPlanes[5][0] + frusPlanes[5][1] * frusPlanes[5][1] + frusPlanes[5][2] * frusPlanes[5][2]);
		frusPlanes[5][0] /= t;
		frusPlanes[5][1] /= t;
		frusPlanes[5][2] /= t;
		frusPlanes[5][3] /= t;
		
		Block[] blocks = new Block[(int) (B_WORLD_SIZEX * B_WORLD_SIZEZ * B_WORLD_HEIGHT * pow(B_CHUNK_SIZE, 3))];
		int index = 0;
		for(int x = 0; x < B_WORLD_SIZEX; x++)
			for(int y = 0; y < B_WORLD_HEIGHT; y++)
				for(int z = 0; z < B_WORLD_SIZEZ; z++)
				{
					Block[] temp = m.get((x + " " + y + " " + z).hashCode()).rebuild((HashMap<Integer, Chunk>) m, p, frusPlanes);
					for(int w = 0; w < temp.length; w++)
						blocks[index++] = temp[w];
				}
		return blocks;
	}
}
