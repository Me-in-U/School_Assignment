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

        public static int distSq(Point p1, Point p2) {
            return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        }

        public int compareTo(Point p) {
            int o = ccw(p0, this, p);
            if (o == 0)
                return (distSq(p0, p) >= distSq(p0, this)) ? -1 : 1;
            return o;
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

    public static void determinePosition(Point[] points1, Point[] points2) {
        Point centroidA = calculateCentroid(points1);
        Point centroidB = calculateCentroid(points2);

        isAAboveB = centroidA.y > centroidB.y;
        isALeftB = centroidA.x < centroidB.x;
    }

    private static Point calculateCentroid(Point[] points) {
        double sumX = 0;
        double sumY = 0;
        for (Point p : points) {
            sumX += p.x;
            sumY += p.y;
        }
        return new Point((int) (sumX / points.length), (int) (sumY / points.length));
    }

    public static int[] findInitialPoints() {
        int ia;
        int ib;

        if (isAAboveB) {
            ia = findIndexWithMinY(polygon1Hull);
            ib = findIndexWithMaxY(polygon2Hull);
        } else {
            ia = findIndexWithMaxX(polygon1Hull);
            ib = findIndexWithMinX(polygon2Hull);
        }

        return new int[] { ia, ib };
    }

    private static int findIndexWithMinY(Point[] points) {
        int index = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i].y < points[index].y) {
                index = i;
            }
        }
        return index;
    }

    private static int findIndexWithMaxY(Point[] points) {
        int index = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i].y > points[index].y) {
                index = i;
            }
        }
        return index;
    }

    private static int findIndexWithMaxX(Point[] points) {
        int index = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i].x > points[index].x) {
                index = i;
            }
        }
        return index;
    }

    private static int findIndexWithMinX(Point[] points) {
        int index = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i].x < points[index].x) {
                index = i;
            }
        }
        return index;
    }

    public static Point[] findTangent(boolean lower) {
        int[] init = findInitialPoints();
        int index_a = init[0];
        int index_b = init[1];
        boolean done = false;
        while (!done) {
            done = true;
            while (true) {
                int next_index_A = lower ? index_a + 1 : index_a - 1;
                if (next_index_A < 0) {
                    next_index_A = polygon1HullCount - 1;
                }
                if (polygon1HullCount <= next_index_A) {
                    next_index_A = 0;
                }
                int ori = Point.ccw(polygon2Hull[index_b], polygon1Hull[index_a], polygon1Hull[next_index_A]);
                if (ori == 0) {
                    break;
                } else if ((lower && (ori < 0)) || (!lower && (ori > 0))) {
                    index_a = next_index_A;
                    done = false;
                } else {
                    break;
                }
            }
            while (true) {
                int next_index_B = lower ? index_b + 1 : index_b - 1;
                if (next_index_B < 0) {
                    next_index_B = polygon2HullCount - 1;
                }
                if (polygon1HullCount <= next_index_B) {
                    next_index_B = 0;
                }
                int ori = Point.ccw(polygon1Hull[index_a], polygon2Hull[index_b], polygon2Hull[next_index_B]);
                if (ori == 0) {
                    break;
                } else if ((lower && (ori > 0)) || (!lower && (ori < 0))) {
                    index_b = next_index_B;
                    done = false;
                } else {
                    break;
                }
            }
            if (done) {
                break;
            }
        }
        return new Point[] { polygon1Hull[index_a], polygon2Hull[index_b] };
    }

    public static void mergePolygonsWithTangents() {
        // Find the start and end indices for the tangents on each polygon
        int upperAIdx = findIndex(polygon1, upperTangent[0]); // Upper tangent index on A
        int upperBIdx = findIndex(polygon2, upperTangent[1]); // Upper tangent index on B

        int lowerAIdx = findIndex(polygon1, lowerTangent[0]); // Lower tangent index on A
        int lowerBIdx = findIndex(polygon2, lowerTangent[1]); // Lower tangent index on B

        List<Point> mergedPolygonList = new ArrayList<>();

        // Add points from polygon A between upper and lower tangents
        int currentIdx = upperAIdx;
        while (true) {
            mergedPolygonList.add(polygon1[currentIdx]);
            if (currentIdx == lowerAIdx) {
                break;
            }
            currentIdx = (currentIdx + 1) % polygon1Count;
        }

        // Add points from polygon B between lower and upper tangents
        currentIdx = lowerBIdx;
        while (true) {
            mergedPolygonList.add(polygon2[currentIdx]);
            if (currentIdx == upperBIdx) {
                break;
            }
            currentIdx = (currentIdx + 1) % polygon2Count;
        }

        // Return to the starting point to close the polygon
        mergedPolygonList.add(upperTangent[0]);

        // Convert the List<Point> to Point[]
        mergedPolygon = new Point[mergedPolygonList.size()];
        mergedPolygon = mergedPolygonList.toArray(mergedPolygon);
    }

    // Helper method to find the index of a point in an array
    private static int findIndex(Point[] points, Point target) {
        for (int i = 0; i < points.length; i++) {
            if (points[i].equals(target)) {
                return i;
            }
        }
        return -1; // Not found (should not happen if target is guaranteed to be in points)
    }

    protected static Point[] polygon1;
    protected static Point[] polygon1Hull;
    protected static int polygon1Count;
    protected static int polygon1HullCount;

    protected static Point[] polygon2;
    protected static Point[] polygon2Hull;
    protected static int polygon2Count;
    protected static int polygon2HullCount;

    protected static Point[] mergedPolygon;

    protected static boolean isAAboveB;
    protected static boolean isALeftB;

    protected static Point[] upperTangent;
    protected static Point[] lowerTangent;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("tangent.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법/Assignment_12/1.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < T; i++) {
            polygon1Count = Integer.parseInt(br.readLine().trim());
            polygon1 = new Point[polygon1Count];
            for (int j = 0; j < polygon1Count; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon1[j] = new Point(x, y);
            }

            polygon2Count = Integer.parseInt(br.readLine().trim());
            polygon2 = new Point[polygon2Count];
            for (int j = 0; j < polygon2Count; j++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon2[j] = new Point(x, y);
            }

            polygon1Hull = convexHull(polygon1);
            polygon1HullCount = polygon1Hull.length;
            polygon2Hull = convexHull(polygon2);
            polygon2HullCount = polygon2Hull.length;
            System.out.println("Hull 1 : " + Arrays.toString(polygon1Hull));
            System.out.println("Hull 2 : " + Arrays.toString(polygon2Hull));

            determinePosition(polygon1, polygon2);
            if (!isAAboveB) {
                Point[] temp = polygon1;
                polygon1 = polygon2;
                polygon2 = temp;
                Point[] temp2 = polygon1Hull;
                polygon1Hull = polygon2Hull;
                polygon2Hull = temp2;
                determinePosition(polygon1, polygon2);
            }
            if (!isAAboveB && !isALeftB) {
                Point[] temp = polygon1;
                polygon1 = polygon2;
                polygon2 = temp;
                Point[] temp2 = polygon1Hull;
                polygon1Hull = polygon2Hull;
                polygon2Hull = temp2;
                determinePosition(polygon1, polygon2);
            }
            upperTangent = findTangent(false);
            lowerTangent = findTangent(true);
            System.out.println("Tangent1 : " + upperTangent[0] + ' ' + upperTangent[1]);
            System.out.println("Tangent1 : " + lowerTangent[0] + ' ' + lowerTangent[1]);
            mergePolygonsWithTangents();
            System.out.println("merged : " + Arrays.toString(mergedPolygon));
            sb.append(polygonArea(mergedPolygon, mergedPolygon.length) - polygonArea(polygon1, polygon1.length)
                    - polygonArea(polygon2, polygon2.length)).append('\n');
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
                    Point.ccw(underTheTop(stack), stack.peekLast(), points[i]) != -1)
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