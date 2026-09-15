package com.daa.sort;

import com.daa.metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    @Test
    @DisplayName("Correctness: Compare QuickSort with Arrays.sort on 150 random arrays")
    void testRandomArraysAgainstArraysSort() {
        Random rnd = new Random(12345);
        for (int i = 0; i < 150; i++) {
            int size = rnd.nextInt(2000) + 1;
            int[] expected = new int[size];
            for (int j = 0; j < size; j++) {
                expected[j] = rnd.nextInt(100_000) - 50_000;
            }
            int[] actual = expected.clone();

            Arrays.sort(expected);
            Metrics metrics = QuickSort.sort(actual);

            assertArrayEquals(expected, actual, "QuickSort result must match Arrays.sort on iteration " + i);
            assertTrue(metrics.getComparisons() > 0, "Comparisons must be tracked");
        }
    }

    @Test
    @DisplayName("Edge Case: Empty array")
    void testEmptyArray() {
        int[] a = new int[0];
        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(new int[0], a);
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getComparisons());
    }

    @Test
    @DisplayName("Edge Case: Single element array")
    void testSingleElement() {
        int[] a = new int[]{99};
        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(new int[]{99}, a);
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getComparisons());
    }

    @Test
    @DisplayName("Edge Case: All elements equal (3-way partition test)")
    void testAllEqualElements() {
        int[] a = new int[1000];
        Arrays.fill(a, 42);
        int[] expected = a.clone();

        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(expected, a);
        // With 3-way partition, all equal elements should be partitioned in one pass!
        assertTrue(metrics.getMaxDepth() <= 2, "3-way partition should handle all-equal elements with depth <= 2");
    }

    @Test
    @DisplayName("Edge Case: Already sorted array")
    void testAlreadySorted() {
        int[] a = new int[1000];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        int[] expected = a.clone();

        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(expected, a);
        assertTrue(metrics.getComparisons() > 0);
    }

    @Test
    @DisplayName("Edge Case: Reverse sorted array")
    void testReverseSorted() {
        int[] a = new int[1000];
        for (int i = 0; i < a.length; i++) {
            a[i] = 1000 - i;
        }
        int[] expected = a.clone();
        Arrays.sort(expected);

        QuickSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    @DisplayName("Depth Check: QuickSort on a sorted array of 100,000 elements must have maxDepth <= 2 * log2(n)")
    void testRecursionDepthOnLargeSortedArray() {
        int n = 100_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }

        Metrics metrics = new Metrics();
        QuickSort.sort(a, metrics);

        double maxAllowedDepth = 2.0 * (Math.log(n) / Math.log(2.0));
        System.out.printf("Sorted array n=%d: maxDepth=%d, maxAllowed=%.1f\n", n, metrics.getMaxDepth(), maxAllowedDepth);

        assertTrue(metrics.getMaxDepth() <= maxAllowedDepth,
                String.format("maxDepth (%d) exceeded 2*log2(n) (%.2f)", metrics.getMaxDepth(), maxAllowedDepth));

        // Also check that it is sorted
        for (int i = 0; i < n - 1; i++) {
            assertTrue(a[i] <= a[i + 1], "Array must remain correctly sorted");
        }
    }
}
