package daa.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPairOfPoints {

    public static class Point {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point p) {
            double dx = this.x - p.x, dy = this.y - p.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public static double findClosestDistance(Point[] points) {
        if (points == null || points.length < 2) throw new IllegalArgumentException("Need at least 2 points");
        Point[] sorted = points.clone();
        Arrays.sort(sorted, Comparator.comparingDouble(p -> p.x));
        return findClosest(sorted, 0, sorted.length - 1);
    }

    private static double findClosest(Point[] pts, int l, int r) {
        if (r - l + 1 <= 3) return bruteForce(pts, l, r);

        int m = (l + r) / 2;
        Point midPoint = pts[m];

        double d = Math.min(findClosest(pts, l, m), findClosest(pts, m + 1, r));

        List<Point> strip = new ArrayList<>();
        for (int i = l; i <= r; i++) {
            if (Math.abs(pts[i].x - midPoint.x) < d) {
                strip.add(pts[i]);
            }
        }
        strip.sort(Comparator.comparingDouble(p -> p.y));

        for (int i = 0; i < strip.size(); i++) {
            Point p1 = strip.get(i);
            for (int j = i + 1; j < strip.size() && (j - i) <= 7; j++) {
                Point p2 = strip.get(j);
                if (p2.y - p1.y >= d) break;
                d = Math.min(d, p1.distanceTo(p2));
            }
        }
        return d;
    }

    public static double bruteForce(Point[] pts, int l, int r) {
        double minD = Double.POSITIVE_INFINITY;
        for (int i = l; i <= r; i++) {
            for (int j = i + 1; j <= r; j++) {
                minD = Math.min(minD, pts[i].distanceTo(pts[j]));
            }
        }
        return minD;
    }

    public static double bruteForce(Point[] pts) {
        if (pts == null || pts.length < 2) throw new IllegalArgumentException("Need at least 2 points");
        return bruteForce(pts, 0, pts.length - 1);
    }
}
