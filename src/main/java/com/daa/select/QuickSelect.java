package com.daa.select;

import com.daa.metrics.Metrics;
import com.daa.sort.QuickSort;

import java.util.Random;

/**
 * QuickSelect implementation to find the k-th smallest element (0-indexed):
 * 1. Reuse Partition: Reuses QuickSort.partition3Way.
 * 2. One Side Only: Continues search only in the partition containing index k.
 * 3. Invalid Input: Validates bounds and throws descriptive IllegalArgumentException.
 */
public class QuickSelect {

    /**
     * Selects the k-th smallest element with fresh metrics.
     *
     * @param a the input array
     * @param k 0-based rank
     * @return the k-th smallest element
     */
    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    /**
     * Selects the k-th smallest element, recording performance metrics.
     *
     * @param a       the input array
     * @param k       0-based rank
     * @param metrics metrics object to record comparisons, depth, and time
     * @return the k-th smallest element
     * @throws IllegalArgumentException if the array is null/empty or k is out of bounds
     */
    public static int select(int[] a, int k, Metrics metrics) {
        // Invalid Input validation
        if (a == null || a.length == 0) {
            throw new IllegalArgumentException("Input array must not be null or empty.");
        }
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid rank k=%d: index must be in range [0, %d] for array of length %d.",
                    k, a.length - 1, a.length));
        }

        Random rnd = new Random();
        metrics.startTimer();

        int left = 0;
        int right = a.length - 1;

        while (left <= right) {
            if (left == right) {
                metrics.stopTimer();
                return a[left];
            }

            metrics.enterRecursion();
            // Reuse Partition from QuickSort
            int[] bounds = QuickSort.partition3Way(a, left, right, metrics, rnd);
            int lt = bounds[0];
            int gt = bounds[1];
            metrics.exitRecursion();

            // One Side Only: continue only into the section containing k
            if (k >= lt && k <= gt) {
                metrics.stopTimer();
                return a[k]; // Equal elements range contains k
            } else if (k < lt) {
                right = lt - 1;
            } else {
                left = gt + 1;
            }
        }

        metrics.stopTimer();
        return a[left];
    }
}
