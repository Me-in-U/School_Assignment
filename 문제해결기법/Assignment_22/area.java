package 문제해결기법.Assignment_22;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.awt.geom.Point2D;

public class area {
    public static class Circle {
        private double x;
        private double y;
        private double radius;

        public Circle(double x, double y, double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public Point2D.Double[] findIntersections(Circle other) {
            double d = Math.hypot(other.x - this.x, other.y - this.y);
            double a = (this.radius * this.radius - other.radius * other.radius + d * d) / (2 * d);
            double h = Math.sqrt(this.radius * this.radius - a * a);
            double xm = this.x + a * (other.x - this.x) / d;
            double ym = this.y + a * (other.y - this.y) / d;
            double xs1 = xm + h * (other.y - this.y) / d;
            double ys1 = ym - h * (other.x - this.x) / d;
            double xs2 = xm - h * (other.y - this.y) / d;
            double ys2 = ym + h * (other.x - this.x) / d;

            return new Point2D.Double[] {
                    new Point2D.Double(xs1, ys1),
                    new Point2D.Double(xs2, ys2)
            };
        }

        public boolean contains(Point2D.Double point) {
            double dx = point.x - this.x;
            double dy = point.y - this.y;
            return (dx * dx + dy * dy) <= (this.radius * this.radius);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_22\\area.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_22\\2.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            Circle[] circles = new Circle[3];
            for (int i = 0; i < 3; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                double x = Double.parseDouble(st.nextToken());
                double y = Double.parseDouble(st.nextToken());
                double r = Double.parseDouble(st.nextToken());
                circles[i] = new Circle(x, y, r);
            }
            Point2D.Double[] points = new Point2D.Double[3];
            for (int i = 0; i < 3; i++) {
                int j = (i + 1) % 3;
                int k = (i + 2) % 3;
                Point2D.Double[] pts = circles[i].findIntersections(circles[j]);
                Point2D.Double validPoint = null;
                for (Point2D.Double pt : pts) {
                    if (circles[k].contains(pt)) {
                        validPoint = pt;
                        break;
                    }
                }
                points[i] = validPoint;
            }
            double area = triangleArea(points[0], points[1], points[2]);
            sb.append(String.format("%.2f", area)).append('\n');
            br.readLine();
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    public static double triangleArea(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        return Math.abs(p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y)) / 2.0;
    }
}