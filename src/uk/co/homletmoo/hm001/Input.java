package uk.co.homletmoo.hm001;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {
	
	public boolean[] keys = new boolean[Keyboard.KEYBOARD_SIZE];
	public float mouseX, mouseY, mouseDX, mouseDY;
	public boolean mouseLeft, mouseRight, mouseMiddle;
	
	public Input()
	{
		grab();
	}
	
	public void pollInput()
	{
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();

		mouseDX = mouseX - Attr.DISPLAY_HALFWIDTH;
		mouseDY = mouseY - Attr.DISPLAY_HALFHEIGHT;
		
		Mouse.setCursorPosition(Attr.DISPLAY_HALFWIDTH, Attr.DISPLAY_HALFHEIGHT);
		
		while(Mouse.next())
			if(Mouse.getEventButton() == 0)
				mouseLeft = Mouse.getEventButtonState();
			else if(Mouse.getEventButton() == 1)
				mouseRight = Mouse.getEventButtonState();
			else if(Mouse.getEventButton() == 2)
				mouseMiddle = Mouse.getEventButtonState();
		
		while (Keyboard.next())
		    keys[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
	}
	
	public void grab()
	{
		Mouse.setGrabbed(true);
	}
	
	public void ungrab()
	{
		Mouse.setGrabbed(false);
	}
}