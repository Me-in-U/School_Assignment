package 문제해결기법.Assignment_25;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class mice2 {
    private static int cornerCount;
    private static int affordability;
    private static int holeCount;
    private static int mouseCount;
    private static Point[] corners;
    private static Point[] holes;
    private static Point[] mice;

    private static class Point {
        int x, y;

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
    }

    private static class Graph {
        private int nodeCount;
        private List<List<Integer>> adjacencyList;
        private int[][] capacity;
        private int[][] flow;

        public Graph(int nodeCount) {
            this.nodeCount = nodeCount;
            adjacencyList = new ArrayList<>();
            capacity = new int[nodeCount][nodeCount];
            flow = new int[nodeCount][nodeCount];
            for (int i = 0; i < nodeCount; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int from, int to, int cap) {
            adjacencyList.get(from).add(to);
            adjacencyList.get(to).add(from);
            capacity[from][to] = cap;
        }

        public int maxFlow(int source, int sink) {
            int totalFlow = 0;
            while (true) {
                int[] parent = new int[nodeCount];
                Arrays.fill(parent, -1);
                Queue<Integer> queue = new LinkedList<>();
                queue.add(source);
                parent[source] = source;

                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    for (int next : adjacencyList.get(current)) {
                        if (capacity[current][next] > flow[current][next] && parent[next] == -1) {
                            parent[next] = current;
                            queue.add(next);
                            if (next == sink)
                                break;
                        }
                    }
                }

                if (parent[sink] == -1)
                    break;

                int pathFlow = Integer.MAX_VALUE;
                for (int i = sink; i != source; i = parent[i]) {
                    pathFlow = Math.min(pathFlow, capacity[parent[i]][i] - flow[parent[i]][i]);
                }

                for (int i = sink; i != source; i = parent[i]) {
                    flow[parent[i]][i] += pathFlow;
                    flow[i][parent[i]] -= pathFlow;
                }

                totalFlow += pathFlow;
            }
            return totalFlow;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_25\\1.inp"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_25\\mice.out"));
        int T = Integer.parseInt(br.readLine());
        StringTokenizer st;

        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            cornerCount = Integer.parseInt(st.nextToken());
            affordability = Integer.parseInt(st.nextToken());
            holeCount = Integer.parseInt(st.nextToken());
            mouseCount = Integer.parseInt(st.nextToken());

            corners = new Point[cornerCount + 1];
            holes = new Point[holeCount];
            mice = new Point[mouseCount];

            for (int i = 0; i < cornerCount; i++) {
                st = new StringTokenizer(br.readLine());
                corners[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }
            corners[cornerCount] = corners[0]; // Close the polygon

            for (int i = 0; i < holeCount; i++) {
                st = new StringTokenizer(br.readLine());
                holes[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }

            for (int i = 0; i < mouseCount; i++) {
                st = new StringTokenizer(br.readLine());
                mice[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }

            Graph graph = new Graph(mouseCount + holeCount + 2);
            int source = mouseCount + holeCount;
            int sink = mouseCount + holeCount + 1;

            for (int i = 0; i < mouseCount; i++) {
                graph.addEdge(source, i, 1);
            }

            for (int i = 0; i < holeCount; i++) {
                graph.addEdge(i + mouseCount, sink, affordability);
            }

            for (int i = 0; i < mouseCount; i++) {
                for (int j = 0; i < holeCount; i++) {
                    if (!isCrossed(mice[i], holes[j])) {
                        graph.addEdge(i, j + mouseCount, 1);
                    }
                }
            }

            bw.write(graph.maxFlow(source, sink) == mouseCount ? "Possible\n" : "Impossible\n");
        }

        br.close();
        bw.close();
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
                    (S12 == 0 && S34 == 0 && Point.isOneLine(p1, p2, M, H)))
                count++;
            if (count > 1)
                return true;
        }
        return false;
    }

}
