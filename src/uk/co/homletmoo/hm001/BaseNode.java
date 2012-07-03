package uk.co.homletmoo.hm001;

import java.util.Random;

public class BaseNode {

	private Block[][][] blocks = new Block[2][2][2];
	private Block[] btemp = new Block[8];
	private Random rand;
	private byte testId;
	private short xGr, yGr, zGr;
	public boolean updated = true;
	
	public BaseNode(short xGr, short yGr, short zGr)
	{
		this.xGr = xGr;
		this.yGr = yGr;
		this.zGr = zGr;
		
		rand = new Random();
		byte[][][] ids = new byte[][][] {{{Block.air, Block.air},
											{(byte) (rand.nextInt(2) - 128), (byte) (rand.nextInt(2) - 128)}},
										{{Block.air, (byte) (rand.nextInt(2) - 128)},
											{Block.air, Block.air}}};
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					blocks[x][y][z] = new Block(ids[x][y][z], (short) (xGr + x), (short) (yGr + y), (short) (zGr + z));
	}
	
	public Block[] getBlocks()
	{
		testId = blocks[0][0][0].id;
		idtest:
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					if(blocks[x][y][z].id != testId)
						break idtest;
					else if(x == 1 && y == 1 && z == 1)
					{
						btemp[0] = new Block(blocks[0][0][0].id, xGr, yGr, zGr, (short) 2);
					}
		
		short index = 0;
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					if(blocks[x][y][z].updated)
					{
						btemp[index++] = blocks[x][y][z];
						updated = true;
					}
					else
						index++;
		return btemp;
	}
}
