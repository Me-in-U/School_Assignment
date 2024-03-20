#include "gl/glut.h"
#include <cstdio>

struct Vec2 {
    float x, y;
};

Vec2 linePt[4] = {
    {-0.3f, 0.2f},
    {0.6f, -0.7f},
    {-0.7f, -0.5f},
    {0.5f, 0.0f}
};

void display() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
    
    glColor3f(1.0, 0.0, 0.0);
    glBegin(GL_LINES);
    glVertex2f(linePt[0].x, linePt[0].y);
    glVertex2f(linePt[1].x, linePt[1].y);
    glEnd();

    glColor3f(0.0, 0.0, 1.0);
    glBegin(GL_LINES);
    glVertex2f(linePt[2].x, linePt[2].y);
    glVertex2f(linePt[3].x, linePt[3].y);
    glEnd();

    /* Calculate the interseciton point! */
    glColor3f(0.0, 1.0, 0.0);
    glPointSize(10.0);
    glBegin(GL_POINTS);
    glVertex2f(0, 0); /* Need to change the value. */
    glEnd();

    glutSwapBuffers();
}

void keyboard(unsigned char key, int x, int y) { 

    switch (key) {
	case 'w':
	    linePt[0].y += 0.1f;
	    linePt[1].y += 0.1f;
	    break;
	case 's':
	    linePt[0].y -= 0.1f;
	    linePt[1].y -= 0.1f;
	    break;
	case 'a':
	    linePt[0].x -= 0.1f;
	    linePt[1].x -= 0.1f;
	    break;
	case 'd':
	    linePt[0].x += 0.1f;
	    linePt[1].x += 0.1f;
	    break;
	case 27: // ESC
	    exit(0);
	    break;
    }
    glutPostRedisplay();
}

int main(int argc, char **argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB);
    glutInitWindowSize(500, 500);
    glutInitWindowPosition(1480, 100);


    glutCreateWindow("OpenGL");
    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);
    glutMainLoop();

    return 0;
}