package com.daa.sort;

import com.daa.metrics.Metrics;

/**
 * MergeSort implementation for int[] featuring:
 * 1. Reusable Buffer: One helper array allocated once in the top-level call and passed down.
 * 2. Cutoff: Subarrays with 15 elements or fewer are sorted with Insertion Sort.
 * 3. Linear Merge: O(n) merging of sorted halves without auxiliary array allocation in recursion.
 */
public class MergeSort {

    public static final int INSERTION_SORT_CUTOFF = 15;

    /**
     * Sorts the entire array using MergeSort with a newly allocated Metrics object.
     *
     * @param a the array to sort
     * @return the metrics recorded during sorting
     */
    public static Metrics sort(int[] a) {
        Metrics metrics = new Metrics();
        sort(a, metrics);
        return metrics;
    }

    /**
     * Sorts the array using MergeSort and records metrics in the provided Metrics object.
     *
     * @param a       the array to sort
     * @param metrics the metrics object to track comparisons, depth, and time
     */
    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) {
            return;
        }

        // Reusable Buffer: Allocate one helper array once in the top-level call.
        // Never create new int[...] inside recursive calls.
        int[] buffer = new int[a.length];

        metrics.startTimer();
        mergeSort(a, buffer, 0, a.length - 1, metrics);
        metrics.stopTimer();
    }

    private static void mergeSort(int[] a, int[] buffer, int left, int right, Metrics metrics) {
        if (left >= right) {
            return;
        }

        // Cutoff: If a subarray has 15 elements or fewer, sort it with Insertion Sort.
        if (right - left + 1 <= INSERTION_SORT_CUTOFF) {
            insertionSort(a, left, right, metrics);
            return;
        }

        int mid = left + (right - left) / 2;

        metrics.enterRecursion();
        mergeSort(a, buffer, left, mid, metrics);
        mergeSort(a, buffer, mid + 1, right, metrics);
        merge(a, buffer, left, mid, right, metrics);
        metrics.exitRecursion();
    }

    /**
     * Linear merge of two sorted halves: a[left..mid] and a[mid+1..right].
     * Time complexity: O(n).
     */
    private static void merge(int[] a, int[] buffer, int left, int mid, int right, Metrics metrics) {
        // Copy elements into the reusable helper buffer
        System.arraycopy(a, left, buffer, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (metrics.isLessOrEqual(buffer[i], buffer[j])) {
                a[k++] = buffer[i++];
            } else {
                a[k++] = buffer[j++];
            }
        }

        while (i <= mid) {
            a[k++] = buffer[i++];
        }

        while (j <= right) {
            a[k++] = buffer[j++];
        }
    }

    /**
     * Insertion Sort for small subarrays (cutoff <= 15).
     */
    public static void insertionSort(int[] a, int left, int right, Metrics metrics) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left && metrics.isGreaterThan(a[j], key)) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
}
