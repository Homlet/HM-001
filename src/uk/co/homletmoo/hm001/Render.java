package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Render {

	private int listSquare;
	
	public void init()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Attr.DISPLAY_WIDTH, 0, Attr.DISPLAY_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		try {
			// TODO: More flexible icon code
			Tex.icon = TextureLoader.getTexture("PNG", new FileInputStream("src/res/favicon.png"));
			Display.setIcon(assignIcon(Tex.icon));
			Tex.logo = TextureLoader.getTexture("PNG", new FileInputStream("src/res/HMXLV2.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		listSquare = glGenLists(1);
		glNewList(listSquare, GL_COMPILE);
			glBegin(GL_QUADS);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				glTexCoord2f(0, 1);		glVertex2f(0, 0);
				glTexCoord2f(1, 1);		glVertex2f(1, 0);
				glTexCoord2f(1, 0);		glVertex2f(1, 1);
				glTexCoord2f(0, 0);		glVertex2f(0, 1);
			glEnd();
		glEndList();
	}
	
	public void render(Vector<Renderable> stack)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Iterator<Renderable> i = stack.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			glLoadIdentity();
			glTranslatef(r.x, r.y, 0);
			glScalef(r.width, r.height, 1);
			glColor3f(r.r, r.g, r.b);
			if(r.tex != null)
			{
				glEnable(GL_TEXTURE_2D);
				r.tex.bind();
			} else
				glDisable(GL_TEXTURE_2D);
			
			switch(r.type)
			{
				case SQUARE:
					glCallList(listSquare);
				break;
			}
		}
	}
	
	private ByteBuffer[] assignIcon(Texture icon)
	{
		int drawBuffer = glGetInteger(GL_DRAW_BUFFER);
		
		glDrawBuffer(GL_BACK);
		icon.bind();
		glEnable(GL_TEXTURE_2D);
			glBegin(GL_QUADS);
				glTexCoord2f(0, 0);		glVertex2i(0, 0);
				glTexCoord2f(1, 0);		glVertex2i(16, 0);
				glTexCoord2f(1, 1);		glVertex2i(16, 16);
				glTexCoord2f(0, 1);		glVertex2i(0, 16);
			glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glReadBuffer(GL_BACK);
		ByteBuffer bufferedIcon = BufferUtils.createByteBuffer(16 * 16 * 4);
		glReadPixels(0, 0, 16, 16, GL_RGBA, GL_BYTE, bufferedIcon);
		
		for(int y = 0; y < 16; y++)
			for(int x = 0; x < 16; x++)
			{
				int index = y * 4 * 16 + x * 4;
				bufferedIcon.put(index + 3, (byte) 255);
				bufferedIcon.put(index, (byte)(bufferedIcon.get(index) * 3));
				bufferedIcon.put(index + 1, (byte)(bufferedIcon.get(index + 1) * 3));
				bufferedIcon.put(index + 2, (byte)(bufferedIcon.get(index + 2) * 3));
			}
		
		glDrawBuffer(drawBuffer);
		
		ByteBuffer[] bufferedIcons = new ByteBuffer[1];
		bufferedIcons[0] = bufferedIcon;
		return bufferedIcons;
	}
}
