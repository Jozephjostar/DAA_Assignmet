package daa.algorithms;

import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class InsertionSortTest {

    @Test
    void testInsertionSort() {
        Random rnd = new Random(42);
        for (int i = 0; i < 50; i++) {
            int[] a = new int[50];
            for (int j = 0; j < a.length; j++) {
                a[j] = rnd.nextInt(1000);
            }
            int[] expected = a.clone();
            Arrays.sort(expected);
            new InsertionSort().sort(a, new Metrics());
            assertArrayEquals(expected, a);
        }
    }
}
