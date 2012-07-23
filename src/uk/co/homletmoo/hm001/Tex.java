package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.PNGImageData;

public class Tex {
	
	/** HM logo in icon format (16x16) */
	public static int icon;
	/** Classic HM logo (32x32) */
	public static int logo;
	
	// Block faces: --------------------------------------------------
		// Grass and dirt:
		public static int b_grass_TOP;
		public static int b_grass_SIDE;
		public static int b_dirt_ALL;
		
		// Stone and rocks:
		public static int b_rocks_ALL;
		public static int b_stone_ALL;
	
	// Cube texture indexes: -----------------------------------------
	public static Tex[] blockTextures = new Tex[Block.TYPE_LENGTH];
	
	public int xp, xn, yp, yn, zp, zn;
	public Tex(int xp, int xn, int yp, int yn, int zp, int zn)
	{
		this.xp = xp;
		this.xn = xn;
		this.yp = yp;
		this.yn = yn;
		this.zp = zp;
		this.zn = zn;
	}
	
	public static void init()
	{
		try {
			if(Attr.DEBUGGING)
			{
				icon = loadTexture(16, 16, loadImage("src/res/favicon.png"));
				logo = loadTexture(32, 32, loadImage("src/res/HMXLV2.png"));
				Display.setIcon(assignIcon("src/res/favicon.png"));
				
				ByteBuffer blocks = loadImage("src/res/blocks.png");
				{
					int width, height;
					width = height = 16;
	
					b_grass_TOP = loadSubTexture(0 * width, 0 * height, width, height, 128, blocks);
					b_grass_SIDE = loadSubTexture(1 * width, 0 * height, width, height, 128, blocks);
					b_dirt_ALL = loadSubTexture(2 * width, 0 * height, width, height, 128, blocks);
					b_rocks_ALL = loadSubTexture(3 * width, 0 * height, width, height, 128, blocks);
					b_stone_ALL = loadSubTexture(4 * width, 0 * height, width, height, 128, blocks);
				}
			}else
			{
				icon = loadTexture(16, 16, loadImage("res/favicon.png"));
				logo = loadTexture(32, 32, loadImage("res/HMXLV2.png"));
				Display.setIcon(assignIcon("res/favicon.png"));
				
				ByteBuffer blocks = loadImage("res/blocks.png");
				{
					int width, height;
					width = height = 16;
	
					b_grass_TOP = loadSubTexture(0 * width, 0 * height, width, height, 128, blocks);
					b_grass_SIDE = loadSubTexture(1 * width, 0 * height, width, height, 128, blocks);
					b_dirt_ALL = loadSubTexture(2 * width, 0 * height, width, height, 128, blocks);
					b_rocks_ALL = loadSubTexture(3 * width, 0 * height, width, height, 128, blocks);
					b_stone_ALL = loadSubTexture(4 * width, 0 * height, width, height, 128, blocks);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(-1);
		}

		blockTextures[Block.TYPE_GRASS + 128] = new Tex(b_grass_SIDE, b_grass_SIDE, b_grass_TOP, b_dirt_ALL, b_grass_SIDE, b_grass_SIDE);
		blockTextures[Block.TYPE_DIRT + 128] = new Tex(b_dirt_ALL, b_dirt_ALL, b_dirt_ALL, b_dirt_ALL, b_dirt_ALL, b_dirt_ALL);
		blockTextures[Block.TYPE_ROCKS + 128] = new Tex(b_rocks_ALL, b_rocks_ALL, b_rocks_ALL, b_rocks_ALL, b_rocks_ALL, b_rocks_ALL);
		blockTextures[Block.TYPE_STONE + 128] = new Tex(b_stone_ALL, b_stone_ALL, b_stone_ALL, b_stone_ALL, b_stone_ALL, b_stone_ALL);
		blockTextures[Block.TYPE_WATER + 128] = new Tex(0, 0, 0, 0, 0, 0);
	}
	
	private static int loadSubTexture(int xOffset, int yOffset, int width, int height, int imageWidth, ByteBuffer imageData)
	{
		imageData.rewind();
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		glGenTextures(tmp);
		tmp.rewind();
		glBindTexture(GL_TEXTURE_2D, tmp.get(0));
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glPixelStorei(GL_UNPACK_ROW_LENGTH, imageWidth);
		glPixelStorei(GL_UNPACK_SKIP_PIXELS, xOffset);
		glPixelStorei(GL_UNPACK_SKIP_ROWS, yOffset);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
		glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
		glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
		glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
		glGenerateMipmap(GL_TEXTURE_2D);
		tmp.rewind();
		return tmp.get(0);
	}
	
	private static int loadTexture(int width, int height, ByteBuffer imageData)
	{
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		glGenTextures(tmp);
		tmp.rewind();
		glBindTexture(GL_TEXTURE_2D, tmp.get(0));
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
		glGenerateMipmap(GL_TEXTURE_2D);
		tmp.rewind();
		return tmp.get(0);
	}
	
	public static ByteBuffer loadImage(String path)
	{
		try {
			ByteBuffer buffer = new PNGImageData().loadImage(new FileInputStream(path));
			ByteBuffer finalBuffer = BufferUtils.createByteBuffer(256 * 256 * 4);
			finalBuffer.put(buffer);
			finalBuffer.rewind();
			return finalBuffer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return null;
	}

	private static ByteBuffer[] assignIcon(String path)
	{
		try {
			ByteBuffer buffIcon = new PNGImageData().loadImage(new FileInputStream(path));
			ByteBuffer[] buffIcons = new ByteBuffer[] { buffIcon };
			return buffIcons;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return null;
	}
}
