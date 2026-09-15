package com.daa.sort;

import com.daa.metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    @Test
    @DisplayName("Correctness: Compare MergeSort with Arrays.sort on 150 random arrays")
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
            Metrics metrics = MergeSort.sort(actual);

            assertArrayEquals(expected, actual, "MergeSort output must match Arrays.sort for random array iteration " + i);
            if (size > MergeSort.INSERTION_SORT_CUTOFF) {
                assertTrue(metrics.getMaxDepth() > 0, "Recursion depth must be > 0 for size > 15");
            }
            assertTrue(metrics.getComparisons() > 0, "Comparisons must be tracked");
        }
    }

    @Test
    @DisplayName("Edge Case: Empty array")
    void testEmptyArray() {
        int[] a = new int[0];
        Metrics metrics = MergeSort.sort(a);
        assertArrayEquals(new int[0], a);
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getComparisons());
    }

    @Test
    @DisplayName("Edge Case: Single element array")
    void testSingleElement() {
        int[] a = new int[]{42};
        Metrics metrics = MergeSort.sort(a);
        assertArrayEquals(new int[]{42}, a);
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getComparisons());
    }

    @Test
    @DisplayName("Edge Case: All elements equal")
    void testAllEqualElements() {
        int[] a = new int[100];
        Arrays.fill(a, 7);
        int[] expected = a.clone();

        Metrics metrics = MergeSort.sort(a);
        assertArrayEquals(expected, a);
        assertTrue(metrics.getComparisons() > 0);
    }

    @Test
    @DisplayName("Edge Case: Already sorted array")
    void testAlreadySorted() {
        int[] a = new int[100];
        for (int i = 0; i < a.length; i++) {
            a[i] = i * 2;
        }
        int[] expected = a.clone();

        Metrics metrics = MergeSort.sort(a);
        assertArrayEquals(expected, a);
        assertTrue(metrics.getComparisons() > 0);
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

        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    @DisplayName("Subarray <= 15 sorted via Insertion Sort cutoff")
    void testInsertionSortCutoff() {
        int[] a = new int[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = a.clone();
        Arrays.sort(expected);

        Metrics metrics = MergeSort.sort(a);
        assertArrayEquals(expected, a);
        assertEquals(0, metrics.getMaxDepth(), "Cutoff <= 15 should not recurse further in mergeSort");
    }
}
