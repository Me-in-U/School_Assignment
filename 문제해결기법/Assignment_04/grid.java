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
        int dotsCount = 0;

        Point() {
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point(int x, int y, int dotsCount) {
            this.x = x;
            this.y = y;
            this.dotsCount = dotsCount;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("trip.out"));
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
            for (int j = 0; j < a; j++) {
                int dotX = Integer.parseInt(st.nextToken());
                int dotY = Integer.parseInt(st.nextToken());
                dots[dotX][dotY] = true;
            }
            boolean[][] unables = new boolean[x + 1][y + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < b; j++) {
                int unableX = Integer.parseInt(st.nextToken());
                int unableY = Integer.parseInt(st.nextToken());
                unables[unableX][unableY] = true;
            }

            LinkedList<Point> points = new LinkedList<>();
            points.add(new Point(0, 0));
            while (!points.isEmpty()) {
                Point p = points.poll();
                if (p.x == x && p.y == y) {
                    continue;
                }
                // !routeCount 업데이트
                p.routeCount = routeCount[p.x][p.y][p.dotsCount];
                // System.out.println("현재점 : (" + p.x + ", " + p.y + ")");
                // !아래로 이동
                int downNextX = p.x + 1;
                int downNextY = p.y;
                if (downNextX <= x) {
                    if (unables[downNextX][downNextY]) {

                    } else if (dots[downNextX][downNextY]) {
                        // System.out.println("아래쪽 분기 추가");
                        if (routeCount[downNextX][downNextY][p.dotsCount + 1] == 0) {
                            Point newPoint = new Point(downNextX, downNextY, p.dotsCount + 1);
                            points.add(newPoint);
                        }
                        routeCount[downNextX][downNextY][p.dotsCount + 1] += p.routeCount;
                    } else {
                        if (routeCount[downNextX][downNextY][p.dotsCount] == 0) {
                            Point newPoint = new Point(downNextX, downNextY, p.dotsCount);
                            points.add(newPoint);
                        }
                        // System.out.println("아래쪽 점 추가");
                        routeCount[downNextX][downNextY][p.dotsCount] += p.routeCount;
                    }
                }
                // !오른쪽으로 이동
                int rightNextX = p.x;
                int rightNextY = p.y + 1;
                if (rightNextY <= y) {
                    if (unables[rightNextX][rightNextY]) {

                    } else if (dots[rightNextX][rightNextY]) {
                        // System.out.println("오른쪽 분기 추가");
                        if (routeCount[rightNextX][rightNextY][p.dotsCount + 1] == 0) {
                            Point newPoint = new Point(rightNextX, rightNextY, p.dotsCount + 1);
                            points.add(newPoint);
                        }
                        routeCount[rightNextX][rightNextY][p.dotsCount + 1] += p.routeCount;
                        // System.out.println("(" + rightNextX + "," + rightNextY + ") 점:" +
                        // (p.dotsCount + 1) + "일때, 라우트:"
                        // + routeCount[rightNextX][rightNextY][p.dotsCount + 1] + "개");
                    } else {
                        // System.out.println("오른쪽 점 추가");
                        if (routeCount[rightNextX][rightNextY][p.dotsCount] == 0) {
                            Point newPoint = new Point(rightNextX, rightNextY, p.dotsCount);
                            points.add(newPoint);
                        }
                        routeCount[rightNextX][rightNextY][p.dotsCount] += p.routeCount;
                        // System.out.println("(" + rightNextX + "," + rightNextY + ") 점:" +
                        // (p.dotsCount) + "일때, 라우트:"
                        // + routeCount[rightNextX][rightNextY][p.dotsCount] + "개");
                    }
                }
                // System.out.println();
            }
            int result = 0;
            for (int j = k; j <= 10; j++) {
                result += routeCount[x][y][j];
            }
            System.out.println(result);
        }

        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
