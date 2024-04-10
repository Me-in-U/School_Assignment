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

public class tangentTest {

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

        isAAboveB = centroidA.y > centroidB.y ? 1 : centroidA.y < centroidB.y ? 0 : -1;
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

        if (isAAboveB == 1) {
            System.out.println("above");
            ia = findIndexWithMinY(polygon1Hull);
            ib = findIndexWithMaxY(polygon2Hull);
        } else if (isAAboveB == 0) {
            System.out.println("under");
            ia = findIndexWithMaxY(polygon1Hull);
            ib = findIndexWithMinY(polygon2Hull);
        } else {
            if (isALeftB) {
                ia = findIndexWithMaxX(polygon1Hull);
                ib = findIndexWithMinX(polygon2Hull);
            } else {
                ia = findIndexWithMinX(polygon1Hull);
                ib = findIndexWithMaxX(polygon2Hull);
            }
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
        int[] init = findInitialPoints(); // Ensure this method exists and returns the initial points indices for the
                                          // tangents
        int index_a = init[0];
        int index_b = init[1];
        boolean done = false;
        while (!done) {
            done = true;
            while (true) {
                int next_index_A = (index_a + (lower ? -1 : 1) + polygon1Hull.length) % polygon1Hull.length;
                int ori = Point.ccw(polygon2Hull[index_b], polygon1Hull[index_a], polygon1Hull[next_index_A]);
                if (ori == 0) {
                    // Collinear
                    break;
                } else if ((lower && (ori < 0)) || (!lower && (ori > 0))) {
                    index_a = next_index_A;
                    done = false;
                } else {
                    break;
                }
            }
            while (true) {
                int next_index_B = (index_b + (lower ? 1 : -1) + polygon2Hull.length) % polygon2Hull.length;
                int ori = Point.ccw(polygon1Hull[index_a], polygon2Hull[index_b], polygon2Hull[next_index_B]);
                if (ori == 0) {
                    // Collinear
                    break;
                } else if ((lower && (ori > 0)) || (!lower && (ori < 0))) {
                    index_b = next_index_B;
                    done = false;
                } else {
                    break;
                }
            }
            if (done) {
                System.out.println("done");
                break;
            }
        }
        return new Point[] { polygon1Hull[index_a], polygon2Hull[index_b] };
    }

    public static void mergePolygonsWithTangents() {
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
        return -1; // Not found
    }

    protected static Point[] polygon1;
    protected static Point[] polygon1Hull;

    protected static Point[] polygon2;
    protected static Point[] polygon2Hull;

    protected static Point[] mergedPolygon;

    protected static int isAAboveB;
    protected static boolean isALeftB;

    protected static Point[] upperTangent;
    protected static Point[] lowerTangent;

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

            determinePosition(polygon1, polygon2);
            // if (!isAAboveB) {
            // Point[] temp1 = new Point[polygon1Count];
            // for (int k = 0; k < polygon1Count; k++) {
            // temp1[k] = new Point(polygon1[k].x, polygon1[k].y);
            // }
            // polygon1 = polygon2;
            // polygon2 = temp1;
            // Point[] temp2 = new Point[polygon1Hull.length];
            // for (int k = 0; k < polygon1Hull.length; k++) {
            // temp2[k] = new Point(polygon1Hull[k].x, polygon1Hull[k].y);
            // }
            // polygon1Hull = polygon2Hull;
            // polygon2Hull = temp2;
            // determinePosition(polygon1, polygon2);
            // polygon1Count = polygon1.length;
            // polygon2Count = polygon2.length;
            // }

            upperTangent = findTangent(false);
            lowerTangent = findTangent(true);
            System.out.println("Tangent Upper : " + upperTangent[0] + ' ' + upperTangent[1]);
            System.out.println("Tangent Lower : " + lowerTangent[0] + ' ' + lowerTangent[1]);

            mergePolygonsWithTangents();
            System.out.println("poly 1 : " + Arrays.toString(polygon1));
            System.out.println("poly 2 : " + Arrays.toString(polygon2));
            System.out.println("Hull 1 : " + Arrays.toString(polygon1Hull));
            System.out.println("Hull 2 : " + Arrays.toString(polygon2Hull));
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

    public static Point[] convexHull(Point[] points) {
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
                    Point.ccw(Point.p0, copy[i], copy[i + 1]) == 0)
                i++;
            copy[resize++] = copy[i];
        }

        Deque<Point> stack = new ArrayDeque<>();
        stack.addLast(copy[0]);
        stack.addLast(copy[1]);
        stack.addLast(copy[2]);
        for (int i = 3; i < resize; i++) {
            while ((stack.size() > 1) &&
                    Point.ccw(underTheTop(stack), stack.peekLast(), copy[i]) != -1)
                stack.pollLast();
            stack.addLast(copy[i]);
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