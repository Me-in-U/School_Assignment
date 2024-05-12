package 문제해결기법.Assignment_25;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class mice {
    private static int cornerCount;
    private static int affordability;
    private static int holeCount;
    private static int mouseCount;
    private static Point[] corners;
    private static Point[] holes;
    private static Point[] mice;
    private static int[][] capacity;
    private static int[][] flow;
    private static ArrayList<ArrayList<Integer>> graph;

    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private static int CCW(Point p1, Point p2, Point p3) {
            long ccw = ((long) p1.x * p2.y + (long) p2.x * p3.y + (long) p3.x * p1.y)
                    - ((long) p1.y * p2.x + (long) p2.y * p3.x + (long) p3.y * p1.x);
            if (ccw == 0)
                return 0;
            return (ccw > 0) ? 1 : -1;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_25\\mice.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_25\\2.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            cornerCount = Integer.parseInt(st.nextToken());
            affordability = Integer.parseInt(st.nextToken());
            holeCount = Integer.parseInt(st.nextToken());
            mouseCount = Integer.parseInt(st.nextToken());
            corners = new Point[cornerCount + 1];
            holes = new Point[holeCount];
            mice = new Point[mouseCount];
            capacity = new int[holeCount + mouseCount + 2][holeCount + mouseCount + 2];
            flow = new int[holeCount + mouseCount + 2][holeCount + mouseCount + 2];
            graph = new ArrayList<>();
            for (int i = 0; i < cornerCount; i++) {
                st = new StringTokenizer(br.readLine());
                corners[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }
            corners[cornerCount] = corners[0];
            for (int i = 0; i < holeCount; i++) {
                st = new StringTokenizer(br.readLine());
                holes[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }
            for (int i = 0; i < mouseCount; i++) {
                st = new StringTokenizer(br.readLine());
                mice[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }
            for (int i = 0; i < mouseCount + holeCount + 2; i++)
                graph.add(new ArrayList<>());
            for (int i = 0; i < mouseCount; i++) {
                graph.get(mouseCount + holeCount).add(i);
                graph.get(i).add(mouseCount + holeCount);
                capacity[mouseCount + holeCount][i] = 1;
            }
            for (int i = 0; i < holeCount; i++) {
                graph.get(i + mouseCount).add(mouseCount + holeCount + 1);
                graph.get(mouseCount + holeCount + 1).add(i + mouseCount);
                capacity[i + mouseCount][mouseCount + holeCount + 1] = affordability;
            }
            for (int i = 0; i < mouseCount; i++) {
                for (int j = 0; j < holeCount; j++) {
                    if (!isCrossed(mice[i], holes[j])) {
                        graph.get(i).add(j + mouseCount);
                        graph.get(j + mouseCount).add(i);
                        capacity[i][j + mouseCount] = 1;
                    }
                }
            }
            sb.append(maxFlow() == mouseCount ? "Possible" : "Impossible").append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    private static boolean isCrossed(Point M, Point H) {
        int count = 0;
        for (int i = 0; i < cornerCount; i++) {
            Point p1 = corners[i];
            Point p2 = corners[i + 1];
            if (H.x == p1.x && H.y == p1.y)
                continue;
            int S1 = Point.CCW(M, H, p1);
            int S2 = Point.CCW(M, H, p2);
            int S3 = Point.CCW(p1, p2, M);
            int S4 = Point.CCW(p1, p2, H);
            int S12 = S1 * S2;
            int S34 = S3 * S4;
            if ((S12 <= 0 && S34 < 0 || S12 < 0 && S34 <= 0) ||
                    (S12 == 0 && S34 == 0 && isOneLine(p1, p2, M, H)))
                count++;
            if (count > 1)
                return true;
        }
        return false;
    }

    private static boolean isOneLine(Point p1, Point p2, Point p3, Point p4) {
        int A;
        int B;
        int C;
        int D;
        if (p1.x == p2.x) {
            A = Math.min(p1.y, p2.y);
            B = Math.max(p1.y, p2.y);
            C = Math.min(p3.y, p4.y);
            D = Math.max(p3.y, p4.y);
        } else {
            A = Math.min(p1.x, p2.x);
            B = Math.max(p1.x, p2.x);
            C = Math.min(p3.x, p4.x);
            D = Math.max(p3.x, p4.x);
        }
        return A <= D && C <= B;
    }

    private static int maxFlow() {
        int maxFlow = 0;
        while (true) {
            int minFlow = 15000;
            int[] prev = new int[mouseCount + holeCount + 2];
            Arrays.fill(prev, -1);
            prev[mouseCount + holeCount] = -2;
            Queue<Integer> q = new LinkedList<>();
            q.add(mouseCount + holeCount);
            while (!q.isEmpty()) {
                int current = q.remove();
                for (int next : graph.get(current)) {
                    if (capacity[current][next] - flow[current][next] > 0 && prev[next] == -1) {
                        q.add(next);
                        prev[next] = current;
                    }
                }
            }
            if (prev[mouseCount + holeCount + 1] == -1)
                break;
            for (int i = mouseCount + holeCount + 1; i != mouseCount + holeCount; i = prev[i])
                minFlow = Math.min(minFlow, capacity[prev[i]][i] - flow[prev[i]][i]);
            for (int i = mouseCount + holeCount + 1; i != mouseCount + holeCount; i = prev[i]) {
                flow[prev[i]][i] += minFlow;
                flow[i][prev[i]] -= minFlow;
            }
            maxFlow += minFlow;
        }
        return maxFlow;
    }
}