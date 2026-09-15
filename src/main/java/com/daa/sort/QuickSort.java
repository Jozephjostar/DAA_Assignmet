package com.daa.sort;

import com.daa.metrics.Metrics;

import java.util.Random;

/**
 * QuickSort implementation for int[] that never crashes with StackOverflowError:
 * 1. Random Pivot: Selects pivot uniformly at random to prevent O(n^2) on sorted/reversed data.
 * 2. Smaller Side First: Recurses into the smaller partition and handles the larger partition with a while loop,
 *    strictly bounding recursion depth to <= log2(n) + 1.
 * 3. 3-Way Partitioning: Dutch National Flag partition (< pivot, = pivot, > pivot)
 *    ensures arrays with duplicates are sorted efficiently in O(n) time.
 */
public class QuickSort {

    private static final Random DEFAULT_RANDOM = new Random();

    /**
     * Sorts the array using QuickSort with a newly allocated Metrics object.
     *
     * @param a the array to sort
     * @return metrics collected during sorting
     */
    public static Metrics sort(int[] a) {
        Metrics metrics = new Metrics();
        sort(a, metrics);
        return metrics;
    }

    /**
     * Sorts the array using QuickSort and records metrics in the provided Metrics object.
     *
     * @param a       the array to sort
     * @param metrics the metrics object to track comparisons, depth, and time
     */
    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) {
            return;
        }

        Random rnd = new Random();
        metrics.startTimer();
        quickSort(a, 0, a.length - 1, metrics, rnd);
        metrics.stopTimer();
    }

    /**
     * Recursive quicksort with tail recursion elimination on the larger side.
     */
    private static void quickSort(int[] a, int left, int right, Metrics metrics, Random rnd) {
        while (left < right) {
            metrics.enterRecursion();

            // 3-Way Partition returns [lt, gt] where:
            // a[left..lt-1] < pivot
            // a[lt..gt] == pivot
            // a[gt+1..right] > pivot
            int[] bounds = partition3Way(a, left, right, metrics, rnd);
            int lt = bounds[0];
            int gt = bounds[1];

            int leftSize = lt - left;
            int rightSize = right - gt;

            // Recurse into the smaller partition, loop on the larger partition.
            // This guarantees recursion depth <= log2(n) + 1.
            if (leftSize < rightSize) {
                if (leftSize > 1) {
                    quickSort(a, left, lt - 1, metrics, rnd);
                }
                metrics.exitRecursion();
                left = gt + 1; // Tail recursion elimination on right side
            } else {
                if (rightSize > 1) {
                    quickSort(a, gt + 1, right, metrics, rnd);
                }
                metrics.exitRecursion();
                right = lt - 1; // Tail recursion elimination on left side
            }
        }
    }

    /**
     * 3-way partition (< pivot, == pivot, > pivot) around a random pivot.
     * Public static so QuickSelect can reuse the exact same partition method.
     *
     * @param a       the array
     * @param left    left bound (inclusive)
     * @param right   right bound (inclusive)
     * @param metrics metrics object to record comparisons
     * @param rnd     random generator for pivot selection
     * @return int[] array of size 2: [lt, gt], where elements in a[lt..gt] == pivot
     */
    public static int[] partition3Way(int[] a, int left, int right, Metrics metrics, Random rnd) {
        // Random Pivot: Choose pivot uniformly at random to avoid O(n^2) worst-case
        int pivotIndex = left + rnd.nextInt(right - left + 1);
        swap(a, left, pivotIndex);

        int pivot = a[left];
        int lt = left;
        int gt = right;
        int i = left + 1;

        while (i <= gt) {
            int cmp = metrics.compare(a[i], pivot);
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
