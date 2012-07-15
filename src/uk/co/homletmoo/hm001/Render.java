package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import static uk.co.homletmoo.hm001.Attr.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.PNGImageData;
import org.newdawn.slick.opengl.TextureLoader;

public class Render {
	
	// DEBUGGING ONLY:
	private boolean fogFlag = false;
	private int fogToggleTimer = 30;
	
	public void init()
	{
		// Load textures
		try {
			Tex.icon = TextureLoader.getTexture("PNG", new FileInputStream("src/res/favicon.png"));
			Tex.logo = TextureLoader.getTexture("PNG", new FileInputStream("src/res/HMXLV2.png"));
			Display.setIcon(assignIcon("src/res/favicon.png"));
		} catch (Exception e) {
			System.err.println("Textures did not load properly.");
			Display.destroy();
			System.exit(1);
		}
		
		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
	    	glDepthFunc(GL_LEQUAL);

		glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		
		glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
		//glEnable(GL_FOG);
			FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
			fogColor.put(0.55f).put(0.75f).put(0.9f).put(0.9f).flip();
			
			glFogi(GL_FOG_MODE, GL_EXP);
			glFogf(GL_FOG_DENSITY, 0.000075f);
			glFog(GL_FOG_COLOR, fogColor);
			glHint(GL_FOG_HINT, GL_DONT_CARE);
	    
			glClearColor(0.55f, 0.75f, 0.9f, 1);
			
        glEnable(GL_POINT_SMOOTH);
			glPointSize(2);
		
		Prim.initLists();

		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective(90, DISPLAY_WIDTH / DISPLAY_HEIGHT, 1, SIZE);
		glMatrixMode(GL_MODELVIEW);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		glEnable(GL_SCISSOR_TEST);
			glScissor(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
	}

	public void render(int time, Vector<Renderable> stack, Block[] blocks, Input input, Player player)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if(DEBUGGING && fogToggleTimer++ > 30 && Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			if(fogFlag)
			{
				glDisable(GL_FOG);
				glDisable(GL_POINT_SMOOTH);
				glPointSize(1);
				fogFlag = false;
			} else
			{
				glEnable(GL_FOG);
				glEnable(GL_POINT_SMOOTH);
				glPointSize(2);
				fogFlag = true;
			}

			fogToggleTimer = 0;
		}
		
		Iterator<Renderable> i = stack.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			glLoadIdentity();
			glRotatef(-player.rotY, 1, 0, 0);
			glRotatef(-player.rotX, 0, 1, 0);
			glTranslatef(r.x - player.x, r.y - player.y, r.z - player.z);
			glScalef(r.width / 2, r.height / 2, r.depth / 2);
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
					glCallList(Prim.cube);
				break;
				
				case POINT:
					glCallList(Prim.point);
				break;
			}
			
			glDisable(GL_TEXTURE_2D);
		}
		
		for(int m = 0; m < blocks.length; m++)
		{
			if(blocks[m] != null)
			{
				glLoadIdentity();
				glRotatef(-player.rotY, 1, 0, 0);
				glRotatef(-player.rotX, 0, 1, 0);
				glTranslatef(blocks[m].x * B_SIZE - player.x, blocks[m].y * B_SIZE - player.y, blocks[m].z * B_SIZE - player.z);
				glScalef(B_SIZE / 2, B_SIZE / 2, B_SIZE / 2);
				glColor3f(blocks[m].r, blocks[m].g, blocks[m].b);
				
				if(blocks[m].all)
				{
					glCallList(Prim.cube);
				}
				else
				{
					if(blocks[m].xp)
						glCallList(Prim.qxp);
					if(blocks[m].xn)
						glCallList(Prim.qxn);
					if(blocks[m].yp)
						glCallList(Prim.qyp);
					if(blocks[m].yn)
						glCallList(Prim.qyn);
					if(blocks[m].zp)
						glCallList(Prim.qzp);
					if(blocks[m].zn)
						glCallList(Prim.qzn);
				}
			}
		}

		glLoadIdentity();
		gluLookAt(0, 0, 0, (float) (player.vecRotXZ[0] * player.vecRotY[1]), (float) (player.vecRotY[0]), (float) (player.vecRotXZ[1] * player.vecRotY[1]), 0, 1, 0);
	}
	
	public void cleanup()
	{
		//TODO: cleanup method
	}
	
	private ByteBuffer[] assignIcon(String path)
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
	
	public static float dotVector3f(float[] v1, float[] v2)
	{
		return (v1[0] * v2[0]) + (v1[1] * v2[1]) + (v1[2] * v2[2]);
	}
	
	public static float[] subVector3f(float[] v1, float[] v2)
	{
		return new float[] { v1[0] - v2[0], v1[1] - v2[1], v1[2] - v2[2] };
	}
}
