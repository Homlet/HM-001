package uk.co.homletmoo.hm001;

public class Node {
	
	private Object[][][] children = new Object[2][2][2];
	private byte level;
	private Block[] btemp;
	public boolean updated = true;
	
	public Node(byte level, short xGr, short yGr, short zGr)
	{
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
		short index = 0;
		if(level > 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
					{
						if(((Node) children[x][y][z]).updated)
							for(int w = 0; w < (int) Math.pow(8, level - 1); w++)
								btemp[index++] = ((Node) children[x][y][z]).getBlocks()[w];
						else
							index += (int) Math.pow(8, level - 1);
					}
		else if(level == 2)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++)
					{
						if(((BaseNode) children[x][y][z]).updated)
							for(int w = 0; w < 8; w++)
								btemp[index++] = ((BaseNode) children[x][y][z]).getBlocks()[w];
						else
							index += 8;
					}
		updated = false;
		return btemp;
	}
}
