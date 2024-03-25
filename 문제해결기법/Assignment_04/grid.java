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
        BufferedReader br = new BufferedReader(new FileReader("grid.inp"));
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

            int[][][] routeCount = new int[x + 1][y + 1][k + 1];

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
            }

            routeCount[0][0][0] = 1;
            for (int xx = 1; xx <= x; xx++) {
                if (!unable[xx][0]) {
                    if (dots[xx][0]) {
                        for (int dot = 1; dot <= k; dot++) {
                            routeCount[xx][0][dot] = routeCount[xx - 1][0][dot - 1];
                        }
                        routeCount[xx][0][k] += routeCount[xx - 1][0][k];
                    } else {
                        System.arraycopy(routeCount[xx - 1][0], 0,
                                routeCount[xx][0], 0, k + 1);
                    }
                } else {
                    break;
                }
            }

            for (int yy = 1; yy <= y; yy++) {
                if (!unable[0][yy]) {
                    if (dots[0][yy]) {
                        for (int dot = 1; dot <= k; dot++) {
                            routeCount[0][yy][dot] = routeCount[0][yy - 1][dot - 1];
                        }
                        routeCount[0][yy][k] += routeCount[0][yy - 1][k];
                    } else {
                        System.arraycopy(routeCount[0][yy - 1], 0,
                                routeCount[0][yy], 0, k + 1);
                    }
                } else {
                    break;
                }
            }

            for (int xx = 1; xx <= x; xx++) {
                for (int yy = 1; yy <= y; yy++) {
                    if (!unable[xx][yy]) {
                        if (dots[xx][yy]) {
                            for (int dot = 1; dot <= k; dot++) {
                                routeCount[xx][yy][dot] = (routeCount[xx - 1][yy][dot - 1]
                                        + routeCount[xx][yy - 1][dot - 1]) % MOD;
                            }
                            routeCount[xx][yy][k] = (routeCount[xx][yy][k] + (routeCount[xx - 1][yy][k]
                                    + (routeCount[xx][yy - 1][k])) % MOD) % MOD;

                        } else {
                            for (int dot = 0; dot <= k; dot++) {
                                routeCount[xx][yy][dot] = (routeCount[xx - 1][yy][dot]
                                        + routeCount[xx][yy - 1][dot]) % MOD;
                            }
                        }
                    }
                }
            }
            sb.append(routeCount[x][y][k]).append('\n');
        }
        bs.write(sb.toString().trim().getBytes());
        bs.close();
        br.close();
    }
}