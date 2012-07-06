package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.PNGImageData;
import org.newdawn.slick.opengl.TextureLoader;

public class Render {
	
	// DEBUGGING ONLY:
	private boolean fogFlag = true;
	private int fogToggleTimer = 30;
	private Block[] bfreeze;
	
	private boolean shaderFlag = true;
	private int shaderProgram = 0, vertShader = 0, fragShader = 0;
	private int locTime;
	
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
		
		// Initialise shader program
		shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
		
		// If shader initialised, initialise and compile shaders
		if(shaderProgram != 0)
		{
			vertShader = createVertShader("src/shader/shader.vp");
			fragShader = createFragShader("src/shader/shader.fp");
		}
		else
			shaderFlag = false;
		
		// If shaders initialised and compiled, attach shaders to program, link, and verify
		if(vertShader != 0 && fragShader != 0)
		{
			ARBShaderObjects.glAttachObjectARB(shaderProgram, vertShader);
			ARBShaderObjects.glAttachObjectARB(shaderProgram, fragShader);
			
			ARBShaderObjects.glLinkProgramARB(shaderProgram);
			if(ARBShaderObjects.glGetObjectParameteriARB(shaderProgram, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE)
			{
				printLogInfo(shaderProgram);
				shaderFlag = false;
			}
			
			ARBShaderObjects.glValidateProgramARB(shaderProgram);
			if(ARBShaderObjects.glGetObjectParameteriARB(shaderProgram, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL_FALSE)
			{
				printLogInfo(shaderProgram);
				shaderFlag = false;
			}
		}
		else
			shaderFlag = false;
		
		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, Attr.DISPLAY_WIDTH, 0, Attr.DISPLAY_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
	    	glDepthFunc(GL_LEQUAL);

		glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		
		glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
		glEnable(GL_FOG);
			FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
			fogColor.put(0).put(0).put(0).put(1).flip();
			
			glFogi(GL_FOG_MODE, GL_EXP2);
			glFog(GL_FOG_COLOR, fogColor);
			glHint(GL_FOG_HINT, GL_DONT_CARE);
	    
        glClearColor(0, 0, 0, 1);
        
        glEnable(GL_POINT_SMOOTH);
			glPointSize(2);
		
		Prim.initLists();

		glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective(75, Attr.DISPLAY_WIDTH / Attr.DISPLAY_HEIGHT, 1, (int) (Attr.SIZE * 1.5));
		glMatrixMode(GL_MODELVIEW);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		glEnable(GL_SCISSOR_TEST);
			glScissor(0, 0, Attr.DISPLAY_WIDTH, Attr.DISPLAY_HEIGHT);
	}

	public void render(int time, Vector<Renderable> stack, Block[] blocks, Input input, Player player)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if(Attr.DEBUGGING && fogToggleTimer++ > 30 && Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			if(fogFlag)
			{
				bfreeze = new Block[blocks.length];
				for(int i = 0; i < blocks.length; i++)
					bfreeze[i] = blocks[i];
				//glDisable(GL_FOG);
				glDisable(GL_POINT_SMOOTH);
				glPointSize(1);
				fogFlag = false;
			} else
			{
				//glEnable(GL_FOG);
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
			
			if(shaderFlag)
			{
				ARBShaderObjects.glUseProgramObjectARB(shaderProgram);
				locTime = ARBShaderObjects.glGetUniformLocationARB(shaderProgram, "time");
				ARBShaderObjects.glUniform1fARB(locTime, time);
			}
			
			switch(r.type)
			{
				case CUBE:
					glCallList(Prim.listCube);
				break;
				
				case POINT:
					glCallList(Prim.listPoint);
				break;
			}
			
			ARBShaderObjects.glUseProgramObjectARB(0);
		}
		
		if(fogFlag)
		{
			//glEnable(GL_TEXTURE_2D);
			for(int b = 0; b < blocks.length; b++)
			{
				if(blocks[b] != null && blocks[b].id == Block.hm)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef(blocks[b].xGr * 64 - player.x, blocks[b].yGr * 64 - player.y, blocks[b].zGr * 64 - player.z);
					glScalef(64 / 2, 64 / 2, 64 / 2);
					glColor3f(1, 0, 0);
					glCallList(Prim.listCube);
				}
			}
		}
		else
		{
			glDisable(GL_TEXTURE_2D);
			for(int b = 0; b < blocks.length; b++)
			{
				if(bfreeze[b] != null && bfreeze[b].id == Block.hm)
				{
					glLoadIdentity();
					glRotatef(-player.rotY, 1, 0, 0);
					glRotatef(-player.rotX, 0, 1, 0);
					glTranslatef(bfreeze[b].xGr * 64 - player.x, bfreeze[b].yGr * 64 - player.y, bfreeze[b].zGr * 64 - player.z);
					glScalef(64 / 2, 64 / 2, 64 / 2);
					glColor3f(1, 0, 0);
					glCallList(Prim.listCube);
				}
			}
		}
		
		glFogf(GL_FOG_DENSITY, Math.min(0.1f, Math.max(0.0007f, 0.00000095f * (Attr.SIZE - player.y))));

		glLoadIdentity();
		gluLookAt(0, 0, 0, (float) (player.vecRotXZ[0] * player.vecRotY[1]), (float) (player.vecRotY[0]), (float) (player.vecRotXZ[1] * player.vecRotY[1]), 0, 1, 0);
	}
	
	public void cleanup()
	{
		//TODO: cleanup method
	}

	private int createVertShader(String filename)
	{
		vertShader = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		if(vertShader == 0)
			return 0;
		String vertexCode = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine()) != null)
				vertexCode += line + '\n';
		} catch(Exception e) {
			System.err.println("Failed to import vertex shader code.");
			return 0;
		}
		
		ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
		ARBShaderObjects.glCompileShaderARB(vertShader);
		if(ARBShaderObjects.glGetObjectParameteriARB(vertShader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
		{
			printLogInfo(vertShader);
			vertShader = 0;
		}
		
		return vertShader;
	}
	
	private int createFragShader(String filename)
	{
		fragShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		if(fragShader == 0)
			return 0;
		String fragCode = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine()) != null)
				fragCode += line + '\n';
		} catch(Exception e) {
			System.err.println("Failed to import fragment shader code.");
			return 0;
		}
		
		ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
		ARBShaderObjects.glCompileShaderARB(fragShader);
		if(ARBShaderObjects.glGetObjectParameteriARB(fragShader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
		{
			printLogInfo(fragShader);
			fragShader = 0;
		}
		
		return fragShader;
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
	
	private static boolean printLogInfo(int obj)
	{
		IntBuffer iVal = BufferUtils.createIntBuffer(1);
		ARBShaderObjects.glGetObjectParameterARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);
		
		int length = iVal.get();
		if(length > 1)
		{
			ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
			iVal.flip();
			ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			String out = new String(infoBytes);
			System.err.println("Info log:\n" + out);
		}
		else
			return true;
		return false;
	}
}
