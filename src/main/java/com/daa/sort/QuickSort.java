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
        if (a == null || a.length <= 1) return;
        Random rnd = new Random();
        metrics.startTimer();
        quickSort(a, 0, a.length - 1, metrics, rnd);
        metrics.stopTimer();
    }

    private static void quickSort(int[] a, int l, int r, Metrics metrics, Random rnd) {
        while (l < r) {
            int[] bounds = partition3Way(a, l, r, metrics, rnd);
            int lt = bounds[0], gt = bounds[1];

            if (lt - l < r - gt) {
                if (lt - 1 > l) {
                    metrics.enterRecursion();
                    quickSort(a, l, lt - 1, metrics, rnd);
                    metrics.exitRecursion();
                }
                l = gt + 1;
            } else {
                if (r > gt + 1) {
                    metrics.enterRecursion();
                    quickSort(a, gt + 1, r, metrics, rnd);
                    metrics.exitRecursion();
                }
                r = lt - 1;
            }
        }
    }

    public static int[] partition3Way(int[] a, int l, int r, Metrics metrics, Random rnd) {
        int pIdx = l + rnd.nextInt(r - l + 1);
        swap(a, l, pIdx);
        int pivot = a[l], lt = l, gt = r, i = l + 1;

        while (i <= gt) {
            int cmp = metrics.compare(a[i], pivot);
            if (cmp < 0) {
                swap(a, lt++, i++);
            } else if (cmp > 0) {
                swap(a, i, gt--);
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
