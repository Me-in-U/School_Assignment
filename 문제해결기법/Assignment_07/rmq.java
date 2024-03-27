import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class rmq {
    protected static int N;
    protected static int[] arr;
    protected static int[] tree;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("rmq.out"));
        BufferedReader br = new BufferedReader(new FileReader("rmq.inp"));
        N = Integer.parseInt(br.readLine());
        arr = new int[N];
        StringTokenizer st = new StringTokenizer("");
        for (int i = 0; i < N; i++) {
            if (!st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            arr[i] = Integer.parseInt(st.nextToken());
        }
        br.readLine();
        makeTree();
        long sum = 0;
        while (true) {
            st = new StringTokenizer(br.readLine());
            char type = st.nextToken().charAt(0);
            if (type == 'q') {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                sum += query(0, N - 1, 0, l, r);
            } else if (type == 'c') {
                int idx = Integer.parseInt(st.nextToken());
                int val = Integer.parseInt(st.nextToken());
                update(0, N - 1, 0, idx, val);
            } else {
                break;
            }
        }
        bw.write(String.valueOf(sum % 100000));
        bw.flush();
        bw.close();
        br.close();
    }

    private static void makeTree() {
        int height = (int) (Math.ceil(Math.log(N) / Math.log(2)) + 1);
        int maxSize = 2 * (int) Math.pow(2, height) - 1;
        tree = new int[maxSize];
        build(0, N - 1, 0);
    }

    private static void build(int start, int end, int node) {
        if (start == end) {
            tree[node] = start;
        } else {
            int mid = (start + end) / 2;
            build(start, mid, 2 * node + 1);
            build(mid + 1, end, 2 * node + 2);
            tree[node] = arr[tree[2 * node + 1]] <= arr[tree[2 * node + 2]] ? tree[2 * node + 1]
                    : tree[2 * node + 2];
        }
    }

    private static void update(int start, int end, int node, int idx, int val) {
        if (start == end) {
            arr[idx] = val;
        } else {
            int mid = (start + end) / 2;
            if (start <= idx && idx <= mid)
                update(start, mid, 2 * node + 1, idx, val);
            else
                update(mid + 1, end, 2 * node + 2, idx, val);
            tree[node] = arr[tree[2 * node + 1]] <= arr[tree[2 * node + 2]] ? tree[2 * node + 1]
                    : tree[2 * node + 2];
        }
    }

    private static int query(int start, int end, int node, int l, int r) {
        if (r < start || end < l)
            return -1;
        if (l <= start && end <= r)
            return tree[node];
        int mid = (start + end) / 2;
        int p1 = query(start, mid, 2 * node + 1, l, r);
        int p2 = query(mid + 1, end, 2 * node + 2, l, r);
        if (p1 == -1)
            return p2;
        if (p2 == -1)
            return p1;
        return arr[p1] <= arr[p2] ? p1 : p2;
    }
}