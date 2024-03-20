#include "gl/glut.h"

void displaySquare() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    glColor3f(1.0f, 1.0f, 0.0f); // 0.0 ~ 1.0

    glBegin(GL_POLYGON); // GL_POLYGON, GL_TRIANGLES, GL_QUADS, GL_LINES
    glVertex2f(-0.5f, -0.5f); //도형에 대한 정점(꼭지점)
    glVertex2f(0.5f, -0.5f);
    glVertex2f(0.5f, 0.5f);
    glVertex2f(-0.5f, 0.5f);
    glEnd();

    glFinish();
}

void displayTriangle() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    glColor3f(0.3f, 0.4f, 0.5f); // 0.0 ~ 1.0

    glBegin(GL_POLYGON); // GL_POLYGON, GL_TRIANGLES, GL_QUADS, GL_LINES
    glVertex2f(0.0f, 0.5f);
    glVertex2f(-0.5f, -0.5f);
    glVertex2f(0.5f,-0.5f);
    glEnd();

    glFinish();
}

int main(int argc, char **argv)
{
    glutInit(&argc, argv);
    glutCreateWindow("OpenGL");
    glutDisplayFunc(displayTriangle);
    glutMainLoop();

    return 0;
}