package uk.co.homletmoo.hm001;

public class Attr {
	
	// Developer-set:
	/** Width of the display window */
	public static final int DISPLAY_WIDTH = 1280;
	/** Height of the display window */
	public static final int DISPLAY_HEIGHT = 720;
	/** Size of render zone */
	public static final int SIZE = 8192;
	
	/** Whether the program should run with debugging features enabled */
	public static final boolean DEBUGGING = true;
	
	/** Enumeration of renderable primitive types */
	public static enum TYPE { CUBE };
	
	
	// Determined at compile-time:
	/** X-centre of the display window */
	public static final int DISPLAY_HALFWIDTH = DISPLAY_WIDTH / 2;
	/** Y-centre of the display window */
	public static final int DISPLAY_HALFHEIGHT = DISPLAY_HEIGHT / 2;
	/** Centre of the render zone */
	public static final int HALFSIZE = SIZE / 2;
}
