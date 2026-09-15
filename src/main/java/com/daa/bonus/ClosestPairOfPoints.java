package com.daa.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Task B Bonus (+5%): Closest Pair of Points in 2D
 * Divide-and-Conquer algorithm with O(n log n) running time:
 * 1. Sort points by X coordinate.
 * 2. Divide points into two halves at the median line.
 * 3. Find closest pair recursively in each half: delta = min(delta_left, delta_right).
 * 4. Build a vertical strip of width 2 * delta around the median line, sorted by Y.
 * 5. In the strip, check only the next 7 points for each point.
 * 6. Return the global minimum Euclidean distance.
 */
public class ClosestPairOfPoints {

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    /**
     * Finds the minimum distance between any pair of points in O(n log n).
     */
    public static double findClosestDistance(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points are required.");
        }

        Point[] pointsSortedByX = points.clone();
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));

        return closestPairHelper(pointsSortedByX, 0, pointsSortedByX.length - 1);
    }

    private static double closestPairHelper(Point[] pointsByX, int left, int right) {
        int n = right - left + 1;
        // Base case: for small sizes, use brute-force
        if (n <= 3) {
            return bruteForce(pointsByX, left, right);
        }

        int mid = left + (right - left) / 2;
        Point midPoint = pointsByX[mid];

        double deltaLeft = closestPairHelper(pointsByX, left, mid);
        double deltaRight = closestPairHelper(pointsByX, mid + 1, right);
        double delta = Math.min(deltaLeft, deltaRight);

        // Build strip of width 2 * delta around median X coordinate
        List<Point> strip = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (Math.abs(pointsByX[i].x - midPoint.x) < delta) {
                strip.add(pointsByX[i]);
            }
        }

        // Sort strip points by Y coordinate
        strip.sort(Comparator.comparingDouble(p -> p.y));

        // Geometric property: only need to inspect next 7 points in the strip
        int stripSize = strip.size();
        for (int i = 0; i < stripSize; i++) {
            Point p1 = strip.get(i);
            for (int j = i + 1; j < stripSize && (j - i) <= 7; j++) {
                Point p2 = strip.get(j);
                if ((p2.y - p1.y) >= delta) {
                    break;
                }
                double dist = p1.distanceTo(p2);
                if (dist < delta) {
                    delta = dist;
                }
            }
        }

        return delta;
    }

    /**
     * Brute force O(n^2) distance calculation for verification and base cases.
     */
    public static double bruteForce(Point[] points, int left, int right) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double d = points[i].distanceTo(points[j]);
                if (d < minDistance) {
                    minDistance = d;
                }
            }
        }
        return minDistance;
    }

    public static double bruteForce(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points are required.");
        }
        return bruteForce(points, 0, points.length - 1);
    }
}
