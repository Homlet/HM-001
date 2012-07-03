package uk.co.homletmoo.hm001;

import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {
	
	/** Time at first frame (for calulating runtime) */
	public long firstFrame;
	
	/** Time at last frame */
	public long lastFrame;
	
	/** Renderer object */
	public Render render;
	
	/** Input handler */
	public Input input;
	
	/** Currently active state, update called in loop */
	public State state;
	
	/** Vector containing all renderable objects */
	public Vector<Renderable> stack = new Vector<Renderable>();
	
	public static void main(String[] args)
	{
		new Main().start();
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
		
		lastFrame = firstFrame = getTime();
		
		render = new Render();
		render.init();
		
		input = new Input();
		input.pollInput();
		
		state = new State();
		
		loop();
	}
	
	/** Main logical loop */
	public void loop()
	{
		while(!Display.isCloseRequested() && !input.keys[Keyboard.KEY_ESCAPE])
		{
			int delta = getDelta();
			update(delta);
			System.out.println("Rendering...");
			
			render.render((int)(getTime() - firstFrame), stack, state.blocks, input, state.player);
			System.out.println("Updating...");

			input.pollInput();
			Display.update();
			Display.sync(60);
		}
		
		input.ungrab();
		render.cleanup();
		Display.destroy();
		System.exit(0);
	}
	
	/** Update call: calls updates for states and pan-state objects */
	public void update(int delta)
	{
		state.update(delta, input);
		stack.clear();
		stack.addAll(state.stack);
	}
	
	/** Calculates the number of milliseconds since the last frame */
	public int getDelta()
	{
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	/** Returns the time in milliseconds */
	public static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

}
