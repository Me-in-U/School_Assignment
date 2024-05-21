package 문제해결기법.Assignment_26;

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

public class service {
    static class Edge {
        int to;
        int capacity;
        Edge reverse;

        public Edge(int to, int capacity) {
            this.to = to;
            this.capacity = capacity;
        }
    }

    static class Graph {
        private List<Edge>[] graph;

        public Graph(int size) {
            graph = new List[size];
            for (int i = 0; i < size; i++) {
                graph[i] = new ArrayList<>();
            }
        }

        public void addEdge(int from, int to, int capacity) {
            Edge forward = new Edge(to, capacity);
            Edge backward = new Edge(from, 0);
            forward.reverse = backward;
            backward.reverse = forward;
            graph[from].add(forward);
            graph[to].add(backward);
        }

        public int maxFlow(int source, int sink) {
            int flow = 0;
            int[] level = new int[graph.length];

            while (true) {
                if (!bfs(level, source, sink))
                    break;
                int[] ptr = new int[graph.length];
                while (true) {
                    int pushed = dfs(ptr, level, source, sink, Integer.MAX_VALUE);
                    if (pushed == 0)
                        break;
                    flow += pushed;
                }
            }
            return flow;
        }

        private boolean bfs(int[] level, int source, int sink) {
            Arrays.fill(level, -1);
            level[source] = 0;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(source);

            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (Edge edge : graph[u]) {
                    if (level[edge.to] == -1 && edge.capacity > 0) {
                        level[edge.to] = level[u] + 1;
                        queue.offer(edge.to);
                        if (edge.to == sink)
                            return true;
                    }
                }
            }
            return false;
        }

        private int dfs(int[] ptr, int[] level, int u, int sink, int flow) {
            if (u == sink)
                return flow;
            for (; ptr[u] < graph[u].size(); ptr[u]++) {
                Edge edge = graph[u].get(ptr[u]);
                if (level[edge.to] == level[u] + 1 && edge.capacity > 0) {
                    int pushed = dfs(ptr, level, edge.to, sink, Math.min(flow, edge.capacity));
                    if (pushed > 0) {
                        edge.capacity -= pushed;
                        edge.reverse.capacity += pushed;
                        return pushed;
                    }
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_26\\service.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_26\\2.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());
        while (T-- > 0) {
            String[] firstLine = br.readLine().trim().split(" ");
            int N = Integer.parseInt(firstLine[0]);
            int P = Integer.parseInt(firstLine[1]);
            int M = Integer.parseInt(firstLine[2]);

            int[] F = Arrays.stream(br.readLine().trim().split(" ")).mapToInt(Integer::parseInt).toArray();

            int nodeCount = N + Arrays.stream(F).sum() + 2;
            int source = nodeCount - 2;
            int sink = nodeCount - 1;

            Graph graph = new Graph(nodeCount);

            for (int i = 0; i < N; i++) {
                graph.addEdge(source, i, M);
            }

            int regionIndexStart = N;
            for (int i = 0; i < P; i++) {
                for (int j = 0; j < F[i]; j++) {
                    int regionIndex = regionIndexStart + j;
                    graph.addEdge(regionIndex, sink, 1);
                }
                regionIndexStart += F[i];
            }

            regionIndexStart = N;
            for (int i = 0; i < N; i++) {
                String[] teamPrefs = br.readLine().trim().split(" ");
                int numPrefs = Integer.parseInt(teamPrefs[0]);
                for (int j = 0; j < numPrefs; j++) {
                    int period = Integer.parseInt(teamPrefs[1 + 2 * j]) - 1;
                    int region = Integer.parseInt(teamPrefs[2 + 2 * j]) - 1;
                    int regionIndex = regionIndexStart + region + (period == 0 ? 0 : Arrays.stream(F, 0, period).sum());
                    graph.addEdge(i, regionIndex, 1);
                }
            }
            int maxFlow = graph.maxFlow(source, sink);
            int totalRegions = Arrays.stream(F).sum();
            sb.append(maxFlow == totalRegions ? 1 : 0).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

}