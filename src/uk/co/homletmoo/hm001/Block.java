package uk.co.homletmoo.hm001;

public class Block {

	// Block types:
	public static final byte air = (byte) -128;
	public static final byte hm = (byte) -127;
	
	public byte id;
	public short xGr, yGr, zGr, size;
	public boolean updated = true;
	
	public Block(byte id, short xGr, short yGr, short zGr, short size)
	{
		this.id = id;
		this.xGr = xGr;
		this.yGr = yGr;
		this.zGr = zGr;
		this.size = size;
	}
	
	public Block(byte id, short xGr, short yGr, short zGr)
	{
		this.id = id;
		this.xGr = xGr;
		this.yGr = yGr;
		this.zGr = zGr;
		this.size = 1;
	}
}
