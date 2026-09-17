package daa.bonus;

import daa.algorithms.QuickSelect;
import daa.metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BonusTasksTest {

    @Test
    @DisplayName("Bonus Task A: Deterministic Select (Median of Medians) correctness")
    void testMedianOfMediansCorrectness() {
        Random rnd = new Random(42);
        for (int i = 0; i < 100; i++) {
            int size = rnd.nextInt(500) + 1;
            int[] original = new int[size];
            for (int j = 0; j < size; j++) {
                original[j] = rnd.nextInt(20_000) - 10_000;
            }

            int[] sorted = original.clone();
            Arrays.sort(sorted);

            int k = rnd.nextInt(size);
            int result = MedianOfMediansSelect.select(original.clone(), k);

            assertEquals(sorted[k], result);
        }
    }

    @Test
    @DisplayName("Bonus Task A: Compare Median of Medians vs QuickSelect (Comparisons & Time)")
    void testCompareMedianOfMediansWithQuickSelect() {
        int n = 10_000;
        Random rnd = new Random(42);
        int[] arr1 = new int[n];
        for (int i = 0; i < n; i++) {
            arr1[i] = rnd.nextInt();
        }
        int[] arr2 = arr1.clone();

        int k = n / 2;

        Metrics mQuick = new Metrics();
        mQuick.startTimer();
        int valQuick = QuickSelect.select(arr1, k, mQuick);
        mQuick.stopTimer();

        Metrics mMom = new Metrics();
        int valMom = MedianOfMediansSelect.select(arr2, k, mMom);

        assertEquals(valQuick, valMom);

        System.out.println("Selection comparison for n=" + n + ":");
        System.out.printf(java.util.Locale.ROOT, "  QuickSelect: %.2f ms, %d comparisons%n", mQuick.timeMs(), mQuick.getComparisons());
        System.out.printf(java.util.Locale.ROOT, "  Median-of-Medians: %.2f ms, %d comparisons%n", mMom.timeMs(), mMom.getComparisons());
    }

    @Test
    @DisplayName("Bonus Task B: Closest Pair of Points O(n log n) matches O(n^2) brute force")
    void testClosestPairOfPoints() {
        Random rnd = new Random(42);
        for (int i = 0; i < 20; i++) {
            int n = rnd.nextInt(200) + 5;
            ClosestPairOfPoints.Point[] points = new ClosestPairOfPoints.Point[n];
            for (int j = 0; j < n; j++) {
                points[j] = new ClosestPairOfPoints.Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
            }

            double expected = ClosestPairOfPoints.bruteForce(points);
            double actual = ClosestPairOfPoints.findClosestDistance(points);

            assertEquals(expected, actual, 1e-9);
        }
    }
}
