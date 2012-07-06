package uk.co.homletmoo.hm001;

import static java.lang.Math.pow;

public class Node {
	
	private Object[][][] children = new Object[2][2][2];
	private byte level;
	private Block[] childbtemp;
	private float xGr, yGr, zGr;
	
	public Block[] btemp;
	public boolean updated = true, culled = false;
	
	public Node(byte level, short xGr, short yGr, short zGr)
	{
		this.xGr = xGr;
		this.yGr = yGr;
		this.zGr = zGr;
		this.level = level;
		btemp = new Block[(int) Math.pow(8, level)];
		
		if(level > 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
						children[x][y][z] = new Node((byte) (level - 1),
														(short) (xGr + x * Math.pow(2, level - 1)),
														(short) (yGr + y * Math.pow(2, level - 1)),
														(short) (zGr + z * Math.pow(2, level - 1)));
		else if(level == 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
						children[x][y][z] = new BaseNode((short) (xGr + x * 2),
															(short) (yGr + y * 2),
															(short) (zGr + z * 2));
			
	}
	
	public Block[] getBlocks()
	{
		updated = false;
		int index = 0;
		if(level > 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
					{
						if(((Node) children[x][y][z]).updated)
						{
							childbtemp = ((Node) children[x][y][z]).getBlocks();
							for(int w = 0; w < (int) Math.pow(8, level - 1); w++)
							{
								btemp[index++] = childbtemp[w];
								updated = true;
							}
						}
						else
							index += (int) Math.pow(8, level - 1);
					}
		else if(level == 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
					{
						if(((BaseNode) children[x][y][z]).updated)
						{
							childbtemp = ((BaseNode) children[x][y][z]).getBlocks();
							for(int w = 0; w < 8; w++)
							{
								btemp[index++] = childbtemp[w];
								updated = true;
							}
						}
						else
							index += 8;
					}
		return btemp;
	}
	
	public void cull(Player player)
	{
		int index = 0;
		if(level > 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
						if(cullTest(x, y, z, player))
						{
							culled = true;
							((Node) children[x][y][z]).culled = true;
							for(int w = 0; w < (int) Math.pow(8, level - 1); w++)
								btemp[index++] = null;
						}else
						{
							((Node) children[x][y][z]).cull(player);
							((Node) children[x][y][z]).updated = true;
							updated = true;
							index += (int) Math.pow(8, level - 1);
						}
	}
	
	private boolean cullTest(int x, int y, int z, Player player)
	{
		float subsize = (float) pow(2, level - 1);
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 2; j++)
				for(int k = 0; k < 2; k++)
					if(Render.dotVector3f(
							Render.subVector3f(
												new float[] { (x * subsize + i * subsize) * Attr.BLOCK_SIZE + xGr * Attr.BLOCK_SIZE, (y * subsize + j * subsize) * Attr.BLOCK_SIZE + yGr * Attr.BLOCK_SIZE, (z * subsize + k * subsize) * Attr.BLOCK_SIZE + zGr * Attr.BLOCK_SIZE },
												new float[] { player.x, player.y, player.z }),
							new float[] { (float) (player.vecRotXZ[0] * player.vecRotY[1]), (float) -player.vecRotY[0], (float) (player.vecRotXZ[1] * player.vecRotY[1]) }) < 0)
						return false;
		//System.out.println("Culling: " + x + ", " + y + ", " + z + " : " + level);
		return true;
	}
}
