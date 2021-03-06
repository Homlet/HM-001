package uk.co.homletmoo.hm001;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {

	public boolean[] keys = new boolean[Keyboard.KEYBOARD_SIZE];
	public boolean[] keysOld = new boolean[Keyboard.KEYBOARD_SIZE];
	public float mouseX, mouseY, mouseDX, mouseDY;
	public boolean mouseLeft, mouseRight, mouseMiddle, grabbed;
	
	public Input()
	{
		grab();
	}
	
	public void pollInput()
	{
		grabbed = Mouse.isGrabbed();
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		
		mouseDX /= 1.5;
		mouseDY /= 1.5;
		mouseDX += (mouseX - Attr.DISPLAY_HALFWIDTH) / 2;
		mouseDY += (mouseY - Attr.DISPLAY_HALFHEIGHT) / 2;
		
		while(Mouse.next())
			if(Mouse.getEventButton() == 0)
				mouseLeft = Mouse.getEventButtonState();
			else if(Mouse.getEventButton() == 1)
				mouseRight = Mouse.getEventButtonState();
			else if(Mouse.getEventButton() == 2)
				mouseMiddle = Mouse.getEventButtonState();
		
		for(int i = 0; i < keys.length; i++)
			keysOld[i] = keys[i];
		
		while (Keyboard.next())
		    keys[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
	}
	
	public boolean pressed(int key)
	{
		return keys[key] && hasChanged(key);
	}
	
	public boolean hasChanged(int key)
	{
		return keys[key] != keysOld[key];
	}
	
	public void grab()
	{
		Mouse.setGrabbed(true);
	}
	
	public void ungrab()
	{
		Mouse.setGrabbed(false);
	}
	
	public void setPos(int x, int y)
	{
		Mouse.setCursorPosition(x, y);
	}
}