package 문제해결기법.Assignment_33;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class bigFibonacci {
    protected static final long MOD = 1000000007L;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_33\\bigFibonacci.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_33\\1.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            long n = Long.parseLong(br.readLine());
            sb.append(n).append(' ').append(fibonacci(n)).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    public static long fibonacci(long n) {
        if (n <= 1)
            return n;

        long[][] result = {
                { 1, 0 },
                { 0, 1 }
        };
        long[][] fiboM = {
                { 1, 1 },
                { 1, 0 }
        };
        while (n > 0) {
            if (n % 2 == 1)
                result = multiplyMatrix(result, fiboM);
            n /= 2;
            fiboM = multiplyMatrix(fiboM, fiboM);
        }
        return result[1][0];
    }

    public static long[][] multiplyMatrix(long[][] a, long[][] b) {
        long[][] result = new long[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 2; k++) {
                    result[i][j] = (result[i][j] + (a[i][k] * b[k][j])) % MOD;
                }
            }
        }
        return result;
    }
}
