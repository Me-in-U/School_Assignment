package 문제해결기법.Assignment_12;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

public class tangent {

    public static class Point implements Comparable<Point> {
        int x;
        int y;
        static Point p0;

        Point() {
            x = 0;
            y = 0;
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        static int orientation(Point a, Point b, Point c) {
            int res = (b.y - a.y) * (c.x - b.x) - (c.y - b.y) * (b.x - a.x);

            if (res == 0)
                return 0;
            if (res > 0)
                return 1;
            return -1;
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

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    static Point mid = new Point();

    // Determines the quadrant of a point
    // (used in compare())
    static int quad(Point p) {
        if (p.x >= 0 && p.y >= 0)
            return 1;
        if (p.x <= 0 && p.y >= 0)
            return 2;
        if (p.x <= 0 && p.y <= 0)
            return 3;
        return 4;
    }

    // Compare function for sorting
    static class PointComparator implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            Point p = new Point(p1.x - mid.x, p1.y - mid.y);
            Point q = new Point(p2.x - mid.x, p2.y - mid.y);

            int one = quad(p);
            int two = quad(q);

            if (one != two)
                return one - two;
            return p.y * q.x - q.y * p.x;
        }
    }

    static double polygonArea(Point[] points, int N) {
        double area = 0;
        for (int i = 0, j = N - 1; i < N; j = i++) {
            area += (points[i].x * points[j].y) - (points[i].y * points[j].x);
        }
        area /= 2;
        return area < 0 ? -area : area;
    }

    protected static Point[] polygon1;
    protected static Point[] polygon1Hull;
    protected static Point[] polygon2;
    protected static Point[] polygon2Hull;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("tangent.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법/Assignment_12/1.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < T; i++) {
            int p1 = Integer.parseInt(br.readLine().trim());
            Point[] polygon1 = new Point[p1];
            List<Point> a = new ArrayList<>();
            for (int j = 0; j < p1; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon1[j] = new Point(x, y);
                a.add(new Point(x, y));
            }
            System.out.println(polygonArea(polygon1, polygon1.length));

            int p2 = Integer.parseInt(br.readLine().trim());
            Point[] polygon2 = new Point[p2];
            List<Point> b = new ArrayList<>();
            for (int j = 0; j < p2; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon2[j] = new Point(x, y);
                b.add(new Point(x, y));
            }
            System.out.println(polygonArea(polygon2, polygon2.length));

            polygon1Hull = convexHull(polygon1);
            polygon2Hull = convexHull(polygon2);
            System.out.println(Arrays.toString(polygon1Hull));
            System.out.println(Arrays.toString(polygon2Hull));

        }
        System.out.print(sb.toString());
        bw.close();
        br.close();
    }

    public static Point[] convexHull(Point[] points) {
        int minX = points[0].x;
        int minY = points[0].y;
        int minIndex = 0;
        int n = points.length;
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
        int hullSize = stack.size();
        Point[] hull = new Point[hullSize];
        for (int i = 0; i < hullSize; i++) {
            hull[i] = stack.pollFirst();
        }
        return hull;
    }

    public static Point underTheTop(Deque<Point> stack) {
        Point p = stack.pollLast();
        Point result = stack.peekLast();
        stack.addLast(p);
        return result;
    }
}