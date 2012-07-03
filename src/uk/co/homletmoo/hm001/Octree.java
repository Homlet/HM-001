package uk.co.homletmoo.hm001;

public class Octree {
	
	private int levels;
	private Node[][][] children = new Node[2][2][2];
	private Block[] btemp;
	
	public Octree(int levels)
	{
		this.levels = levels;
		btemp = new Block[(int) Math.pow(8, levels)];

		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
					children[x][y][z] = new Node((byte) (levels - 1),
													(short) (x * Math.pow(2, levels - 1)),
													(short) (y * Math.pow(2, levels - 1)),
													(short) (z * Math.pow(2, levels - 1)));
	}
	
	public Block[] getBlocks()
	{
		short index = 0;
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++)
				{
					if(children[x][y][z].updated)
						for(int w = 0; w < (int) Math.pow(8, levels - 1); w++)
							btemp[index++] = children[x][y][z].getBlocks()[w];
				}
		return btemp;
	}
}
