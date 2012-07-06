package uk.co.homletmoo.hm001;

import java.util.Random;

public class BaseNode {

	private Block[][][] blocks = new Block[2][2][2];
	private Random rand = new Random();
	public Block[] btemp = new Block[8];
	public boolean updated = true, culled = false;
	
	public BaseNode(short xGr, short yGr, short zGr)
	{
		byte[][][] ids = new byte[][][] {{{(byte) (rand.nextInt(2) - 128), (byte) (rand.nextInt(2) - 128)},
												{Block.air, (byte) (rand.nextInt(2) - 128)}},
											{{Block.hm, (byte) (rand.nextInt(2) - 128)},
												{Block.air, (byte) (rand.nextInt(2) - 128)}}};
			
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					blocks[x][y][z] = new Block(ids[x][y][z], (short) (xGr + x), (short) (yGr + y), (short) (zGr + z));
	}
	
	public Block[] getBlocks()
	{
		updated = false;
		byte index = 0;
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					if(blocks[x][y][z].updated)
					{
						btemp[index++] = blocks[x][y][z];
						blocks[x][y][z].updated = false;
						updated = true;
					}
					else
						index++;
		return btemp;
	}
}
