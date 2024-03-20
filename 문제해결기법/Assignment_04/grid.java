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
        while (T-- > 0) {
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

            boolean[][] unable = new boolean[x + 1][y + 1];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < b; j++)
                unable[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = true;

            if (unable[0][0] || unable[x][y]) {
                sb.append(0).append('\n');
                continue;
            } else {
                routeCount[0][0][0] = 1;
            }

            for (int xx = 1; xx <= x; xx++) {
                if (!unable[xx][0]) {
                    if (dots[xx][0]) {
                        for (int dot = 1; dot <= 10; dot++) {
                            routeCount[xx][0][dot] = routeCount[xx - 1][0][dot - 1];
                        }
                        routeCount[xx][0][10] = ((routeCount[xx][0][10] % MOD) + (routeCount[xx - 1][0][10] % MOD))
                                % MOD;
                    } else {
                        for (int dot = 0; dot <= 10; dot++) {
                            routeCount[xx][0][dot] = routeCount[xx - 1][0][dot];
                        }
                    }
                } else {
                    break;
                }
            }

            for (int yy = 1; yy <= y; yy++) {
                if (!unable[0][yy]) {
                    if (dots[0][yy]) {
                        for (int dot = 1; dot <= 10; dot++) {
                            routeCount[0][yy][dot] = routeCount[0][yy - 1][dot - 1];
                        }
                        routeCount[0][yy][10] = ((routeCount[0][yy][10] % MOD) + (routeCount[0][yy - 1][10] % MOD))
                                % MOD;
                    } else {
                        for (int dot = 0; dot <= 10; dot++) {
                            routeCount[0][yy][dot] = routeCount[0][yy - 1][dot];
                        }
                    }
                } else {
                    break;
                }
            }
            for (int xx = 1; xx <= x; xx++) {
                for (int yy = 1; yy <= y; yy++) {
                    if (!unable[xx][yy]) {
                        if (dots[xx][yy]) {
                            for (int dot = 1; dot <= 10; dot++) {
                                routeCount[xx][yy][dot] = ((routeCount[xx - 1][yy][dot - 1] % MOD)
                                        + (routeCount[xx][yy - 1][dot - 1] % MOD))
                                        % MOD;
                            }
                            routeCount[xx][yy][10] = ((routeCount[xx][yy][10] % MOD) +
                                    ((routeCount[xx - 1][yy][10] % MOD)
                                            + (routeCount[xx][yy - 1][10] % MOD))
                                            % MOD)
                                    % MOD;
                        } else {
                            for (int dot = 0; dot <= 10; dot++) {
                                routeCount[xx][yy][dot] = ((routeCount[xx - 1][yy][dot] % MOD)
                                        + (routeCount[xx][yy - 1][dot] % MOD))
                                        % MOD;
                            }
                        }
                    }
                }
            }
            System.out.print("(" + x + "," + y + ") ->");
            for (int i = 0; i <= 10; i++) {
                System.out.print(routeCount[x][y][i] + " ");
            }
            System.out.println();
            int result = 0;
            for (int kk = k; kk <= 10; kk++) {
                result = ((result % MOD) + (routeCount[x][y][kk] % MOD)) % MOD;
            }
            sb.append(result).append('\n');
        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
