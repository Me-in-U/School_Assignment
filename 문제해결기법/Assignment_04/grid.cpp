#include <fstream>
#include <sstream>
#include <vector>
#include <queue>

using namespace std;

class Point
{
public:
    int x, y, routeCount, dotsCount;

    Point(int x, int y, int dotsCount) : x(x), y(y), routeCount(0), dotsCount(dotsCount) {}
};

int main()
{
    const int moveX[2] = {1, 0};
    const int moveY[2] = {0, 1};

    ifstream fin("grid.inp");
    ofstream fout("grid.out");

    int T;
    fin >> T;
    while (T--)
    {
        int x, y, a, b, k;
        fin >> x >> y >> a >> b >> k;

        vector<vector<vector<int>>> routeCount(x + 1, vector<vector<int>>(y + 1, vector<int>(11, 0)));
        routeCount[0][0][0] = 1;

        vector<vector<bool>> dots(x + 1, vector<bool>(y + 1, false));
        for (int i = 0; i < a; i++)
        {
            int dx, dy;
            fin >> dx >> dy;
            dots[dx][dy] = true;
        }

        vector<vector<bool>> unables(x + 1, vector<bool>(y + 1, false));
        for (int i = 0; i < b; i++)
        {
            int ux, uy;
            fin >> ux >> uy;
            unables[ux][uy] = true;
        }

        queue<Point> points;
        points.push(Point(0, 0, 0));
        while (!points.empty())
        {
            Point p = points.front();
            points.pop();

            if (p.x == x && p.y == y)
            {
                continue;
            }

            p.routeCount = routeCount[p.x][p.y][p.dotsCount];
            for (int i = 0; i < 2; i++)
            {
                int nextX = p.x + moveX[i];
                int nextY = p.y + moveY[i];
                if (nextX <= x && nextY <= y && !unables[nextX][nextY])
                {
                    int dotsCount = dots[nextX][nextY] ? min(p.dotsCount + 1, 10) : p.dotsCount;
                    if (routeCount[nextX][nextY][dotsCount] == 0)
                    {
                        points.push(Point(nextX, nextY, dotsCount));
                    }
                    routeCount[nextX][nextY][dotsCount] = (routeCount[nextX][nextY][dotsCount] + p.routeCount) % 1000000007;
                }
            }
        }

        int result = 0;
        for (int i = k; i <= 10; i++)
        {
            result = (result + routeCount[x][y][i]) % 1000000007;
        }
        fout << result << endl;
    }

    fout.close();
    fin.close();

    return 0;
}
