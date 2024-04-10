package 문제해결기법.Assignment_12;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class tangentTest2 {

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

        public static int distSq(Point p1, Point p2) {
            return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        }

        public int compareTo(Point p) {
            int o = ccw(p0, this, p);
            if (o == 0)
                return (distSq(p0, p) >= distSq(p0, this)) ? -1 : 1;
            return o;
        }

        static int ccw(Point a, Point b, Point c) {
            int res = (b.y - a.y) * (c.x - b.x) -
                    (c.y - b.y) * (b.x - a.x);

            if (res == 0)
                return 0;
            if (res > 0)
                return 1;
            return -1;
        }

        public static void swap(Point p1, Point p2) {
            int tempX = p1.x;
            int tempY = p1.y;
            p1.x = p2.x;
            p1.y = p2.y;
            p2.x = tempX;
            p2.y = tempY;
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
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    static int orientation(Point a, Point b, Point c) {
        int res = (b.y - a.y) * (c.x - b.x) -
                (c.y - b.y) * (b.x - a.x);

        if (res == 0)
            return 0;
        if (res > 0)
            return 1;
        return -1;
    }

    static int orientation2(Point a, Point b, Point c) {
        int val = (b.y - a.y) * (c.x - b.x) -
                (b.x - a.x) * (c.y - b.y);

        if (val == 0)
            return 0; // colinear

        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    static Point mid = new Point();

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

    public static List<Point> deepCopyList(List<Point> original) {
        List<Point> copy = new ArrayList<>();
        for (Point p : original) {
            copy.add(new Point(p.x, p.y)); // Point 객체도 새로 생성하여 복사
        }
        return copy;
    }

    public static Point[] deepCopyArray(Point[] original) {
        Point[] copy = new Point[original.length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new Point(original[i].x, original[i].y); // 새 Point 객체 생성
        }
        return copy;
    }

    public static void mergePolygonsWithTangents() {
        System.out.println("upsidedown : " + upsideDown);
        int upperAIdx = findIndex(polygon1, upperTangent[0]); // Upper tangent index on A
        int upperBIdx = findIndex(polygon2, upperTangent[1]); // Upper tangent index on B
        System.out.println("index Upper: " + upperAIdx + " " + upperBIdx);
        int lowerAIdx = findIndex(polygon1, lowerTangent[0]); // Lower tangent index on A
        int lowerBIdx = findIndex(polygon2, lowerTangent[1]); // Lower tangent index on B
        System.out.println("index Lower: " + lowerAIdx + " " + lowerBIdx);

        List<Point> mergedPolygonList = new ArrayList<>();

        // Add points from polygon A between upper and lower tangents
        int currentIndex = upperAIdx;
        while (true) {
            mergedPolygonList.add(polygon1[currentIndex]);
            if (currentIndex == lowerAIdx) {
                break;
            }
            currentIndex = (currentIndex + 1) % polygon1.length;
        }
        // Add the lower tangent point from polygon A
        mergedPolygonList.add(polygon1[lowerAIdx]);

        // Add points from polygon B between lower and upper tangents
        currentIndex = lowerBIdx;
        while (true) {
            mergedPolygonList.add(polygon2[currentIndex]);
            if (currentIndex == upperBIdx) {
                break;
            }
            currentIndex = (currentIndex + 1) % polygon2.length;
        }
        // Add the upper tangent point from polygon B to close the polygon
        mergedPolygonList.add(polygon2[upperBIdx]);

        // Convert the List<Point> to Point[]
        mergedPolygon = new Point[mergedPolygonList.size()];
        mergedPolygon = mergedPolygonList.toArray(mergedPolygon);
    }

    public static int findIndex(Point[] polygon, Point target) {

        for (int i = 0; i < polygon.length; i++) {
            if (polygon[i].equals(target)) {
                return i;
            }
        }
        System.out.println("Not found" + Arrays.toString(polygon));
        System.out.println(target.x + ", " + target.y);
        return -1; // Not found
    }

    // Finds upper tangent of two polygons 'a' and 'b'
    // represented as two lists of points.
    static Point[] findUpperTangent(List<Point> a, List<Point> b) {
        // n1 -> number of points in polygon a
        // n2 -> number of points in polygon b
        int n1 = a.size(), n2 = b.size();

        // To find a point inside the convex polygon (centroid),
        // we sum up all the coordinates and then divide by
        // n (number of points). But this would be a floating-point
        // value. So to get rid of this, we multiply points
        // initially with n1 and then find the center and
        // then divide it by n1 again.
        // Similarly, we do divide and multiply for n2 (i.e.,
        // elements of b)

        // maxa and minb are used to check if polygon a
        // is left of b.
        int maxa = Integer.MIN_VALUE;
        for (int i = 0; i < n1; i++) {
            maxa = Math.max(maxa, a.get(i).x);
            mid.x += a.get(i).x;
            mid.y += a.get(i).y;
            a.get(i).x *= n1;
            a.get(i).y *= n1;
        }

        // Sorting the points in counter-clockwise order
        // for polygon a
        a.sort(new PointComparator());

        for (int i = 0; i < n1; i++) {
            a.get(i).x /= n1;
            a.get(i).y /= n1;
        }

        mid.x = 0;
        mid.y = 0;

        int minb = Integer.MAX_VALUE;
        for (int i = 0; i < n2; i++) {
            mid.x += b.get(i).x;
            mid.y += b.get(i).y;
            minb = Math.min(minb, b.get(i).x);
            b.get(i).x *= n2;
            b.get(i).y *= n2;
        }

        // Sorting the points in counter-clockwise
        // order for polygon b
        b.sort(new PointComparator());

        for (int i = 0; i < n2; i++) {
            b.get(i).x /= n2;
            b.get(i).y /= n2;
        }

        // If a is to the right of b, swap a and b
        // This makes sure a is left of b.
        if (minb < maxa) {
            List<Point> temp = deepCopyList(a);
            a = deepCopyList(b);
            b = temp;

            n1 = a.size();
            n2 = b.size();

            Point[] temp2 = deepCopyArray(polygon1);
            polygon1 = deepCopyArray(polygon2);
            polygon2 = temp2;

            upsideDown = true;
        }

        // ia -> rightmost point of a
        int ia = 0, ib = 0;
        for (int i = 1; i < n1; i++) {
            if (a.get(i).x > a.get(ia).x)
                ia = i;
        }

        // ib -> leftmost point of b
        for (int i = 1; i < n2; i++) {
            if (b.get(i).x < b.get(ib).x)
                ib = i;
        }

        // Finding the upper tangent
        int inda = ia, indb = ib;
        boolean done = false;
        while (!done) {
            done = true;
            while (orientation(b.get(indb), a.get(inda), a.get((inda + 1) % n1)) > 0)
                inda = (inda + 1) % n1;

            while (orientation(a.get(inda), b.get(indb), b.get((n2 + indb - 1) % n2)) < 0) {
                indb = (n2 + indb - 1) % n2;
                done = false;
            }
        }
        System.out.println("upper tangent (" + a.get(inda).x + "," + a.get(inda).y + ") (" + b.get(indb).x + ","
                + b.get(indb).y + ")");
        return new Point[] { new Point(a.get(inda).x, a.get(inda).y), new Point(b.get(indb).x, b.get(indb).y) };
    }

    // Finds lower tangent of two polygons 'a' and 'b'
    static Point[] findLowerTangent(List<Point> a, List<Point> b) {
        // Similar steps as findUpperTangent with modifications for lower tangent
        int n1 = a.size(), n2 = b.size();
        int ia = 0, ib = 0;
        for (int i = 1; i < n1; i++) {
            if (a.get(i).x > a.get(ia).x)
                ia = i;
        }
        for (int i = 1; i < n2; i++) {
            if (b.get(i).x < b.get(ib).x)
                ib = i;
        }
        // Finding the lower tangent
        int inda = ia, indb = ib;
        boolean done = false;
        while (!done) {
            done = true;
            while (orientation(b.get(indb), a.get(inda), a.get((n1 + inda - 1) % n1)) < 0)
                inda = (n1 + inda - 1) % n1;

            while (orientation(a.get(inda), b.get(indb), b.get((indb + 1) % n2)) > 0) {
                indb = (indb + 1) % n2;
                done = false;
            }
        }

        System.out.println("lower tangent (" + a.get(inda).x + "," + a.get(inda).y + ") (" + b.get(indb).x + ","
                + b.get(indb).y + ")");
        return new Point[] { new Point(a.get(inda).x, a.get(inda).y), new Point(b.get(indb).x, b.get(indb).y) };
    }

    protected static Point[] polygon1;
    protected static List<Point> polygon1Hull;

    protected static Point[] polygon2;
    protected static List<Point> polygon2Hull;

    protected static Point[] mergedPolygon;

    protected static Point[] upperTangent;
    protected static Point[] lowerTangent;
    protected static boolean upsideDown = false;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("tangent.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법/Assignment_12/test.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < T; i++) {
            int polygon1Count = Integer.parseInt(br.readLine().trim());
            polygon1 = new Point[polygon1Count];
            for (int j = 0; j < polygon1Count; j++) {
                st = new StringTokenizer(br.readLine().trim());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon1[j] = new Point(x, y);
            }

            int polygon2Count = Integer.parseInt(br.readLine().trim());
            polygon2 = new Point[polygon2Count];
            for (int j = 0; j < polygon2Count; j++) {
                st = new StringTokenizer(br.readLine().trim());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon2[j] = new Point(x, y);
            }

            polygon1Hull = convexHull(polygon1);
            polygon2Hull = convexHull(polygon2);

            upperTangent = findUpperTangent(polygon1Hull, polygon2Hull);
            lowerTangent = findLowerTangent(polygon1Hull, polygon2Hull);

            mergePolygonsWithTangents();
            System.out.println("poly 1 : " + Arrays.toString(polygon1));
            System.out.println("poly 2 : " + Arrays.toString(polygon2));
            System.out.println("Hull 1 : " + polygon1Hull.toString());
            System.out.println("Hull 2 : " + polygon2Hull.toString());
            System.out.println("Tangent Upper : " + upperTangent[0] + ' ' + upperTangent[1]);
            System.out.println("Tangent Lower : " + lowerTangent[0] + ' ' + lowerTangent[1]);
            System.out.println("merged_polygon : " + Arrays.toString(mergedPolygon));
            System.out.println();
            System.out.println("A m : " + polygonArea(mergedPolygon, mergedPolygon.length));
            System.out.println("A a : " + polygonArea(polygon1, polygon1.length));
            System.out.println("A b : " + polygonArea(polygon2, polygon2.length));
            sb.append(String.format("%.1f",
                    Math.round((polygonArea(mergedPolygon, mergedPolygon.length)
                            - polygonArea(polygon1, polygon1.length)
                            - polygonArea(polygon2, polygon2.length)) * 10) / 10.0))
                    .append('\n');

        }
        System.out.print(sb.toString());
        bw.close();
        br.close();
    }

    public static List<Point> convexHull(Point[] points) {
        Point[] copy = new Point[points.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = new Point(points[i].x, points[i].y);
        }
        int minX = copy[0].x;
        int minY = copy[0].y;
        int minIndex = 0;
        int n = copy.length;
        for (int i = 1; i < n; i++) {
            int x = copy[i].x;
            int y = copy[i].y;
            if (x < minX || (x == minX && y < minY)) {
                minX = x;
                minY = y;
                minIndex = i;
            }
        }
        Point.swap(copy[0], copy[minIndex]);
        Point.p0 = copy[0];
        Arrays.sort(copy, 1, n);
        int resize = 1;
        for (int i = 1; i < n; i++) {
            while ((i < n - 1) &&
                    orientation(Point.p0, copy[i], copy[i + 1]) == 0)
                i++;
            copy[resize++] = copy[i];
        }

        Deque<Point> stack = new ArrayDeque<>();
        stack.addLast(copy[0]);
        stack.addLast(copy[1]);
        stack.addLast(copy[2]);
        for (int i = 3; i < resize; i++) {
            while ((stack.size() > 1) &&
                    orientation(underTheTop(stack), stack.peekLast(), copy[i]) != -1)
                stack.pollLast();
            stack.addLast(copy[i]);
        }
        int hullSize = stack.size();
        List<Point> hull = new ArrayList<>();
        for (int i = 0; i < hullSize; i++) {
            hull.add(stack.pollFirst());
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