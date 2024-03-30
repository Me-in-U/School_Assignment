import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayDeque;

public class hull {
    public static class Point implements Comparable<Point> {
        int x;
        int y;
        static Point p0;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static int ccw(Point p, Point q, Point r) {
            int val = (q.y - p.y) * (r.x - q.x) -
                    (q.x - p.x) * (r.y - q.y);
            if (val == 0)
                return 0;
            return (val > 0) ? 1 : 2;
        }

        public static void swap(Point p1, Point p2) {
            int tempX = p1.x;
            int tempY = p1.y;
            p1.x = p2.x;
            p1.y = p2.y;
            p2.x = tempX;
            p2.y = tempY;
        }

        public static int distSq(Point p1, Point p2) {
            return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        }

        public int compareTo(Point p) {
            int o = ccw(p0, this, p);
            if (o == 0)
                return (distSq(p0, p) >= distSq(p0, this)) ? -1 : 1;
            return (o == 2) ? -1 : 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    protected static int n;
    protected static Point[] points;
    protected static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("hull.out"));
        BufferedReader br = new BufferedReader(new FileReader("hull.inp"));
        n = Integer.parseInt(br.readLine());
        points = new Point[n];
        StringTokenizer st = null;
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            points[i] = new Point(x, y);
        }
        convexHull();
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    public static void convexHull() {
        int minX = points[0].x;
        int minY = points[0].y;
        int minIndex = 0;
        for (int i = 1; i < n; i++) {
            int x = points[i].x;
            int y = points[i].y;
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

        sb.append(stack.size()).append('\n');
        while (!stack.isEmpty()) {
            Point p = stack.pollFirst();
            sb.append(p.x).append(' ').append(p.y).append('\n');
        }
    }

    public static Point underTheTop(Deque<Point> stack) {
        Point p = stack.pollLast();
        Point result = stack.peekLast();
        stack.addLast(p);
        return result;
    }
}