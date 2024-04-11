import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class rectangles {
    protected static boolean[][] arr;
    protected static boolean[] visited;
    protected static int N;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("cycle.out"));
        BufferedReader br = new BufferedReader(new FileReader("cycle.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            N = Integer.parseInt(br.readLine());
            arr = new boolean[N + 1][N + 1];
            visited = new boolean[N + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++)
                arr[j][Integer.parseInt(st.nextToken())] = true;
            int cycles = 0;
            for (int currentNode = 1; currentNode <= N; currentNode++)
                if (!visited[currentNode])
                    if (doCycle(currentNode))
                        cycles++;
            sb.append(cycles).append("\n");
        }
        System.out.print(sb.toString());
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    public static boolean doCycle(int currentNode) {
        if (visited[currentNode])
            return false;
        visited[currentNode] = true;
        for (int nextNode = 1; nextNode <= N; nextNode++)
            if (arr[currentNode][nextNode])
                if (!visited[nextNode] && doCycle(nextNode) || visited[nextNode])
                    return visited[currentNode] = true;
        visited[currentNode] = false;
        return false;
    }
}