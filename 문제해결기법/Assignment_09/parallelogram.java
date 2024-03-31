import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class parallelogram {
    public static class Point {
        long x;
        long y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        static long distance(Point p1, Point p2) {
            long a = p2.x - p1.x;
            long b = p2.y - p1.y;
            return a * a + b * b;
        }
    }

    static boolean isParallelogram(Point[] points) {
        Set<Long> distancesSet = new HashSet<>();
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                distancesSet.add(Point.distance(points[i], points[j]));
        return distancesSet.size() <= 4;
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("parallelogram.out"));
        BufferedReader br = new BufferedReader(new FileReader("parallelogram.inp"));
        StringBuilder sb = new StringBuilder();
        int[] data;
        while (true) {
            data = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (data[0] == 0 && data[1] == 0 && data[2] == 0 && data[3] == 0 &&
                    data[4] == 0 && data[5] == 0 && data[6] == 0 && data[7] == 0) {
                break;
            }
            Point[] points = new Point[4];
            int index = 0;
            for (int i = 0; i < 8; i += 2) {
                points[index++] = new Point(data[i], data[i + 1]);
            }
            sb.append(isParallelogram(points) ? 1 : 0).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}