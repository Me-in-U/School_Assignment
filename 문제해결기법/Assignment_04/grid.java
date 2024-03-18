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
        int routeCount = 1;
        int passedDotsCount = 0;

        Point() {
        }

        Point(int x, int y, int routeCount) {
            this.routeCount = routeCount;
        }

        void dotPlus() {
            this.passedDotsCount++;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("trip.out"));
        BufferedReader br = new BufferedReader(new FileReader("trip.inp"));
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

            Point[][][] passedCount = new Point[x + 1][y + 1][11];

            boolean[][] dots = new boolean[x + 1][y + 1];
            for (int j = 0; j < a; j++) {
                st = new StringTokenizer(br.readLine());
                dots[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;
            }
            boolean[][] unables = new boolean[x + 1][y + 1];
            for (int j = 0; j < b; j++) {
                st = new StringTokenizer(br.readLine());
                unables[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;
            }

            LinkedList<Point> points = new LinkedList<>();
            points.add(new Point(0, 0, 1));
            while (!points.isEmpty()) {
                Point p = points.poll();
                int downNextX = p.x + 1;
                int downNextY = p.y;
                if (downNextX <= x) {
                    if(unables[downNextX][downNextY]){
                        continue;
                    }else if(dots[downNextX][downNextY]){
                        passedCount
                    }else{

                    }
                }
                int rightNextX = p.x;
                int rightNexty = p.y + 1;
                if (rightNexty <= y) {

                }
            }
        }

        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
