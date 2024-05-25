package 문제해결기법.Assignment_28;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.StringTokenizer;

public class scc {
    protected static ArrayList<Integer>[] graph;
    protected static ArrayList<Integer>[] reversedGraph;
    protected static ArrayList<Integer>[] sccGraph;
    protected static Deque<Integer> stack;
    protected static boolean[] visited;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_28\\scc.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_28\\2.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numVertices = Integer.parseInt(st.nextToken());
        int numEdges = Integer.parseInt(st.nextToken());
        graph = new ArrayList[numVertices + 1];
        reversedGraph = new ArrayList[numVertices + 1];
        sccGraph = new ArrayList[numVertices + 1];
        stack = new ArrayDeque<>();
        visited = new boolean[numVertices + 1];
        for (int i = 1; i <= numVertices; i++) {
            graph[i] = new ArrayList<>();
            reversedGraph[i] = new ArrayList<>();
            sccGraph[i] = new ArrayList<>();
        }
        for (int i = 0; i < numEdges; i++) {
            st = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(st.nextToken()) + 1;
            int v2 = Integer.parseInt(st.nextToken()) + 1;
            graph[v1].add(v2);
            reversedGraph[v2].add(v1);
        }
        for (int i = 1; i <= numVertices; i++)
            if (!visited[i])
                DFS(i);
        visited = new boolean[numVertices + 1];
        int sccCount = 0;
        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited[vertex])
                reverseDFS(vertex, ++sccCount);
        }
        sb.append(sccCount).append('\n');
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    protected static void DFS(int currentVertex) {
        visited[currentVertex] = true;
        for (int nextVertex : graph[currentVertex])
            if (!visited[nextVertex])
                DFS(nextVertex);
        stack.push(currentVertex);
    }

    protected static void reverseDFS(int currentVertex, int sccCount) {
        visited[currentVertex] = true;
        for (int nextVertex : reversedGraph[currentVertex])
            if (!visited[nextVertex])
                reverseDFS(nextVertex, sccCount);
        sccGraph[sccCount].add(currentVertex);
    }
}