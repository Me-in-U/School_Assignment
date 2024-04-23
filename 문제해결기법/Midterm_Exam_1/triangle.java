import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class triangle {
    public static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("triangle.out"));
        BufferedReader br = new BufferedReader(new FileReader("triangle.inp"));
        StringBuilder sb = new StringBuilder();
        int N = Integer.parseInt(br.readLine());
        while (N-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            Point p1 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            Point p2 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            Point p3 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            double x = calcLength(p1, p2);
            double y = calcLength(p2, p3);
            double z = calcLength(p3, p1);
            if (x >= y && x >= z)
                sb.append(checkQ(x, y, z));
            else if (y >= x && y >= z)
                sb.append(checkQ(y, x, z));
            else
                sb.append(checkQ(z, x, y));
            sb.append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    public static double calcLength(Point p1, Point p2) {
        return Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2);
    }

    static int checkQ(double max, double a, double b) {
        if (a + b == max)
            return 1;
        else if (a + b < max)
            return 2;
        else
            return 0;
    }
}