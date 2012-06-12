package uk.co.homletmoo.hm001;

import static org.lwjgl.opengl.GL11.*;

public class Prim {
	
	public static int listCube;
	
	public static void initLists()
	{
		listCube = glGenLists(1);
		glNewList(listCube, GL_COMPILE);
			glBegin(GL_QUADS);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			    glTexCoord2f(0, 0);			glVertex3f(1, 1, 0);
			    glTexCoord2f(1, 0);			glVertex3f(0, 1, 0);
			    glTexCoord2f(1, 1);			glVertex3f(0, 1, 1);
			    glTexCoord2f(0, 1);			glVertex3f(1, 1, 1);

			    glTexCoord2f(0, 0);			glVertex3f(1, 0, 0);
			    glTexCoord2f(1, 0);			glVertex3f(0, 0, 0);
			    glTexCoord2f(1, 1);			glVertex3f(0, 0, 1);
				glTexCoord2f(0, 1);			glVertex3f(1, 0, 1);

				glTexCoord2f(0, 0);			glVertex3f(1, 1, 1);
				glTexCoord2f(1, 0);			glVertex3f(0, 1, 1);
				glTexCoord2f(1, 1);			glVertex3f(0, 0, 1);
				glTexCoord2f(0, 1);			glVertex3f(1, 0, 1);

				glTexCoord2f(1, 1);			glVertex3f(1, 0, 0);
				glTexCoord2f(1, 0);			glVertex3f(0, 0, 0);
				glTexCoord2f(0, 0);			glVertex3f(0, 1, 0);
				glTexCoord2f(0, 1);			glVertex3f(1, 1, 0);

				glTexCoord2f(0, 0);			glVertex3f(0, 1, 1);
				glTexCoord2f(1, 0);			glVertex3f(0, 1, 0);
				glTexCoord2f(1, 1);			glVertex3f(0, 0, 0);
				glTexCoord2f(0, 1);			glVertex3f(0, 0, 1);

				glTexCoord2f(0, 0);			glVertex3f(1, 1, 1);
				glTexCoord2f(1, 0);			glVertex3f(1, 1, 0);
				glTexCoord2f(1, 1);			glVertex3f(1, 0, 0);
				glTexCoord2f(0, 1);			glVertex3f(1, 0, 1);
			glEnd();
		glEndList();
	}
}
