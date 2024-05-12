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
    private static Point[] mousePositions;
    private static int[][] capacity;
    private static int[][] flow;
    private static ArrayList<ArrayList<Integer>> adj;

    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_25\\mice.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_25\\1.inp"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        cornerCount = Integer.parseInt(st.nextToken());
        affordability = Integer.parseInt(st.nextToken());
        holeCount = Integer.parseInt(st.nextToken());
        mouseCount = Integer.parseInt(st.nextToken());
        corners = new Point[cornerCount + 1];
        holes = new Point[holeCount];
        mousePositions = new Point[mouseCount];
        capacity = new int[holeCount + mouseCount + 2][holeCount + mouseCount + 2];
        flow = new int[holeCount + mouseCount + 2][holeCount + mouseCount + 2];
        for (int i = 0; i < cornerCount; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            corners[i] = new Point(x, y);
        }
        corners[cornerCount] = corners[0];
        for (int i = 0; i < holeCount; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            holes[i] = new Point(x, y);
        }
        for (int i = 0; i < mouseCount; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            mousePositions[i] = new Point(x, y);
        }
        adj = new ArrayList<>();
        for (int i = 0; i < mouseCount + holeCount + 2; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < mouseCount; i++) {
            adj.get(mouseCount + holeCount).add(i);
            adj.get(i).add(mouseCount + holeCount);
            capacity[mouseCount + holeCount][i] = 1;
        }
        for (int i = 0; i < holeCount; i++) {
            adj.get(i + mouseCount).add(mouseCount + holeCount + 1);
            adj.get(mouseCount + holeCount + 1).add(i + mouseCount);
            capacity[i + mouseCount][mouseCount + holeCount + 1] = affordability;
        }
        addEdges();
        int maxFlow = maxFlow();
        bw.write(maxFlow == mouseCount ? "Possible" : "Impossible");
        bw.close();
        br.close();
    }

    private static void addEdges() {
        for (int i = 0; i < mouseCount; i++) {
            for (int j = 0; j < holeCount; j++) {
                if (!isCrossed(mousePositions[i], holes[j])) {
                    adj.get(i).add(j + mouseCount);
                    adj.get(j + mouseCount).add(i);
                    capacity[i][j + mouseCount] = 1;
                }
            }
        }
    }

    private static boolean isCrossed(Point M, Point H) {
        int count = 0;
        for (int i = 0; i < cornerCount; i++) {
            Point p1 = corners[i];
            Point p2 = corners[i + 1];
            if (H.x == p1.x && H.y == p1.y)
                continue;
            int S1 = CCW(M, H, p1);
            int S2 = CCW(M, H, p2);
            int S3 = CCW(p1, p2, M);
            int S4 = CCW(p1, p2, H);
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

    private static int CCW(Point p1, Point p2, Point p3) {
        long ccw = (long) p1.x * p2.y + (long) p2.x * p3.y + (long) p3.x * p1.y -
                (long) p1.y * p2.x + (long) p2.y * p3.x + (long) p3.y * p1.x;
        if (ccw == 0)
            return 0;
        if (ccw > 0)
            return 1;
        return -1;
    }

    private static int maxFlow() {
        int result = 0;
        while (true) {
            int minFlow = 15000;
            int[] prev = new int[mouseCount + holeCount + 2];
            Queue<Integer> q = new LinkedList<>();
            Arrays.fill(prev, -1);
            q.add(mouseCount + holeCount);
            prev[mouseCount + holeCount] = -2;
            while (!q.isEmpty()) {
                int cur = q.remove();
                for (int next : adj.get(cur)) {
                    if (capacity[cur][next] - flow[cur][next] > 0 && prev[next] == -1) {
                        q.add(next);
                        prev[next] = cur;
                    }
                }
            }
            if (prev[mouseCount + holeCount + 1] == -1)
                break;
            for (int i = mouseCount + holeCount + 1; i != mouseCount + holeCount; i = prev[i]) {
                minFlow = Math.min(minFlow, capacity[prev[i]][i] - flow[prev[i]][i]);
            }
            for (int i = mouseCount + holeCount + 1; i != mouseCount + holeCount; i = prev[i]) {
                flow[prev[i]][i] += minFlow;
                flow[i][prev[i]] -= minFlow;
            }
            result += minFlow;
        }
        return result;
    }
}