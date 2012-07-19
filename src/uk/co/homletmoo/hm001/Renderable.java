package uk.co.homletmoo.hm001;

/** Stores data for drawing an OpenGL primitive */
public class Renderable {
	
	public Attr.TYPE type;
	public float x = 0, y = 0, z = 0;
	public float width, height, depth;
	public float offsetX, offsetY, offsetZ;
	public float r, g, b;
	public int tex;
	
	/**
	 * 
	 * @param type An Attr.TYPE integer describing the sort of primitive
	 * @param x Starting x point
	 * @param y Starting y point
	 * @param z Starting z point
	 * @param width Width of primitive
	 * @param height Height of primitive
	 * @param depth Depth of primitive
	 * @param r Red value of colour, from 0 to 1
	 * @param g Green value of colour, from 0 to 1
	 * @param b Blue value of colour, from 0 to 1
	 * @param tex Integer texture pointer to use from the Tex class. Set as 0 for no texture
	 */
	public Renderable(Attr.TYPE type, float x, float y, float z, float width, float height, float depth, float r, float g, float b, int tex)
	{
		this.type = type;
		this.offsetX = x;
		this.offsetY = y;
		this.offsetZ = z;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.r = r;
		this.g = g;
		this.b = b;
		this.tex = tex;
	}
}
