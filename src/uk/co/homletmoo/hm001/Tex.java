package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;

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
	
	// Block faces: ------------------------------
		// TNT:
		public static int b_tnt_TOP;
		public static int b_tnt_SIDE;
		public static int b_tnt_BASE;

		// Chest:
		public static int b_chest_TOP_BASE;
		public static int b_chest_SIDE;
		public static int b_chest_FRONT;
	
	public static void init()
	{
		try {
			icon = loadTexture(16, 16, loadImage("src/res/favicon.png"));
			logo = loadTexture(32, 32, loadImage("src/res/HMXLV2.png"));
			Display.setIcon(assignIcon("src/res/favicon.png"));
			
			ByteBuffer blocks = loadImage("src/res/blocks.png");
			{
				int width, height;
				width = height = 16;

				b_tnt_TOP = loadSubTexture(9 * width, 0 * height, width, height, 256, blocks);
				b_tnt_SIDE = loadSubTexture(8 * width, 0 * height, width, height, 256, blocks);
				b_tnt_BASE = loadSubTexture(10 * width, 0 * height, width, height, 256, blocks);

				b_chest_TOP_BASE = loadSubTexture(9 * width, 1 * height, width, height, 256, blocks);
				b_chest_SIDE = loadSubTexture(10 * width, 1 * height, width, height, 256, blocks);
				b_chest_FRONT = loadSubTexture(11 * width, 1 * height, width, height, 256, blocks);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(-1);
		}
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
		glPixelStorei(GL_UNPACK_ROW_LENGTH, imageWidth);
		glPixelStorei(GL_UNPACK_SKIP_PIXELS, xOffset);
		glPixelStorei(GL_UNPACK_SKIP_ROWS, yOffset);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
		glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
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
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
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
