package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Render {
	
	public void init()
	{
		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, Attr.DISPLAY_WIDTH, 0, Attr.DISPLAY_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		try {
			// TODO: More flexible icon code
			Tex.icon = TextureLoader.getTexture("PNG", new FileInputStream("src/res/favicon.png"));
			Display.setIcon(assignIcon(Tex.icon));
			Tex.logo = TextureLoader.getTexture("PNG", new FileInputStream("src/res/HMXLV2.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		glClearDepth(1);
		glEnable(GL_DEPTH_TEST);
	    	glDepthFunc(GL_LESS);
		
		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective(90, Attr.DISPLAY_WIDTH / Attr.DISPLAY_HEIGHT, 1, Attr.SIZE);
		glMatrixMode(GL_MODELVIEW);

		glCullFace(GL_BACK);
		
		glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_FOG);
            FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
            fogColor.put(0.1f).put(0.01f).put(0).put(1).flip();

            glFogi(GL_FOG_MODE, GL_EXP);
            glFog(GL_FOG_COLOR, fogColor);
            glFogf(GL_FOG_DENSITY, 0.0003f);
            glHint(GL_FOG_HINT, GL_DONT_CARE);
            glClearColor(0.1f, 0.01f, 0, 1);
		
		Prim.initLists();
	}
	
	public void render(Vector<Renderable> stack)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Iterator<Renderable> i = stack.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			glLoadIdentity();
			gluLookAt(Attr.HALFSIZE, Attr.HALFSIZE, 1, Attr.HALFSIZE, Attr.HALFSIZE, Attr.HALFSIZE, 0, 1, 0);
			glTranslatef(r.x, r.y, r.z);
			glScalef(r.width, r.height, r.depth);
			glColor3f(r.r, r.g, r.b);
			if(r.tex != null)
			{
				glEnable(GL_TEXTURE_2D);
				r.tex.bind();
			} else
				glDisable(GL_TEXTURE_2D);
			
			switch(r.type)
			{
				case CUBE:
					glCallList(Prim.listCube);
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
