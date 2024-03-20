package 문제해결기법.Assignment_04;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class grid2 {
    public static void main(String agrs[]) throws Exception {

        try {
            FileReader fileReader = new FileReader("grid.inp");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grid.out"));
            int mod = 1000000007;

            int testcase = Integer.parseInt(bufferedReader.readLine());

            StringBuilder sb = new StringBuilder();

            for (int t = 0; t < testcase; t++) {
                int X = 0;
                int Y = 0;
                int a = 0;
                int b = 0;
                int k = 0;

                String line = bufferedReader.readLine();
                StringTokenizer st = new StringTokenizer(line, " ");
                X = Integer.parseInt(st.nextToken());
                Y = Integer.parseInt(st.nextToken());
                a = Integer.parseInt(st.nextToken());
                b = Integer.parseInt(st.nextToken());
                k = Integer.parseInt(st.nextToken());

                int x_axis_done = 0;
                int y_axis_done = 0;
                // 점이 o인지 x 인지 구별 o 이면 안에 1 ,x면 안에 2
                int[][] dots = new int[X + 1][Y + 1];

                line = bufferedReader.readLine();
                st = new StringTokenizer(line, " ");
                while (st.hasMoreTokens()) {
                    dots[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = 1;
                }

                line = bufferedReader.readLine();
                st = new StringTokenizer(line, " ");
                while (st.hasMoreTokens()) {
                    dots[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())] = 2;
                }

                // ----------------------------------------------------------------------------------

                // 내가 지금 지나갈 점들의 집합
                int[][][] point = new int[X + 1][Y + 1][k + 1];

                // 맨 처음점은 1,0,0 이렇게
                point[0][0][0] = 1;

                for (int y = 0; y < Y + 1; y++) {
                    for (int x = 0; x < X + 1; x++) {
                        if (x == 0 && y == 0) {
                            continue;
                        }

                        // 못가는 점 일때
                        if (dots[x][y] == 2) {
                            if (x == 0) {
                                // 그뒤에 모든 y축은 안봐도됨
                                y_axis_done = 1;
                            } else if (y == 0) {
                                // 그뒤에 모든 x축은 안봐도됨
                                x_axis_done = 1;
                            }
                        } else if (dots[x][y] == 1) {
                            if (y == 0) {
                                // x축에 x표시가 내앞에 없었을때,
                                if (x_axis_done == 0) {
                                    // 오른쪽으로한칸씩 떙겨줌
                                    for (int z = k - 1; z >= 0; z--) {
                                        // 마지막인덱스에서는 더해줌
                                        if (z == k - 1) {
                                            point[x][y][z + 1] = (point[x - 1][y][z] + point[x - 1][y][z + 1]) % mod;
                                        } else {
                                            point[x][y][z + 1] = point[x - 1][y][z];
                                        }
                                    }
                                    point[x][y][0] = 0;
                                }

                            } else if (x == 0) {
                                // y축에 x표시가 내앞에 없었을때,
                                if (y_axis_done == 0) {
                                    // 오른쪽으로한칸씩 떙겨줌
                                    for (int z = k - 1; z >= 0; z--) {
                                        // 마지막인덱스에서는 더해줌
                                        if (z == k - 1) {
                                            point[x][y][z + 1] = (point[x][y - 1][z] + point[x][y - 1][z + 1]) % mod;
                                        } else {
                                            point[x][y][z + 1] = point[x][y - 1][z];
                                        }
                                    }
                                    point[x][y][0] = 0;
                                }
                            } else {
                                for (int z = k - 1; z >= 0; z--) {
                                    // 마지막인덱스에서는 더해줌
                                    if (z == k - 1) {
                                        point[x][y][z + 1] = ((point[x - 1][y][z] + point[x][y - 1][z]) % mod
                                                + (point[x - 1][y][z + 1] + point[x][y - 1][z + 1]) % mod) % mod;
                                    } else {
                                        point[x][y][z + 1] = (point[x - 1][y][z] + point[x][y - 1][z]) % mod;
                                    }
                                }
                                point[x][y][0] = 0;
                            }
                        } else {
                            // o도 x도아닌점에대해서는 그냥 더해줌
                            if (y == 0) {
                                if (x_axis_done == 0) {
                                    for (int z = 0; z <= k; z++) {
                                        point[x][y][z] = point[x - 1][y][z];
                                    }
                                }
                            } else if (x == 0) {
                                if (y_axis_done == 0) {
                                    for (int z = 0; z <= k; z++) {
                                        point[x][y][z] = point[x][y - 1][z];
                                    }
                                }
                            } else {
                                for (int z = 0; z <= k; z++) {
                                    point[x][y][z] = (point[x - 1][y][z] + point[x][y - 1][z]) % mod;
                                }
                            }
                        }
                        if (x == X && y == Y) {
                            sb.append(point[X][Y][k] + "\n");
                        }
                    }
                }
            }
            String result = sb.toString();
            bufferedWriter.write(result.trim());
            bufferedWriter.flush();
        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }
}