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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class tangentTest2 {

    public static class Point implements Comparable<Point> {
        double x;
        double y;
        static Point p0;

        Point() {
            x = 0;
            y = 0;
        }

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        static int ccw(Point a, Point b, Point c) {
            double res = (b.y - a.y) * (c.x - b.x) -
                    (c.y - b.y) * (b.x - a.x);

            if (res == 0)
                return 0;
            if (res > 0)
                return 1; // 시계
            return -1; // 반시계
        }

        public static void swap(Point p1, Point p2) {
            double tempX = p1.x;
            double tempY = p1.y;
            p1.x = p2.x;
            p1.y = p2.y;
            p2.x = tempX;
            p2.y = tempY;
        }

        public static double distSq(Point p1, Point p2) {
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
            double result = x;
            result = 31 * result + y;
            return (int) result;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static void makePath() {
        path = new Path2D.Double();
        // Move to the first point in the mergedPolygon
        path.moveTo(mergedPolygon[0].x, mergedPolygon[0].y);
        // Connect the points in the mergedPolygon
        for (int i = 1; i < mergedPolygon.length; i++) {
            path.lineTo(mergedPolygon[i].x, mergedPolygon[i].y);
        }
        // Close the path
        path.closePath();
    }

    public static boolean isBInsideMergedPolygon(Point[] b) {
        for (Point point : b) {
            if (!pointInPolygon(point)) {
                System.out.println("외부");
                return false;
            }
        }
        System.out.println("내부");
        return true;
    }

    // 다각형 A가 다각형 B 안에 있는지 확인하는 메서드
    // Checking if a point is inside a polygon
    public static boolean pointInPolygon(Point point) {
        // Create a Point2D object for the test point
        Point2D testPoint = new Point2D.Double(point.x, point.y);
        // First, check if the point is on the edge of the polygon
        if (isPointOnEdgeOfPolygon(point)) {
            // The point is on the edge of the polygon
            return true;
        }
        // Check if the test point is inside the polygon
        return path.contains(testPoint);
    }

    // Method to check if a point is on the edge of the polygon
    private static boolean isPointOnEdgeOfPolygon(Point point) {
        for (int i = 0; i < mergedPolygon.length; i++) {
            Point p1 = mergedPolygon[i];
            // Wrap around to the first point
            Point p2 = mergedPolygon[(i + 1) % mergedPolygon.length];

            if (isPointOnLineSegment(point, p1, p2)) {
                return true;
            }
        }
        return false;
    }

    // Method to check if a point is on a line segment
    private static boolean isPointOnLineSegment(Point p, Point p1, Point p2) {
        double minX = Math.min(p1.x, p2.x);
        double maxX = Math.max(p1.x, p2.x);
        double minY = Math.min(p1.y, p2.y);
        double maxY = Math.max(p1.y, p2.y);

        // Check if the point is within the bounds of the line segment
        if (p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY) {
            double dy = p2.y - p1.y;
            double dx = p2.x - p1.x;
            // To avoid division by zero in the slope calculation
            if (dx == 0) {
                return p.x == p1.x;
            }
            double slope = dy / dx;
            double intercept = p1.y - (slope * p1.x);
            // Check if the point is on the line
            return p.y == slope * p.x + intercept;
        }

        return false;
    }

    static double polygonArea(Point[] points) {
        double total = 0;
        for (int i = 0; i < points.length; i++) {
            int j = (i + 1) % points.length;
            total += (points[i].x * points[j].y)
                    - (points[j].x * points[i].y);
        }
        return total / 2;
    }

    public static Point[] deepCopyArray(Point[] original) {
        Point[] copy = new Point[original.length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new Point(original[i].x, original[i].y);
        }
        return copy;
    }

    public static void determinePosition(Point[] points1, Point[] points2) {
        double[] centroidA = calculateCentroid(points1);
        double[] centroidB = calculateCentroid(points2);
        System.out.println("center X : " + " " + centroidA[0] + " " + centroidB[0]);
        System.out.println("center Y : " + " " + centroidA[1] + " " + centroidB[1]);
        isAAboveB = centroidA[1] > centroidB[1] ? 1 : centroidA[1] < centroidB[1] ? 0 : -1;
        isALeftB = centroidA[0] < centroidB[0];
    }

    private static double[] calculateCentroid(Point[] points) {
        double sumX = 0;
        double sumY = 0;
        for (Point p : points) {
            sumX += p.x;
            sumY += p.y;
        }
        return new double[] { sumX / points.length, sumY / points.length };
    }

    public static int[] findInitialPoints() {
        int ia;
        int ib;

        if (isAAboveB == 1) {
            ia = findIndexWithMinY(polygon1Hull);
            ib = findIndexWithMaxY(polygon2Hull);
        } else if (isAAboveB == 0) {
            ia = findIndexWithMaxY(polygon1Hull);
            ib = findIndexWithMinY(polygon2Hull);
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
        int[] init = findInitialPoints(); // Ensure this method exists and returns the initial points indices for the
                                          // tangents
        int index_a = init[0];
        int index_b = init[1];
        boolean done = false;
        boolean findParallel_A = false;
        int parallelIndex_A = -1;
        boolean findParallel_B = false;
        int parallelIndex_B = -1;
        while (!done) {
            // Todo : 연속적으로 평행할 때 최초 점을 저장했다가 으로 선택
            done = true;
            while (true) {
                int next_index_A = (index_a + (lower ? -1 : 1) + polygon1Hull.length) % polygon1Hull.length;
                int ori = Point.ccw(polygon2Hull[index_b], polygon1Hull[index_a], polygon1Hull[next_index_A]);
                if ((lower && (ori <= 0)) || (!lower && (ori >= 0))) {
                    if (ori == 0 && !findParallel_A) {
                        parallelIndex_A = index_a;
                        findParallel_A = true;
                    } else {
                        findParallel_A = false;
                    }
                    index_a = next_index_A;
                    done = false;
                } else {
                    break;
                }
            }
            while (true) {
                int next_index_B = (index_b + (lower ? 1 : -1) + polygon2Hull.length) % polygon2Hull.length;
                int ori = Point.ccw(polygon1Hull[index_a], polygon2Hull[index_b], polygon2Hull[next_index_B]);
                if ((lower && (ori >= 0)) || (!lower && (ori <= 0))) {
                    if (ori == 0 && !findParallel_B) {
                        parallelIndex_B = index_b;
                        findParallel_B = true;
                    } else {
                        findParallel_B = false;
                    }
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
        if (findParallel_A) {
            index_a = parallelIndex_A;
        }
        if (findParallel_B) {
            index_b = parallelIndex_B;
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
            currentIndex = (currentIndex - 1 + polygon1.length) % polygon1.length;
        }
        // Add the lower tangent point from polygon A
        // mergedPolygonList.add(polygon1[lowerAIdx]);

        // Add points from polygon B between lower and upper tangents
        currentIndex = lowerBIdx;
        while (true) {
            mergedPolygonList.add(polygon2[currentIndex]);
            if (currentIndex == upperBIdx) {
                break;
            }
            currentIndex = (currentIndex - 1 + polygon2.length) % polygon2.length;
        }
        // Add the upper tangent point from polygon B to close the polygon
        // mergedPolygonList.add(polygon2[upperBIdx]);

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
    protected static Path2D path;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("tangent.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법/Assignment_12/3.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < T; i++) {
            polygon1 = new Point[Integer.parseInt(br.readLine().trim())];
            for (int j = 0; j < polygon1.length; j++) {
                st = new StringTokenizer(br.readLine().trim());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon1[j] = new Point(x, y);
            }
            polygon2 = new Point[Integer.parseInt(br.readLine().trim())];
            for (int j = 0; j < polygon2.length; j++) {
                st = new StringTokenizer(br.readLine().trim());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                polygon2[j] = new Point(x, y);
            }

            determinePosition(polygon1, polygon2);
            if (!isALeftB) {
                System.out.println("LR Changed");
                Point[] temp1 = deepCopyArray(polygon1);
                polygon1 = polygon2;
                polygon2 = temp1;
                determinePosition(polygon1, polygon2);
            }

            polygon1Hull = convexHull(polygon1);
            polygon2Hull = convexHull(polygon2);

            upperTangent = findTangent(false);
            lowerTangent = findTangent(true);

            System.out.println("Tangent Upper : " + upperTangent[0] + ' ' + upperTangent[1]);
            System.out.println("Tangent Lower : " + lowerTangent[0] + ' ' + lowerTangent[1]);

            mergePolygonsWithTangents();
            makePath();

            System.out.println("poly 1 : " + Arrays.toString(polygon1));
            System.out.println("poly 2 : " + Arrays.toString(polygon2));
            System.out.println("Hull 1 : " + Arrays.toString(polygon1Hull));
            System.out.println("Hull 2 : " + Arrays.toString(polygon2Hull));
            System.out.println("merged_polygon : " + Arrays.toString(mergedPolygon));
            System.out.println();
            System.out.println("Area m : " + String.format("%.1f", polygonArea(mergedPolygon)));
            System.out.println("Area a : " + String.format("%.1f", polygonArea(polygon1)));
            System.out.println("Area b : " + String.format("%.1f", polygonArea(polygon2)));
            double totalArea = polygonArea(mergedPolygon);
            if (isBInsideMergedPolygon(polygon1)) {
                System.out.println("polygon1 included");
                totalArea -= polygonArea(polygon1);
            }
            if (isBInsideMergedPolygon(polygon2)) {
                System.out.println("polygon2 included");
                totalArea -= polygonArea(polygon2);
            }
            sb.append(String.format("%.1f", totalArea * 10)).append('\n');
        }
        System.out.print(sb.toString());
        bw.write(sb.toString().trim());
        bw.close();
        br.close();
    }

    public static Point[] convexHull(Point[] points) {
        Point[] copy = deepCopyArray(points);
        double minX = copy[0].x;
        double minY = copy[0].y;
        int minIndex = 0;
        int n = copy.length;
        for (int i = 1; i < n; i++) {
            double x = copy[i].x;
            double y = copy[i].y;
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