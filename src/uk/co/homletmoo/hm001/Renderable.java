package uk.co.homletmoo.hm001;

import org.newdawn.slick.opengl.Texture;

/** Stores data for drawing an OpenGL primitive */
public class Renderable {
	
	public Attr.TYPE type;
	public float x = 0, y = 0, width, height;
	public float offsetX, offsetY;
	public float r, g, b;
	public Texture tex;
	
	/**
	 * 
	 * @param type An Attr.TYPE integer describing the sort of primitive
	 * @param x Starting x point
	 * @param y Starting y point
	 * @param width Width of primitive
	 * @param height Height of primitive
	 * @param r Red value of colour, from 0 to 1
	 * @param g Green value of colour, from 0 to 1
	 * @param b Blue value of colour, from 0 to 1
	 * @param tex Texture to use from the Tex class. Set as null for no texture
	 */
	public Renderable(Attr.TYPE type, float x, float y, float width, float height, float r, float g, float b, Texture tex)
	{
		this.type = type;
		this.offsetX = x;
		this.offsetY = y;
		this.width = width;
		this.height = height;
		this.r = r;
		this.g = g;
		this.b = b;
		this.tex = tex;
	}
}
