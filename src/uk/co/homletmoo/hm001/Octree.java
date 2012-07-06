package uk.co.homletmoo.hm001;

import static java.lang.Math.*; 

public class Octree {
	
	private int levels;
	private Node[][][] children = new Node[2][2][2];
	private Block[] btemp;
	private Block[] childbtemp;
	private float cullTimer = 0; 
	
	public Octree(int levels)
	{
		this.levels = levels;
		btemp = new Block[(int) pow(8, levels)];

		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					children[x][y][z] = new Node((byte) (levels - 1),
													(short) (x * pow(2, levels - 1)),
													(short) (y * pow(2, levels - 1)),
													(short) (z * pow(2, levels - 1)));
	}
	
	public Block[] getBlocks(Player player, int delta, Input input)
	{
		int index = 0;
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
				{
					if(cullTest(x, y, z, player))
					{
						for(int w = 0; w < (int) pow(8, levels - 1); w++)
							btemp[index++] = null;
						children[x][y][z].culled = true;
					} else
					{
						if(children[x][y][z].updated)
						{
							children[x][y][z].culled = false;
							children[x][y][z].updated = false;
							childbtemp = children[x][y][z].getBlocks();
							for(int w = 0; w < (int) pow(8, levels - 1); w++)
								btemp[index++] = childbtemp[w];
						} else
						{
							cullTimer += delta;
							if(cullTimer > 2000 * input.mouseDX) //TODO: Fix mouse speed check
							{
								System.out.println(cullTimer);
								children[x][y][z].cull(player);
								cullTimer = 0;
							}
							
							if(children[x][y][z].culled)
							{
								children[x][y][z].culled = false;
								for(int w = 0; w < (int) pow(8, levels - 1); w++)
									btemp[index++] = children[x][y][z].btemp[w];
							} else
								index += pow(8, levels - 1);
						}
					}
				}
		
		return btemp;
	}
	
	private boolean cullTest(int x, int y, int z, Player player)
	{
		float subsize = (float) pow(2, levels - 1);
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 2; j++)
				for(int k = 0; k < 2; k++)
					if(Render.dotVector3f(
							Render.subVector3f(
												new float[] { (x * subsize + i * subsize) * Attr.BLOCK_SIZE, (y * subsize + j * subsize) * Attr.BLOCK_SIZE, (z * subsize + k * subsize) * Attr.BLOCK_SIZE },
												new float[] { player.x, player.y, player.z }),
							new float[] { (float) (player.vecRotXZ[0] * player.vecRotY[1]), (float) -player.vecRotY[0], (float) (player.vecRotXZ[1] * player.vecRotY[1]) }) < 0)
						return false;
		//System.out.println("Culling: " + x + ", " + y + ", " + z);
		return true;
	}
}
