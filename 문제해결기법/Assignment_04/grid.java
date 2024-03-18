package 문제해결기법.Assignment_04;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class grid {
    public static final int MOD = 1_000_000_007;

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("grid.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_04\\2.inp"));
        StringTokenizer st = null;
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            int[][][] routeCount = new int[x + 1][y + 1][11];

            boolean[][] dots = new boolean[x + 1][y + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < a; j++)
                dots[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;

            boolean[][] unables = new boolean[x + 1][y + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < b; j++)
                unables[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;

            if (!unables[0][0]) {
                routeCount[0][0][0] = 1;
            } else {
                sb.append(0).append('\n');
                continue;
            }

            for (int j = 1; j <= x; j++) {
                if (!unables[j][0]) {
                    if (dots[j][0]) {
                        for (int dot = 1; dot <= 10; dot++) {
                            routeCount[j][0][dot] = routeCount[j - 1][0][dot - 1] % MOD;
                        }
                        routeCount[j][0][10] = ((routeCount[j][0][10] % MOD) + (routeCount[j - 1][0][10] % MOD)) % MOD;
                    } else {
                        for (int dot = 0; dot <= 10; dot++) {
                            routeCount[j][0][dot] = routeCount[j - 1][0][dot] % MOD;
                        }
                    }
                }
            }
            for (int j = 1; j <= y; j++) {
                if (!unables[0][j]) {
                    if (dots[0][j]) {
                        for (int dot = 1; dot <= 10; dot++) {
                            routeCount[0][j][dot] = routeCount[0][j - 1][dot - 1] % MOD;
                        }
                        routeCount[0][j][10] = ((routeCount[0][j][10] % MOD) + (routeCount[0][j - 1][10] % MOD)) % MOD;
                    } else {
                        for (int dot = 0; dot <= 10; dot++) {
                            routeCount[0][j][dot] = routeCount[0][j - 1][dot] % MOD;
                        }
                    }
                }
            }

            for (int j = 1; j <= x; j++) {
                for (int j2 = 1; j2 <= y; j2++) {
                    if (!unables[j][j2]) {
                        if (dots[j][j2]) {
                            for (int dot = 1; dot <= 10; dot++) {
                                routeCount[j][j2][dot] = ((routeCount[j - 1][j2][dot - 1] % MOD)
                                        + (routeCount[j][j2 - 1][dot - 1] % MOD))
                                        % MOD;
                            }
                            routeCount[j][j2][10] = ((routeCount[j][j2][10] % MOD) +
                                    ((routeCount[j - 1][j2][10] % MOD)
                                            + (routeCount[j][j2 - 1][10] % MOD))
                                            % MOD)
                                    % MOD;
                        } else {
                            for (int dot = 0; dot <= 10; dot++) {
                                routeCount[j][j2][dot] = ((routeCount[j - 1][j2][dot] % MOD)
                                        + (routeCount[j][j2 - 1][dot] % MOD))
                                        % MOD;
                            }
                        }
                    }
                }
            }
            int result = 0;
            for (int j = k; j <= 10; j++)
                result = ((result % MOD) + (routeCount[x][y][j] % MOD)) % MOD;
            sb.append(result).append('\n');
        }
        System.out.print(sb.toString());
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
