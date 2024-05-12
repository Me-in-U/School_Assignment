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
    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        static int CCW(Point p1, Point p2, Point p3) {
            long area = ((long) p1.x * p2.y + (long) p2.x * p3.y + (long) p3.x * p1.y)
                    - ((long) p1.y * p2.x + (long) p2.y * p3.x + (long) p3.y * p1.x);
            if (area == 0)
                return 0;
            return (area > 0) ? 1 : -1;
        }

        static boolean isCollinear(Point a, Point b, Point c, Point d) {
            int minA, maxA, minC, maxC;
            if (a.x == b.x) {
                minA = Math.min(a.y, b.y);
                maxA = Math.max(a.y, b.y);
                minC = Math.min(c.y, d.y);
                maxC = Math.max(c.y, d.y);
            } else {
                minA = Math.min(a.x, b.x);
                maxA = Math.max(a.x, b.x);
                minC = Math.min(c.x, d.x);
                maxC = Math.max(c.x, d.x);
            }
            return minA <= maxC && minC <= maxA;
        }

        static boolean isCrossed(Point mouse, Point hole, Point[] corners) {
            int intersectionCount = 0;
            for (int i = 0; i < corners.length - 1; i++) {
                Point start = corners[i];
                Point end = corners[i + 1];
                if (hole.x == start.x && hole.y == start.y)
                    continue;
                int cross1 = Point.CCW(mouse, hole, start);
                int cross2 = Point.CCW(mouse, hole, end);
                int cross3 = Point.CCW(start, end, mouse);
                int cross4 = Point.CCW(start, end, hole);
                int c12 = cross1 * cross2;
                int c34 = cross3 * cross4;

                boolean crosses = c12 <= 0 && c34 < 0 || c12 < 0 && c34 <= 0;
                boolean collinearAndOverlapping = c12 == 0 && c34 == 0 && Point.isCollinear(start, end, mouse, hole);
                if (crosses || collinearAndOverlapping) {
                    intersectionCount++;
                    if (intersectionCount > 1) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class Graph {
        private int source;
        private int sink;

        private int[][] capacity;
        private int[][] flow;

        private ArrayList<ArrayList<Integer>> adjacencyList;

        Graph(int source, int sink, int nodeCount) {
            this.source = source;
            this.sink = sink;
            capacity = new int[nodeCount][nodeCount];
            flow = new int[nodeCount][nodeCount];
            adjacencyList = new ArrayList<>();
            for (int i = 0; i < nodeCount; i++)
                adjacencyList.add(new ArrayList<>());
        }

        void addEdge(int from, int to, int cap) {
            adjacencyList.get(from).add(to);
            adjacencyList.get(to).add(from);
            capacity[from][to] = cap;
        }

        int maxFlow() {
            int totalFlow = 0;
            while (true) {
                int[] parent = new int[capacity.length];
                Arrays.fill(parent, -1);
                parent[source] = -2;
                Queue<Integer> queue = new LinkedList<>();
                queue.add(source);
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    for (int next : adjacencyList.get(current)) {
                        if (capacity[current][next] - flow[current][next] > 0 && parent[next] == -1) {
                            queue.add(next);
                            parent[next] = current;
                            if (next == sink)
                                break;
                        }
                    }
                    if (parent[sink] != -1)
                        break;
                }
                if (parent[sink] == -1)
                    break;
                int pathFlow = Integer.MAX_VALUE;
                for (int v = sink; v != source; v = parent[v])
                    pathFlow = Math.min(pathFlow, capacity[parent[v]][v] - flow[parent[v]][v]);
                for (int v = sink; v != source; v = parent[v]) {
                    flow[parent[v]][v] += pathFlow;
                    flow[v][parent[v]] -= pathFlow;
                }
                totalFlow += pathFlow;
            }
            return totalFlow;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("mice.out"));
        BufferedReader br = new BufferedReader(new FileReader("mice.inp"));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int cornerCount = Integer.parseInt(st.nextToken());
            int affordability = Integer.parseInt(st.nextToken());
            int holeCount = Integer.parseInt(st.nextToken());
            int mouseCount = Integer.parseInt(st.nextToken());

            Point[] corners = new Point[cornerCount + 1];
            Point[] holes = new Point[holeCount];
            Point[] mice = new Point[mouseCount];
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

            int source = mouseCount + holeCount;
            int sink = source + 1;
            int nodeCount = source + 2;

            Graph graph = new Graph(source, sink, nodeCount);

            for (int i = 0; i < mouseCount; i++)
                graph.addEdge(source, i, 1);
            for (int i = 0; i < holeCount; i++)
                graph.addEdge(i + mouseCount, sink, affordability);
            for (int i = 0; i < mouseCount; i++)
                for (int j = 0; j < holeCount; j++)
                    if (!Point.isCrossed(mice[i], holes[j], corners))
                        graph.addEdge(i, j + mouseCount, 1);

            sb.append(graph.maxFlow() == mouseCount ? "Possible" : "Impossible").append('\n');
        }
        bw.write(sb.toString());
        br.close();
        bw.close();
    }
}
