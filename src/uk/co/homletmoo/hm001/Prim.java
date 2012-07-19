package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;

public class Prim {
	
	public static int cube;
	public static int point;
	//Quad faces (quad x-positive, quad x-negative etc)
	public static int qxp, qxn, qyp, qyn, qzp, qzn;
	
	public static void initLists()
	{
		cube = glGenLists(1);
		glNewList(cube, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(0, 1, 0);
				glTexCoord2f(0, 1);			glVertex3f(1, 1, -1);		// Top
				glTexCoord2f(1, 1);			glVertex3f(-1, 1, -1);
				glTexCoord2f(1, 0);			glVertex3f(-1, 1, 1);
				glTexCoord2f(0, 0);			glVertex3f(1, 1, 1);
				
				glNormal3f(0, -1, 0);
				glTexCoord2f(1, 1);			glVertex3f(-1, -1, 1);		// Bottom
				glTexCoord2f(1, 0);			glVertex3f(-1, -1, -1);
				glTexCoord2f(0, 0);			glVertex3f(1, -1, -1);
				glTexCoord2f(0, 1);			glVertex3f(1, -1, 1);
				
				glNormal3f(0, 0, 1);
				glTexCoord2f(1, 0);			glVertex3f(1, 1, 1);		// Back
				glTexCoord2f(0, 0);			glVertex3f(-1, 1, 1);
				glTexCoord2f(0, 1);			glVertex3f(-1, -1, 1);
				glTexCoord2f(1, 1);			glVertex3f(1, -1, 1);
				
				glNormal3f(0, 0, -1);
				glTexCoord2f(0, 1);			glVertex3f(1, -1, -1);		// Front
				glTexCoord2f(1, 1);			glVertex3f(-1, -1, -1);
				glTexCoord2f(1, 0);			glVertex3f(-1, 1, -1);
				glTexCoord2f(0, 0);			glVertex3f(1, 1, -1);
				
				glNormal3f(-1, 0, 0);			
				glTexCoord2f(1, 0);			glVertex3f(-1, 1, 1);		// Right
				glTexCoord2f(0, 0);			glVertex3f(-1, 1, -1);
				glTexCoord2f(0, 1);			glVertex3f(-1, -1, -1);
				glTexCoord2f(1, 1);			glVertex3f(-1, -1, 1);
				
				glNormal3f(1, 0, 0);			
				glTexCoord2f(1, 1);			glVertex3f(1, -1, -1);		// Left
				glTexCoord2f(1, 0);			glVertex3f(1, 1, -1);
				glTexCoord2f(0, 0);			glVertex3f(1, 1, 1);
				glTexCoord2f(0, 1);			glVertex3f(1, -1, 1);
			glEnd();
		glEndList();
		
		point = glGenLists(1);
		glNewList(point, GL_COMPILE);
			glBegin(GL_POINTS);
				glVertex3f(0, 0, 0);
			glEnd();
		glEndList();
		
	//	Begin defining faces --------------------------------------
	//
		qxp = glGenLists(1);
		glNewList(qxp, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(1, 0, 0);			
				glTexCoord2f(1, 1);			glVertex3f(1, -1, -1);		// Left
				glTexCoord2f(1, 0);			glVertex3f(1, 1, -1);
				glTexCoord2f(0, 0);			glVertex3f(1, 1, 1);
				glTexCoord2f(0, 1);			glVertex3f(1, -1, 1);
			glEnd();
		glEndList();
		
		qxn = glGenLists(1);
		glNewList(qxn, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(-1, 0, 0);			
				glTexCoord2f(1, 0);			glVertex3f(-1, 1, 1);		// Right
				glTexCoord2f(0, 0);			glVertex3f(-1, 1, -1);
				glTexCoord2f(0, 1);			glVertex3f(-1, -1, -1);
				glTexCoord2f(1, 1);			glVertex3f(-1, -1, 1);
			glEnd();
		glEndList();
		
		qyp = glGenLists(1);
		glNewList(qyp, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(0, 1, 0);
				glTexCoord2f(0, 1);			glVertex3f(1, 1, -1);		// Top
				glTexCoord2f(1, 1);			glVertex3f(-1, 1, -1);
				glTexCoord2f(1, 0);			glVertex3f(-1, 1, 1);
				glTexCoord2f(0, 0);			glVertex3f(1, 1, 1);
			glEnd();
		glEndList();
		
		qyn = glGenLists(1);
		glNewList(qyn, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(0, -1, 0);
				glTexCoord2f(1, 1);			glVertex3f(-1, -1, 1);		// Bottom
				glTexCoord2f(1, 0);			glVertex3f(-1, -1, -1);
				glTexCoord2f(0, 0);			glVertex3f(1, -1, -1);
				glTexCoord2f(0, 1);			glVertex3f(1, -1, 1);
			glEnd();
		glEndList();
		
		qzp = glGenLists(1);
		glNewList(qzp, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(0, 0, 1);
				glTexCoord2f(1, 0);			glVertex3f(1, 1, 1);		// Back
				glTexCoord2f(0, 0);			glVertex3f(-1, 1, 1);
				glTexCoord2f(0, 1);			glVertex3f(-1, -1, 1);
				glTexCoord2f(1, 1);			glVertex3f(1, -1, 1);
			glEnd();
		glEndList();
		
		qzn = glGenLists(1);
		glNewList(qzn, GL_COMPILE);
			glBegin(GL_QUADS);
				glNormal3f(0, 0, -1);
				glTexCoord2f(0, 1);			glVertex3f(1, -1, -1);		// Front
				glTexCoord2f(1, 1);			glVertex3f(-1, -1, -1);
				glTexCoord2f(1, 0);			glVertex3f(-1, 1, -1);
				glTexCoord2f(0, 0);			glVertex3f(1, 1, -1);
			glEnd();
		glEndList();
	}
}
