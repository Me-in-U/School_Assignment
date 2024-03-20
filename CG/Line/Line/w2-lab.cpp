#include "gl/glut.h"
#include <cstdio>
#include <algorithm>
#include <cmath>

struct Vec2 {
    float x, y;
};

Vec2 linePt[6] = {
    {-0.3f, 0.2f},
    {0.6f, -0.7f},
    {-0.7f, -0.5f},
    {0.5f, 0.0f}
};

const float EPSILON = 1e-5; // 부동소수점 비교를 위한 작은 값

// 두 값이 거의 같은지 비교하는 함수
bool almostEqual(float a, float b) {
    return std::abs(a - b) < EPSILON;
}

bool isBetween(float value, float end1, float end2) {
    // value가 end1과 end2 사이에 있는지 확인 (end1이 end2보다 클 수도 있으므로)
    return (std::min(end1, end2) <= value && value <= std::max(end1, end2));
}

// 교차점 계산 함수
Vec2 calculateIntersection(Vec2 a1, Vec2 a2, Vec2 b1, Vec2 b2) {
    float denominator = (a1.x - a2.x) * (b1.y - b2.y) - (a1.y - a2.y) * (b1.x - b2.x);

    // 평행 또는 일치하는 경우 -3 반환
    if (almostEqual(denominator, 0.0)) {
        // 동일한 좌표로 구성된 선분인지 추가 검사
        if ((almostEqual(a1.x, b1.x) && almostEqual(a1.y, b1.y) &&
            almostEqual(a2.x, b2.x) && almostEqual(a2.y, b2.y)) ||
            (almostEqual(a1.x, b2.x) && almostEqual(a1.y, b2.y) &&
                almostEqual(a2.x, b1.x) && almostEqual(a2.y, b1.y))) {
            return Vec2{ -2, -2 }; // 선분이 일치
        }
        return Vec2{ -3, -3 }; // 선분이 평행
    }

    float intersectionX = ((a1.x * a2.y - a1.y * a2.x) * (b1.x - b2.x) - (a1.x - a2.x) * (b1.x * b2.y - b1.y * b2.x)) / denominator;
    float intersectionY = ((a1.x * a2.y - a1.y * a2.x) * (b1.y - b2.y) - (a1.y - a2.y) * (b1.x * b2.y - b1.y * b2.x)) / denominator;

    // 교차점이 선분 범위 내에 없는 경우 -1 반환
    if (!(isBetween(intersectionX, a1.x, a2.x) && isBetween(intersectionY, a1.y, a2.y) &&
        isBetween(intersectionX, b1.x, b2.x) && isBetween(intersectionY, b1.y, b2.y))) {
        return Vec2{ -1, -1 }; // 선분이 교차하지 않음
    }

    return Vec2{ intersectionX, intersectionY }; // 유효한 교차점
}
// 텍스트를 화면에 렌더링하는 함수
void renderBitmapString(float x, float y, void* font, const char* string) {
    const char* c;
    glRasterPos2f(x, y);
    for (c = string; *c != '\0'; c++) {
        glutBitmapCharacter(font, *c);
    }
}

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
    Vec2 intersection = calculateIntersection(linePt[0], linePt[1], linePt[2], linePt[3]);
    // 유효한 교차점만 표시
    if (intersection.x != -1 && intersection.y != -1) {
        glColor3f(0.0, 1.0, 0.0); // 녹색
        glPointSize(10.0);
        glBegin(GL_POINTS);
        glVertex2f(intersection.x, intersection.y);
        glEnd();
    }
    else {
        // 윈도우 크기의 중앙 값을 얻습니다.
        int windowWidth = glutGet(GLUT_WINDOW_WIDTH);
        int windowHeight = glutGet(GLUT_WINDOW_HEIGHT);

        // 오류 메시지를 설정하고 중앙에 렌더링합니다.
        glColor3f(1.0, 0.0, 0.0); // 빨간색으로 설정
        if (intersection.x == -2 && intersection.y == -2) {
            renderBitmapString(0.0f, 0.0f, GLUT_BITMAP_TIMES_ROMAN_24, "Lines Coincide");
        }
        else if (intersection.x == -3 && intersection.y == -3) {
            renderBitmapString(0.0f, 0.0f, GLUT_BITMAP_TIMES_ROMAN_24, "Parallel Lines");
        }
        else {
            renderBitmapString(0.0f, 0.0f, GLUT_BITMAP_TIMES_ROMAN_24, "No Intersection");
        }
    }
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