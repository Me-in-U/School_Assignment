package 문제해결기법.Assignment_31;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class show {
    protected static int size;
    protected static int N;
    protected static int M;
    protected static int[] scc;
    protected static List<Integer>[] graph;
    protected static List<Integer>[] graphReversed;
    protected static boolean[] visited;
    protected static Deque<Integer> stack = new ArrayDeque<>();
    protected static StringBuilder sb = new StringBuilder();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_31\\show.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_31\\0.inp"));
        StringTokenizer st = null;

        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            size = 2 * N + 1;
            scc = new int[size];
            graph = new ArrayList[size];
            graphReversed = new ArrayList[size];
            visited = new boolean[size];
            for (int i = 0; i < size; i++) {
                graph[i] = new ArrayList<>();
                graphReversed[i] = new ArrayList<>();
            }
            for (int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                int bulb1 = Integer.parseInt(st.nextToken());
                char bulb1Color = st.nextToken().charAt(0);
                int bulb2 = Integer.parseInt(st.nextToken());
                char bulb2Color = st.nextToken().charAt(0);
                int bulb3 = Integer.parseInt(st.nextToken());
                char bulb3Color = st.nextToken().charAt(0);

                if (bulb1Color == 'B')
                    bulb1 = not(bulb1);
                if (bulb2Color == 'B')
                    bulb2 = not(bulb2);
                if (bulb3Color == 'B')
                    bulb3 = not(bulb3);

                addGraph(bulb1, bulb2);
                addGraph(bulb2, bulb3);
                addGraph(bulb3, bulb1);
            }

            for (int i = 1; i <= 2 * N; i++)
                if (!visited[i])
                    dfs(i);

            int sccCount = 0;
            visited = new boolean[size];
            while (!stack.isEmpty()) {
                int bulb = stack.pop();
                if (visited[bulb])
                    continue;
                dfsReverse(bulb, sccCount++);
            }

            boolean flag = true;
            for (int i = 1; i <= N; i++) {
                if (scc[i] == scc[N + i]) {
                    flag = false;
                    break;
                }
            }
            sb.append(flag ? 1 : -1).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    static int notX(int x) {
        if (x > N)
            return x - N;
        return x + N;
    }

    public static int not(int bulb) {
        if (bulb > N)
            return bulb - N;
        return bulb + N;
    }

    public static void addGraph(int x, int y) {
        graph[not(x)].add(y);
        graph[not(y)].add(x);
        graphReversed[x].add(not(y));
        graphReversed[y].add(not(x));
    }

    public static void dfs(int bulb) {
        visited[bulb] = true;
        for (int connectBulb : graph[bulb])
            if (!visited[connectBulb])
                dfs(connectBulb);
        stack.push(bulb);
    }

    public static void dfsReverse(int bulb, int sccCount) {
        visited[bulb] = true;
        for (int connectBulb : graphReversed[bulb])
            if (!visited[connectBulb])
                dfsReverse(connectBulb, sccCount);
        scc[bulb] = sccCount;
    }
}