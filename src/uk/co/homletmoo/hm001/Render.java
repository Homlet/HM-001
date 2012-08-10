package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import static uk.co.homletmoo.hm001.Attr.*;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

public class Render {
	
	// DEBUGGING ONLY:
	private boolean fogFlag = false;
	private int fogToggleTimer = 30;
	
	public void init()
	{
		// Load textures
		glEnable(GL_TEXTURE_2D);
			Tex.init();
		glDisable(GL_TEXTURE_2D);
		
		glEnable(GL_DEPTH_TEST);
	    	glDepthFunc(GL_LEQUAL);

		glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		
		glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
		glEnable(GL_FOG);
			FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
			fogColor.put(0.55f).put(0.75f).put(1).put(1).flip();
			
			glFogi(GL_FOG_MODE, GL_EXP2);
			glFogf(GL_FOG_DENSITY, 0.00075f);
			glFog(GL_FOG_COLOR, fogColor);
			glHint(GL_FOG_HINT, GL_DONT_CARE);
	    
			glClearColor(0.55f, 0.75f, 0.9f, 1);
			
        glEnable(GL_POINT_SMOOTH);
			glPointSize(2);
		
		glLineWidth(2.5f);
		
		Prim.initLists();

		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective(90, (float) DISPLAY_WIDTH / (float) DISPLAY_HEIGHT, 1, RENDER_DISTANCE);
		glMatrixMode(GL_MODELVIEW);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		glEnable(GL_SCISSOR_TEST);
			glScissor(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
	}

	public void render(int time, Vector<Renderable> stack, Block[] blocks, Input input, Player player, Random rand)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		
		
		// Toggle debugging features -------------------------
		if(DEBUGGING && fogToggleTimer++ > 30 && Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			if(fogFlag)
			{
				glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				glDisable(GL_FOG);
				glDisable(GL_CULL_FACE);
				glDisable(GL_POINT_SMOOTH);
				glPointSize(1);
				fogFlag = false;
			} else
			{
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				glEnable(GL_FOG);
				glEnable(GL_CULL_FACE);
				glEnable(GL_POINT_SMOOTH);
				glPointSize(2);
				fogFlag = true;
			}

			fogToggleTimer = 0;
		}
		
		// Render entities -----------------------------------
		Iterator<Renderable> i = stack.iterator();
		while(i.hasNext())
		{
			Renderable r = i.next();
			glLoadIdentity();
			glRotatef(-player.rotY, 1, 0, 0);
			glRotatef(-player.rotX, 0, 1, 0);
			glTranslatef(r.x - player.p.x, r.y - player.p.y, r.z - player.p.z);
			glScalef(r.width / 2, r.height / 2, r.depth / 2);
			glColor3f(r.r, r.g, r.b);
        	
			if(r.tex != 0)
			{
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, r.tex);
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
		}
		glDisable(GL_TEXTURE_2D);
		
		// Render blocks -------------------------------------
		int blockCount = 0, index = 0;
		for(int t = 1; t < Block.TYPE_LENGTH; t++)
		{
			int lastIndex = index;
			if(t == Block.TYPE_WATER + 128)
			{
				glDisable(GL_TEXTURE_2D);
				glColor4f(0.9f, 0.9f, 1, 0.5f);
			}
			else
			{
				glEnable(GL_TEXTURE_2D);
				glColor4f(1, 1, 1, 1);
			}
			
			glBindTexture(GL_TEXTURE_2D, Tex.blockTextures[t].xp);
			for(int m = index; m < blocks.length; m++)
			{
				if(blocks[m].type != t - 128
				|| m == blocks.length - 1)
				{
					index = m;
					break;
				}
				blockCount++;
				if(blocks[m].xp)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef((blocks[m].p.x + 0.5f) * B_SIZE - player.p.x, (blocks[m].p.y + 0.5f * blocks[m].sy) * B_SIZE - player.p.y, (blocks[m].p.z + 0.5f * blocks[m].sz) * B_SIZE - player.p.z);
					glScalef(B_SIZE / 2, (B_SIZE * blocks[m].sy) / 2, (B_SIZE * blocks[m].sz) / 2);
					glMatrixMode(GL_TEXTURE);
					glLoadIdentity();
					glScalef((float) blocks[m].sz, (float) blocks[m].sy, 0);
					glMatrixMode(GL_MODELVIEW);
					glCallList(Prim.qxp);
				}
			}

			glBindTexture(GL_TEXTURE_2D, Tex.blockTextures[t].xn);
			for(int m = lastIndex; m < index - 1; m++)
			{
				if(blocks[m].xn)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef((blocks[m].p.x + 0.5f) * B_SIZE - player.p.x, (blocks[m].p.y + 0.5f * blocks[m].sy) * B_SIZE - player.p.y, (blocks[m].p.z + 0.5f * blocks[m].sz) * B_SIZE - player.p.z);
					glScalef(B_SIZE / 2, (B_SIZE * blocks[m].sy) / 2, (B_SIZE * blocks[m].sz) / 2);
					glMatrixMode(GL_TEXTURE);
					glLoadIdentity();
					glScalef(blocks[m].sz, (float) blocks[m].sy, 0);
					glMatrixMode(GL_MODELVIEW);
					glCallList(Prim.qxn);
				}
			}

			glBindTexture(GL_TEXTURE_2D, Tex.blockTextures[t].yp);
			for(int m = lastIndex; m < index - 1; m++)
			{
				if(blocks[m].yp)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef((blocks[m].p.x + 0.5f) * B_SIZE - player.p.x, (blocks[m].p.y + 0.5f * blocks[m].sy) * B_SIZE - player.p.y, (blocks[m].p.z + 0.5f * blocks[m].sz) * B_SIZE - player.p.z);
					glScalef(B_SIZE / 2, (B_SIZE * blocks[m].sy) / 2, (B_SIZE * blocks[m].sz) / 2);
					glMatrixMode(GL_TEXTURE);
					glLoadIdentity();
					glScalef(1, (float) blocks[m].sz, 0);
					glMatrixMode(GL_MODELVIEW);
					glCallList(Prim.qyp);
				}
			}

			glBindTexture(GL_TEXTURE_2D, Tex.blockTextures[t].yn);
			for(int m = lastIndex; m < index - 1; m++)
			{
				if(blocks[m].yn)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef((blocks[m].p.x + 0.5f) * B_SIZE - player.p.x, (blocks[m].p.y + 0.5f * blocks[m].sy) * B_SIZE - player.p.y, (blocks[m].p.z + 0.5f * blocks[m].sz) * B_SIZE - player.p.z);
					glScalef(B_SIZE / 2, (B_SIZE * blocks[m].sy) / 2, (B_SIZE * blocks[m].sz) / 2);
					glMatrixMode(GL_TEXTURE);
					glLoadIdentity();
					glScalef(1, (float) blocks[m].sz, 0);
					glMatrixMode(GL_MODELVIEW);
					glCallList(Prim.qyn);
				}
			}

			glBindTexture(GL_TEXTURE_2D, Tex.blockTextures[t].zp);
			for(int m = lastIndex; m < index - 1; m++)
			{
				if(blocks[m].zp)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef((blocks[m].p.x + 0.5f) * B_SIZE - player.p.x, (blocks[m].p.y + 0.5f * blocks[m].sy) * B_SIZE - player.p.y, (blocks[m].p.z + 0.5f * blocks[m].sz) * B_SIZE - player.p.z);
					glScalef(B_SIZE / 2, (B_SIZE * blocks[m].sy) / 2, (B_SIZE * blocks[m].sz) / 2);
					glMatrixMode(GL_TEXTURE);
					glLoadIdentity();
					glScalef(1, (float) blocks[m].sy, 0);
					glMatrixMode(GL_MODELVIEW);
					glCallList(Prim.qzp);
				}
			}

			glBindTexture(GL_TEXTURE_2D, Tex.blockTextures[t].zn);
			for(int m = lastIndex; m < index - 1; m++)
			{
				if(blocks[m].zn)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef((blocks[m].p.x + 0.5f) * B_SIZE - player.p.x, (blocks[m].p.y + 0.5f * blocks[m].sy) * B_SIZE - player.p.y, (blocks[m].p.z + 0.5f * blocks[m].sz) * B_SIZE - player.p.z);
					glScalef(B_SIZE / 2, (B_SIZE * blocks[m].sy) / 2, (B_SIZE * blocks[m].sz) / 2);
					glMatrixMode(GL_TEXTURE);
					glLoadIdentity();
					glScalef(1, (float) blocks[m].sy, 0);
					glMatrixMode(GL_MODELVIEW);
					glCallList(Prim.qzn);
				}
			}
		}
		System.out.print("Bls: " + blockCount + " ");
		glDisable(GL_TEXTURE_2D);

		glLoadIdentity();
		gluLookAt(0, 0, 0, (float) (player.vecRotXZ.x * player.vecRotY.y), (float) (player.vecRotY.x), (float) (player.vecRotXZ.y * player.vecRotY.y), 0, 1, 0);
	}
	
	public void cleanup()
	{
		//TODO: cleanup method
	}
}
