package uk.co.homletmoo.hm001;

import org.lwjgl.input.Keyboard;

public class Attr {
	
	// Developer-set:
	//
		/** Use fullscreen or windowed */
		public static final boolean FULLSCREEN = false;
		/** Width of the display window */
		public static final int DISPLAY_WIDTH = 1280;
		/** Height of the display window */
		public static final int DISPLAY_HEIGHT = 720;
		/** Render distance in OpenGL units */
		public static final int RENDER_DISTANCE = 3072;
	
		/** Mouse X-sensitivity */
		public static final double SENS_X = 0.1;
		/** Mouse Y-sensitivity */
		public static final double SENS_Y = 0.15;

		/** Gravity */
		public static final float GRAV = 0.03f;
		
		/** Enumeration of renderable primitive types */
		public static enum PRIM {
									CUBE,
									POINT
								}; 
		
		/** Whether the program should run with debugging features enabled */
		public static final boolean DEBUGGING = true;
		
		
		// Block data structure constants:
		/** Chuck size in blocks */
		public static final int B_CHUNK_SIZE = 16;

		/** World height in chunks */
		public static final int B_WORLD_SIZEX = 3;

		/** World height in chunks */
		public static final int B_WORLD_HEIGHT = 3;

		/** World height in chunks */
		public static final int B_WORLD_SIZEZ = 2;

		/** Ground level in blocks */
		public static final int B_SEA_LEVEL = 32;

		/** Block size in OpenGL units */
		public static final int B_SIZE = 32;
		
		
		// Player constants:
		/** Speed of player walking */
		public static final float P_SPEED = 0.15f;
		
		/** Speed of player walking */
		public static final float P_SPEED_JUMP = 6;

		/** XZ Size of player */
		public static final float P_SIZE_XZ = 16;

		/** Height of player */
		public static final float P_SIZE_Y = 48;
		
		/** Control for walking forwards */
		public static final int P_C_FORWARD = Keyboard.KEY_W;
		
		/** Control for walking backwards */
		public static final int P_C_BACKWARD = Keyboard.KEY_S;
		
		/** Control for strafing left */
		public static final int P_C_LEFT = Keyboard.KEY_A;
		
		/** Control for strafing right */
		public static final int P_C_RIGHT = Keyboard.KEY_D;
		
		/** Control for jumping */
		public static final int P_C_JUMP = Keyboard.KEY_SPACE;
		
		/** Control for crouching */
		public static final int P_C_CROUCH = Keyboard.KEY_LSHIFT;
	
	
	// Determined at compile-time:
	//
		/** X-centre of the display window */
		public static final int DISPLAY_HALFWIDTH = DISPLAY_WIDTH / 2;
		/** Y-centre of the display window */
		public static final int DISPLAY_HALFHEIGHT = DISPLAY_HEIGHT / 2;
		/** World height in blocks */
		public static final int B_WORLD_SIZEX_BL = B_WORLD_SIZEX * B_CHUNK_SIZE;
		/** World height in blocks */
		public static final int B_WORLD_HEIGHT_BL = B_WORLD_HEIGHT * B_CHUNK_SIZE;
		/** World height in blocks */
		public static final int B_WORLD_SIZEZ_BL = B_WORLD_SIZEZ * B_CHUNK_SIZE;
}
