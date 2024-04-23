package 문제해결기법.Midterm_Exam_5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class dish {
    static long[][][] dp;
    static int mod = 1_000_000_007;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Midterm_Exam_5\\dish.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Midterm_Exam_5\\1.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int l = Integer.parseInt(st.nextToken());
            int r = n;
            dp = new long[n + 1][n + 1][n + 1];
            solve(n, l, r);
            long sum = 0;
            for (int i = 0; i <= n; i++) {
                sum = (sum + dp[n][l][i]) % mod;
            }
            sb.append(n).append(' ').append(l).append(' ').append(sum).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    static void solve(int n, int l, int r) {
        dp[1][1][1] = 1;
        for (int i = 2; i < n + 1; i++) {
            dp[i][i][1] = dp[i][1][i] = 1;
            for (int j = 1; j < l + 1; j++) {
                for (int k = 1; k < r + 1; k++) {
                    dp[i][j][k] = (dp[i - 1][j][k - 1] + dp[i - 1][j - 1][k]
                            + (dp[i - 1][j][k] * (i - 2))) % mod;
                }
            }
        }
    }
}
