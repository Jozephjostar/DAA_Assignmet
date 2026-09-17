package daa.algorithms;

import daa.metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSelectTest {

    @Test
    @DisplayName("QuickSelect: The result must equal sorted[k] on at least 100 random arrays")
    void testQuickSelectOnRandomArrays() {
        Random rnd = new Random(42);
        for (int i = 0; i < 150; i++) {
            int size = rnd.nextInt(1000) + 1;
            int[] original = new int[size];
            for (int j = 0; j < size; j++) {
                original[j] = rnd.nextInt(100_000) - 50_000;
            }

            int[] sorted = original.clone();
            Arrays.sort(sorted);

            int k = rnd.nextInt(size);
            int[] testArray = original.clone();

            Metrics metrics = new Metrics();
            int result = QuickSelect.select(testArray, k, metrics);

            assertEquals(sorted[k], result);
        }
    }

    @Test
    @DisplayName("Edge Case: k = 0 (minimum element)")
    void testMinimumElement() {
        int[] a = {5, 2, 8, 1, 9, 3};
        assertEquals(1, QuickSelect.select(a, 0));
    }

    @Test
    @DisplayName("Edge Case: k = n - 1 (maximum element)")
    void testMaximumElement() {
        int[] a = {5, 2, 8, 1, 9, 3};
        assertEquals(9, QuickSelect.select(a, a.length - 1));
    }

    @Test
    @DisplayName("Edge Case: Single element array")
    void testSingleElement() {
        int[] a = {42};
        assertEquals(42, QuickSelect.select(a, 0));
    }

    @Test
    @DisplayName("Edge Case: All elements equal")
    void testAllEqualElements() {
        int[] a = {7, 7, 7, 7, 7};
        assertEquals(7, QuickSelect.select(a, 2));
    }

    @Test
    @DisplayName("Invalid Input: Empty array throws IllegalArgumentException")
    void testEmptyArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(new int[0], 0));
    }

    @Test
    @DisplayName("Invalid Input: Null array throws IllegalArgumentException")
    void testNullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(null, 0));
    }

    @Test
    @DisplayName("Invalid Input: k out of range (negative or >= length)")
    void testKOutOfRangeThrows() {
        int[] a = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(a, -1));
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(a, 3));
    }
}
