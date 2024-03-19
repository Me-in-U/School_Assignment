#include "gl/glut.h"
#include <cstdio>
#include <vector>

struct Vec2 {
    float x, y;
};

Vec2 mousePt = { 0, 0 };
std::vector<Vec2> vecs;

void display() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    glColor3f(0.0f, 0.0f, 0.0f);
    glPointSize(10.0);

    glBegin(GL_POINTS);
    //glVertex2f(mousePt.x, mousePt.y);
    for (const auto& pt : vecs) {
        glVertex2f(pt.x, pt.y);
    }
    glEnd();

    glutSwapBuffers();
}

void keyboard(unsigned char key, int x, int y) {

    switch (key) {
    case 27: // ESC
        exit(0);
        break;
    }
    glutPostRedisplay();
}

void mouseCoordinateTranslate(int winX, int winY)
{
    mousePt.x = winX / 250.0 - 1;
    mousePt.y = (winY / 250.0 - 1) * (-1.0);
    vecs.push_back(mousePt);
}

void mouse(int button, int state, int x, int y)
{
    printf("mouse: %d %d %d %d\n", button, state, x, y);
    //if (state == GLUT_DOWN) {
    //    mouseCoordinateTranslate(x, y);
    //}
    if (state == GLUT_DOWN) {
        mouseCoordinateTranslate(x, y);
    }
    glutPostRedisplay();
}

void mouseMotion(int x, int y)
{
    printf("mouse motion: %d %d\n", x, y);
    //mouseCoordinateTranslate(x, y);
    mouseCoordinateTranslate(x, y);
    glutPostRedisplay();
}

//void mousePassiveMotion(int x, int y)
//{
//    printf("mouse passive motion: %d %d\n", x, y);
    //mouseCoordinateTranslate(x, y);
  //  glutPostRedisplay();
//}

int main(int argc, char** argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
    glutInitWindowSize(500, 500);
    glutInitWindowPosition(1480, 100);


    glutCreateWindow("OpenGL");
    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);
    glutMouseFunc(mouse);
    glutMotionFunc(mouseMotion);
    //glutPassiveMotionFunc(mousePassiveMotion);
    glutMainLoop();

    return 0;
}