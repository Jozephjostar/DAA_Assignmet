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
    @DisplayName("Bonus Task A: Compare Median of Medians vs QuickSelect on Random and Sorted")
    void testCompareMedianOfMediansWithQuickSelect() {
        int n = 10_000;
        int k = n / 2;

        Random rnd = new Random(42);
        int[] rndArr1 = new int[n];
        for (int i = 0; i < n; i++) {
            rndArr1[i] = rnd.nextInt();
        }
        int[] rndArr2 = rndArr1.clone();

        Metrics mQuickRnd = new Metrics();
        mQuickRnd.startTimer();
        int valQuickRnd = QuickSelect.select(rndArr1, k, mQuickRnd);
        mQuickRnd.stopTimer();

        Metrics mMomRnd = new Metrics();
        int valMomRnd = MedianOfMediansSelect.select(rndArr2, k, mMomRnd);

        assertEquals(valQuickRnd, valMomRnd);

        int[] sortedArr1 = new int[n];
        for (int i = 0; i < n; i++) {
            sortedArr1[i] = i;
        }
        int[] sortedArr2 = sortedArr1.clone();

        Metrics mQuickSorted = new Metrics();
        mQuickSorted.startTimer();
        int valQuickSorted = QuickSelect.select(sortedArr1, k, mQuickSorted);
        mQuickSorted.stopTimer();

        Metrics mMomSorted = new Metrics();
        int valMomSorted = MedianOfMediansSelect.select(sortedArr2, k, mMomSorted);

        assertEquals(valQuickSorted, valMomSorted);

        System.out.println("Selection comparison for n=" + n + " (Random):");
        System.out.printf(java.util.Locale.ROOT, "  QuickSelect: %.2f ms, %d comparisons%n", mQuickRnd.timeMs(), mQuickRnd.getComparisons());
        System.out.printf(java.util.Locale.ROOT, "  Median-of-Medians: %.2f ms, %d comparisons%n", mMomRnd.timeMs(), mMomRnd.getComparisons());

        System.out.println("Selection comparison for n=" + n + " (Sorted):");
        System.out.printf(java.util.Locale.ROOT, "  QuickSelect: %.2f ms, %d comparisons%n", mQuickSorted.timeMs(), mQuickSorted.getComparisons());
        System.out.printf(java.util.Locale.ROOT, "  Median-of-Medians: %.2f ms, %d comparisons%n", mMomSorted.timeMs(), mMomSorted.getComparisons());
    }

    @Test
    @DisplayName("Bonus Task B: Closest Pair of Points O(n log n) matches O(n^2) brute force for n <= 2000")
    void testClosestPairOfPoints() {
        Random rnd = new Random(42);
        int[] testSizes = {10, 50, 200, 500, 2000};
        for (int n : testSizes) {
            ClosestPairOfPoints.Point[] points = new ClosestPairOfPoints.Point[n];
            for (int j = 0; j < n; j++) {
                points[j] = new ClosestPairOfPoints.Point(rnd.nextDouble() * 10_000, rnd.nextDouble() * 10_000);
            }

            double expected = ClosestPairOfPoints.bruteForce(points);
            double actual = ClosestPairOfPoints.findClosestDistance(points);

            assertEquals(expected, actual, 1e-7);
        }
    }
}
