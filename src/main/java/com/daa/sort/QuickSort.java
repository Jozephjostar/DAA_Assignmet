package com.daa.sort;

import com.daa.metrics.Metrics;
import java.util.Random;

public class QuickSort {

    public static Metrics sort(int[] a) {
        Metrics metrics = new Metrics();
        sort(a, metrics);
        return metrics;
    }

    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) {
            return;
        }
        Random rnd = new Random();
        metrics.startTimer();
        quickSort(a, 0, a.length - 1, metrics, rnd);
        metrics.stopTimer();
    }

    private static void quickSort(int[] a, int left, int right, Metrics metrics, Random rnd) {
        while (left < right) {
            metrics.enterRecursion();
            int[] bounds = partition3Way(a, left, right, metrics, rnd);
            int lt = bounds[0];
            int gt = bounds[1];

            int leftSize = lt - left;
            int rightSize = right - gt;

            if (leftSize < rightSize) {
                if (leftSize > 1) {
                    quickSort(a, left, lt - 1, metrics, rnd);
                }
                metrics.exitRecursion();
                left = gt + 1;
            } else {
                if (rightSize > 1) {
                    quickSort(a, gt + 1, right, metrics, rnd);
                }
                metrics.exitRecursion();
                right = lt - 1;
            }
        }
    }

    public static int[] partition3Way(int[] a, int left, int right, Metrics metrics, Random rnd) {
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
