package uk.co.homletmoo.hm001;

import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {
	
	/** Time at last frame */
	public long lastFrame;
	
	/** Renderer object */
	public Render render;
	
	/** Currently active state, update called in loop */
	public State state;
	
	/** Vector containing all renderable objects */
	public Vector<Renderable> stack = new Vector<Renderable>();
	
	public static void main(String[] args)
	{
		Main m = new Main();
		m.start();
	}
	
	/** Initialise the program */
	public void start()
	{
		// Create Display with resolution specified in Attr class
		try
		{
			Display.setDisplayMode(new DisplayMode(Attr.DISPLAY_WIDTH, Attr.DISPLAY_HEIGHT));
			//Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			Display.setTitle("HM OpenGL Test");
			Display.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		lastFrame = getTime();
		
		render  = new Render();
		render.init();
		
		state = new State();
		
		loop();
	}
	
	/** Main logical loop */
	public void loop()
	{
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			int delta = getDelta();
			update(delta);
			
			render.render(stack);
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
		System.exit(0);
	}
	
	/** Update call: calls updates for states and pan-state objects */
	public void update(int delta)
	{
		state.update(delta);
		stack.clear();
		stack.addAll(state.stack);
	}
	
	/** Returns the time in milliseconds */
	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/** Calculates the number of milliseconds since the last frame */
	public int getDelta()
	{
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		
		return delta;
	}

}
