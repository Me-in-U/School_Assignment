import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class rangeSum {
    protected static long[] A;
    protected static long[] BIT;
    protected static int N;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("rangeSum.out"));
        BufferedReader br = new BufferedReader(new FileReader("rangeSum.inp"));
        StringBuilder sb = new StringBuilder();
        N = Integer.parseInt(br.readLine());
        A = new long[N + 1];
        BIT = new long[N + 1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            A[i] = Long.parseLong(st.nextToken());
            update(i, A[i]);
        }
        while (true) {
            st = new StringTokenizer(br.readLine());
            char command = st.nextToken().charAt(0);
            int a = Integer.parseInt(st.nextToken());
            long b = Long.parseLong(st.nextToken());
            if (command == 'c') {
                long diff = b - A[a];
                A[a] = b;
                update(a, diff);
            } else if (command == 's') {
                sb.append(sum((int) b) - sum(a - 1)).append('\n');
            } else if (command == 'q') {
                break;
            }
        }
        bw.write(sb.toString().trim());
        bw.flush();
        bw.close();
        br.close();
    }

    public static void update(int index, long diff) {
        while (index <= N) {
            BIT[index] += diff;
            index += (index & -index);
        }
    }

    public static long sum(int index) {
        long sum = 0;
        while (index > 0) {
            sum += BIT[index];
            index -= (index & -index);
        }
        return sum;
    }
}