package daa.algorithms;

import daa.metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    @Test
    @DisplayName("Correctness: Compare QuickSort with Arrays.sort on 150 random arrays")
    void testRandomArraysAgainstArraysSort() {
        Random rnd = new Random(42);
        for (int i = 0; i < 150; i++) {
            int size = rnd.nextInt(2000) + 1;
            int[] expected = new int[size];
            for (int j = 0; j < size; j++) {
                expected[j] = rnd.nextInt(100_000) - 50_000;
            }
            int[] actual = expected.clone();

            Arrays.sort(expected);
            Metrics metrics = QuickSort.sort(actual);

            assertArrayEquals(expected, actual);
            if (size > 1) {
                assertTrue(metrics.getMaxDepth() > 0);
            }
            assertTrue(metrics.getComparisons() > 0);
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
        int[] a = new int[]{42};
        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(new int[]{42}, a);
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getComparisons());
    }

    @Test
    @DisplayName("Edge Case: All elements equal (3-way partition test)")
    void testAllEqualElements() {
        int[] a = new int[1000];
        Arrays.fill(a, 99);
        int[] expected = a.clone();

        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(expected, a);
        assertEquals(0, metrics.getMaxDepth());
    }

    @Test
    @DisplayName("Edge Case: Already sorted array")
    void testAlreadySorted() {
        int[] a = new int[100];
        for (int i = 0; i < a.length; i++) {
            a[i] = i * 2;
        }
        int[] expected = a.clone();

        Metrics metrics = QuickSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    @DisplayName("Edge Case: Reverse sorted array")
    void testReverseSorted() {
        int[] a = new int[100];
        for (int i = 0; i < a.length; i++) {
            a[i] = 100 - i;
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

        Metrics metrics = QuickSort.sort(a);

        double maxAllowedDepth = 2.0 * (Math.log(n) / Math.log(2));
        System.out.println("Sorted array n=" + n + ": maxDepth=" + metrics.getMaxDepth() + ", maxAllowed=" + String.format(java.util.Locale.ROOT, "%.1f", maxAllowedDepth));

        assertTrue(metrics.getMaxDepth() <= maxAllowedDepth);
        assertTrue(metrics.getMaxDepth() <= (Math.log(n) / Math.log(2)) + 5);
    }
}
