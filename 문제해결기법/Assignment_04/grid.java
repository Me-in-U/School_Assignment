package 문제해결기법.Assignment_04;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class grid {
    public static class Point {
        int x;
        int y;
        int routeCount;
        int dotsCount;

        Point(int x, int y, int dotsCount) {
            this.x = x;
            this.y = y;
            this.dotsCount = dotsCount;
        }
    }

    public static final int MOD = 1_000_000_007;
    protected static final int[] moveX = { 1, 0 };
    protected static final int[] moveY = { 0, 1 };

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("grid.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_04\\2.inp"));
        StringTokenizer st = null;
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            int[][][] routeCount = new int[x + 1][y + 1][11];
            routeCount[0][0][0] = 1;

            boolean[][] dots = new boolean[x + 1][y + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < a; j++)
                dots[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;

            boolean[][] unables = new boolean[x + 1][y + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < b; j++)
                unables[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;

            // LinkedList<Point> points = new LinkedList<>();
            // points.add(new Point(0, 0, 0));
            // while (!points.isEmpty()) {
            // Point p = points.poll();
            // if (p.x == x && p.y == y) {
            // continue;
            // }
            // p.routeCount = routeCount[p.x][p.y][p.dotsCount];
            // for (int j = 0; j < 2; j++) {
            // int nextX = p.x + moveX[j];
            // int nextY = p.y + moveY[j];
            // if (nextX <= x && nextY <= y) {
            // if (!unables[nextX][nextY]) {
            // int dotsCount;
            // if (dots[nextX][nextY]) {
            // dotsCount = (p.dotsCount == 10) ? 10 : (p.dotsCount + 1);
            // } else {
            // dotsCount = p.dotsCount;
            // }
            // if (routeCount[nextX][nextY][dotsCount] == 0) {
            // Point newPoint = new Point(nextX, nextY, dotsCount);
            // points.add(newPoint);
            // }
            // routeCount[nextX][nextY][dotsCount] = (routeCount[nextX][nextY][dotsCount]
            // + p.routeCount) % MOD;
            // }
            // }
            // }
            // }

            // !맨 윗 첫 줄
            for (int j = 1; j <= y; j++) {
                if (!unables[0][j]) {
                    boolean nextIsDot = dots[0][j];
                    if (nextIsDot) {
                        for (int dot = 1; dot <= 10; dot++) {
                            int nextDotCount = dot + 1;
                            if (nextDotCount > 10) {
                                nextDotCount = 10;
                            }
                            routeCount[0][j][nextDotCount] = routeCount[0][j - 1][dot - 1] % MOD;
                        }
                    } else {
                        for (int dot = 0; dot <= 10; dot++) {
                            routeCount[0][j][dot] = routeCount[0][j - 1][dot] % MOD;
                        }
                    }
                }
            }
            // !맨 왼쪽 첫 줄
            for (int j = 1; j <= x; j++) {
                if (!unables[j][0]) {
                    boolean nextIsDot = dots[j][0];
                    if (nextIsDot) {
                        for (int dot = 1; dot <= 10; dot++) {
                            int nextDotCount = dot + 1;
                            if (nextDotCount > 10) {
                                nextDotCount = 10;
                            }
                            routeCount[j][0][nextDotCount] = routeCount[j - 1][0][dot - 1] % MOD;
                        }
                    } else {
                        for (int dot = 0; dot <= 10; dot++) {
                            routeCount[j][0][dot] = routeCount[j - 1][0][dot] % MOD;
                        }
                    }
                }
            }
            for (int j = 1; j <= x; j++) {
                for (int j2 = 1; j2 <= y; j2++) {
                    if (!unables[j][j2]) {
                        if (dots[j][j2]) {
                            for (int dot = 0; dot <= 10; dot++) {
                                int nextDotCount = dot + 1;
                                if (nextDotCount > 10) {
                                    nextDotCount = 10;
                                }
                                routeCount[j][j2][nextDotCount] = (routeCount[j - 1][j2][dot]
                                        + routeCount[j][j2 - 1][dot]) % MOD;
                            }
                        } else {
                            for (int dot = 0; dot <= 10; dot++) {
                                routeCount[j][j2][dot] = (routeCount[j - 1][j2][dot] + routeCount[j][j2 - 1][dot])
                                        % MOD;
                            }
                        }
                    }

                }
            }

            int result = 0;
            for (int j = k; j <= 10; j++)
                result = (result + routeCount[x][y][j]) % MOD;
            System.out.println(result);
            sb.append(result).append('\n');
        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
