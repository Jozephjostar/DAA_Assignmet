package com.daa.bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
        if (n <= 3) {
            return bruteForce(pointsByX, left, right);
        }

        int mid = left + (right - left) / 2;
        Point midPoint = pointsByX[mid];

        double deltaLeft = closestPairHelper(pointsByX, left, mid);
        double deltaRight = closestPairHelper(pointsByX, mid + 1, right);
        double delta = Math.min(deltaLeft, deltaRight);

        List<Point> strip = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (Math.abs(pointsByX[i].x - midPoint.x) < delta) {
                strip.add(pointsByX[i]);
            }
        }

        strip.sort(Comparator.comparingDouble(p -> p.y));

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
