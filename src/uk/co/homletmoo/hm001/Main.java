package uk.co.homletmoo.hm001;

import java.util.Random;
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
	
	/** Master random instance */
	private Random rand = new Random();

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
			if(Attr.FULLSCREEN)
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			else
				Display.setDisplayMode(new DisplayMode(Attr.DISPLAY_WIDTH, Attr.DISPLAY_HEIGHT));
			
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
		
		state = new State(rand);
		
		loop();
	}
	
	/** Main logical loop */
	public void loop()
	{
		while(!Display.isCloseRequested() && (!input.keys[Keyboard.KEY_ESCAPE] || !input.keys[Keyboard.KEY_GRAVE]))
		{
			if(input.pressed(Keyboard.KEY_ESCAPE))
				if(input.grabbed)
					input.ungrab();
				else
					input.grab();
			
			int delta = getDelta();
			System.out.print(delta + " ms / " + 1.0f / delta * 1000 + "fps\n");
			update(delta);
			
			System.out.print("Rendering... ");
			render.render((int)(getTime() - firstFrame), stack, state.blocks, input, state.player, rand);
			System.out.print("Updating... ");
			
			input.pollInput();
			if(input.grabbed)
				input.setPos(Attr.DISPLAY_HALFWIDTH, Attr.DISPLAY_HALFHEIGHT);
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
		state.update(delta, input, rand, state.player);
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
