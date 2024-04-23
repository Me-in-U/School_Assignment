import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.StringTokenizer;

public class point {
    public static class Point implements Comparable<Point> {
        long x;
        long y;
        static Point p0;

        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public static int ccw(Point p, Point q, Point r) {
            long val = (q.y - p.y) * (r.x - q.x) -
                    (q.x - p.x) * (r.y - q.y);
            if (val == 0)
                return 0;
            return (val > 0) ? 1 : 2;
        }

        public static void swap(Point p1, Point p2) {
            long tempX = p1.x;
            long tempY = p1.y;
            p1.x = p2.x;
            p1.y = p2.y;
            p2.x = tempX;
            p2.y = tempY;
        }

        public static long distSq(Point p1, Point p2) {
            return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        }

        public int compareTo(Point p) {
            int o = ccw(p0, this, p);
            if (o == 0)
                return (distSq(p0, p) >= distSq(p0, this)) ? -1 : 1;
            return (o == 2) ? -1 : 1;
        }

        public static int ccw1(Point p, Point q, Point r) {
            long val = (q.y - p.y) * (r.x - q.x) -
                    (r.y - q.y) * (q.x - p.x);
            if (val == 0)
                return 0;
            return (val > 0) ? 1 : -1;
        }
    }

    protected static Point[] points;
    protected static int n = 4;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("point.out"));
        BufferedReader br = new BufferedReader(new FileReader("point.inp"));
        StringBuilder sb = new StringBuilder();
        int N = Integer.parseInt(br.readLine());
        while (N-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            Point p0 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            Point p1 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            Point p2 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            Point p3 = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            if (Point.ccw1(p0, p1, p2) * Point.ccw1(p0, p1, p3) < 0 &&
                    Point.ccw1(p2, p3, p0) * Point.ccw1(p2, p3, p1) < 0) {
                sb.append(1).append('\n');
                continue;
            }
            if (Point.ccw1(p0, p3, p1) * Point.ccw1(p0, p3, p2) < 0 &&
                    Point.ccw1(p1, p2, p0) * Point.ccw1(p1, p2, p3) < 0) {
                sb.append(1).append('\n');
                continue;
            }
            points = new Point[4];
            points[0] = p0;
            points[1] = p1;
            points[2] = p2;
            points[3] = p3;
            sb.append(convexHull() == 4 ? 3 : 2).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    public static int convexHull() {
        long minX = points[0].x;
        long minY = points[0].y;
        int minIndex = 0;
        for (int i = 1; i < n; i++) {
            long x = points[i].x;
            long y = points[i].y;
            if (x < minX || (x == minX && y < minY)) {
                minX = x;
                minY = y;
                minIndex = i;
            }
        }
        Point.swap(points[0], points[minIndex]);
        Point.p0 = points[0];
        Arrays.sort(points, 1, n);
        int resize = 1;
        for (int i = 1; i < n; i++) {
            while ((i < n - 1) &&
                    Point.ccw(Point.p0, points[i], points[i + 1]) == 0)
                i++;
            points[resize++] = points[i];
        }

        Deque<Point> stack = new ArrayDeque<>();
        stack.addLast(points[0]);
        stack.addLast(points[1]);
        stack.addLast(points[2]);
        for (int i = 3; i < resize; i++) {
            while ((stack.size() > 1) &&
                    Point.ccw(underTheTop(stack), stack.peekLast(), points[i]) != 2)
                stack.pollLast();
            stack.addLast(points[i]);
        }
        return stack.size();
    }

    public static Point underTheTop(Deque<Point> stack) {
        Point p = stack.pollLast();
        Point result = stack.peekLast();
        stack.addLast(p);
        return result;
    }
}