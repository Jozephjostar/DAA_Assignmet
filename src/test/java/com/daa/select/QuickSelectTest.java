package com.daa.select;

import com.daa.metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSelectTest {

    @Test
    @DisplayName("QuickSelect: The result must equal sorted[k] on at least 100 random arrays")
    void testQuickSelectOnRandomArrays() {
        Random rnd = new Random(54321);
        for (int i = 0; i < 150; i++) {
            int n = rnd.nextInt(1000) + 1;
            int[] original = new int[n];
            for (int j = 0; j < n; j++) {
                original[j] = rnd.nextInt(50_000) - 25_000;
            }

            int[] sorted = original.clone();
            Arrays.sort(sorted);

            int k = rnd.nextInt(n);
            int[] workArray = original.clone();

            Metrics metrics = new Metrics();
            int selected = QuickSelect.select(workArray, k, metrics);

            assertEquals(sorted[k], selected,
                    String.format("Iteration %d: k-th (%d) smallest element mismatch!", i, k));
            if (n > 1) {
                assertTrue(metrics.getComparisons() > 0, "Comparisons must be tracked");
            }
        }
    }

    @Test
    @DisplayName("Edge Case: Minimum (k=0), Maximum (k=n-1), and Median (k=n/2)")
    void testBoundaryRanks() {
        int[] original = {42, 17, 99, -5, 0, 1000, 23, 7, 7, 7, -12};
        int[] sorted = original.clone();
        Arrays.sort(sorted);

        assertEquals(sorted[0], QuickSelect.select(original.clone(), 0));
        assertEquals(sorted[sorted.length - 1], QuickSelect.select(original.clone(), sorted.length - 1));
        int mid = sorted.length / 2;
        assertEquals(sorted[mid], QuickSelect.select(original.clone(), mid));
    }

    @Test
    @DisplayName("Edge Case: Single element array")
    void testSingleElement() {
        int[] a = {88};
        assertEquals(88, QuickSelect.select(a, 0));
    }

    @Test
    @DisplayName("Edge Case: All elements equal")
    void testAllEqualElements() {
        int[] a = new int[50];
        Arrays.fill(a, 33);
        assertEquals(33, QuickSelect.select(a, 0));
        assertEquals(33, QuickSelect.select(a, 25));
        assertEquals(33, QuickSelect.select(a, 49));
    }

    @Test
    @DisplayName("Invalid Input: Empty array throws IllegalArgumentException")
    void testEmptyArrayThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                QuickSelect.select(new int[0], 0));
        assertTrue(ex.getMessage().contains("null or empty"));
    }

    @Test
    @DisplayName("Invalid Input: Null array throws IllegalArgumentException")
    void testNullArrayThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                QuickSelect.select(null, 0));
        assertTrue(ex.getMessage().contains("null or empty"));
    }

    @Test
    @DisplayName("Invalid Input: Negative k throws IllegalArgumentException")
    void testNegativeKThrowsException() {
        int[] a = {1, 2, 3};
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                QuickSelect.select(a, -1));
        assertTrue(ex.getMessage().contains("Invalid rank"));
    }

    @Test
    @DisplayName("Invalid Input: k >= n throws IllegalArgumentException")
    void testKOutOfBoundsThrowsException() {
        int[] a = {10, 20, 30};
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                QuickSelect.select(a, 3));
        assertTrue(ex.getMessage().contains("Invalid rank"));
    }
}
