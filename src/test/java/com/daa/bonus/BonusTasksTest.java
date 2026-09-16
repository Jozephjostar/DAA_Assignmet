package com.daa.bonus;

import com.daa.metrics.Metrics;
import com.daa.select.QuickSelect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BonusTasksTest {

    @Test
    @DisplayName("Bonus Task A: Median of Medians correctness vs Arrays.sort on 100 random arrays")
    void testMedianOfMediansCorrectness() {
        Random rnd = new Random(777);
        for (int i = 0; i < 100; i++) {
            int n = rnd.nextInt(500) + 1;
            int[] original = new int[n];
            for (int j = 0; j < n; j++) {
                original[j] = rnd.nextInt(10_000) - 5000;
            }

            int[] sorted = original.clone();
            Arrays.sort(sorted);

            int k = rnd.nextInt(n);
            int selected = MedianOfMediansSelect.select(original.clone(), k);

            assertEquals(sorted[k], selected,
                    String.format("Mismatch in Median of Medians at iteration %d for k=%d", i, k));
        }
    }

    @Test
    @DisplayName("Bonus Task A: Compare Median of Medians vs QuickSelect (Comparisons & Time)")
    void testCompareMedianOfMediansWithQuickSelect() {
        int n = 10_000;
        Random rnd = new Random(999);
        int[] arr1 = new int[n];
        for (int i = 0; i < n; i++) {
            arr1[i] = rnd.nextInt(100_000);
        }
        int[] arr2 = arr1.clone();
        int k = n / 2;

        Metrics mQuick = new Metrics();
        int resQuick = QuickSelect.select(arr1, k, mQuick);

        Metrics mMoM = new Metrics();
        int resMoM = MedianOfMediansSelect.select(arr2, k, mMoM);

        assertEquals(resQuick, resMoM, "Both selection methods must return identical median");
        System.out.printf("Selection comparison for n=%d:\n  QuickSelect: %.2f ms, %d comparisons\n  Median-of-Medians: %.2f ms, %d comparisons\n",
                n, mQuick.getTimeMs(), mQuick.getComparisons(), mMoM.getTimeMs(), mMoM.getComparisons());
    }

    @Test
    @DisplayName("Bonus Task B: Closest Pair O(n log n) vs Brute-Force O(n^2) for n <= 2000")
    void testClosestPairAgainstBruteForce() {
        Random rnd = new Random(123);
        int[] testSizes = {2, 3, 4, 10, 50, 200, 500, 1000, 2000};

        for (int n : testSizes) {
            ClosestPairOfPoints.Point[] points = new ClosestPairOfPoints.Point[n];
            for (int i = 0; i < n; i++) {
                double x = rnd.nextDouble() * 10_000 - 5000;
                double y = rnd.nextDouble() * 10_000 - 5000;
                points[i] = new ClosestPairOfPoints.Point(x, y);
            }

            double fastDistance = ClosestPairOfPoints.findClosestDistance(points);
            double bruteDistance = ClosestPairOfPoints.bruteForce(points);

            assertEquals(bruteDistance, fastDistance, 1e-6,
                    String.format("Closest pair distance mismatch for n=%d: fast=%.6f, brute=%.6f",
                            n, fastDistance, bruteDistance));
        }
    }

    @Test
    @DisplayName("Bonus Task B: Edge cases (identical coordinates, collinear points)")
    void testClosestPairEdgeCases() {
        ClosestPairOfPoints.Point[] twoPoints = {
                new ClosestPairOfPoints.Point(0, 0),
                new ClosestPairOfPoints.Point(3, 4)
        };
        assertEquals(5.0, ClosestPairOfPoints.findClosestDistance(twoPoints), 1e-9);

        ClosestPairOfPoints.Point[] threePoints = {
                new ClosestPairOfPoints.Point(0, 0),
                new ClosestPairOfPoints.Point(1, 1),
                new ClosestPairOfPoints.Point(10, 10)
        };
        assertEquals(Math.sqrt(2.0), ClosestPairOfPoints.findClosestDistance(threePoints), 1e-9);

        ClosestPairOfPoints.Point[] collinear = {
                new ClosestPairOfPoints.Point(1, 5),
                new ClosestPairOfPoints.Point(4, 5),
                new ClosestPairOfPoints.Point(10, 5),
                new ClosestPairOfPoints.Point(12, 5)
        };
        assertEquals(2.0, ClosestPairOfPoints.findClosestDistance(collinear), 1e-9);
    }
}
