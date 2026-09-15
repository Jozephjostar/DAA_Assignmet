package com.daa.bonus;

import com.daa.metrics.Metrics;
import com.daa.sort.MergeSort;

/**
 * Task A Bonus (+10%): Deterministic Select (Median of Medians)
 * Guaranteed O(n) worst-case time complexity:
 * 1. Split array into groups of 5.
 * 2. Find median of each group with Insertion Sort.
 * 3. Recursively find the median of these medians and use it as the pivot.
 * 4. Partition around this pivot and continue on the side containing k.
 */
public class MedianOfMediansSelect {

    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid rank k=%d: must be in [0, %d].", k, a.length - 1));
        }

        metrics.startTimer();
        int result = selectHelper(a, 0, a.length - 1, k, metrics);
        metrics.stopTimer();
        return result;
    }

    private static int selectHelper(int[] a, int left, int right, int k, Metrics metrics) {
        while (left <= right) {
            if (left == right) {
                return a[left];
            }

            metrics.enterRecursion();
            int pivotValue = getMedianOfMedians(a, left, right, metrics);
            int[] bounds = partitionAroundValue(a, left, right, pivotValue, metrics);
            metrics.exitRecursion();

            int lt = bounds[0];
            int gt = bounds[1];

            if (k >= lt && k <= gt) {
                return a[k];
            } else if (k < lt) {
                right = lt - 1;
            } else {
                left = gt + 1;
            }
        }
        return a[left];
    }

    /**
     * Divides array into groups of 5, sorts each group using insertion sort,
     * and recursively selects the median of medians.
     */
    private static int getMedianOfMedians(int[] a, int left, int right, Metrics metrics) {
        int n = right - left + 1;
        if (n <= 5) {
            MergeSort.insertionSort(a, left, right, metrics);
            return a[left + n / 2];
        }

        int numGroups = (n + 4) / 5;
        for (int i = 0; i < numGroups; i++) {
            int subLeft = left + i * 5;
            int subRight = Math.min(subLeft + 4, right);
            MergeSort.insertionSort(a, subLeft, subRight, metrics);
            int medianIdx = subLeft + (subRight - subLeft) / 2;
            swap(a, left + i, medianIdx);
        }

        // Recursively find median of the medians (located in a[left .. left + numGroups - 1])
        int medOfMedsRank = left + numGroups / 2;
        return selectHelper(a, left, left + numGroups - 1, medOfMedsRank, metrics);
    }

    /**
     * 3-Way partition around a specified pivot value.
     */
    private static int[] partitionAroundValue(int[] a, int left, int right, int pivotValue, Metrics metrics) {
        int lt = left;
        int gt = right;
        int i = left;

        while (i <= gt) {
            int cmp = metrics.compare(a[i], pivotValue);
            if (cmp < 0) {
                swap(a, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                swap(a, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
