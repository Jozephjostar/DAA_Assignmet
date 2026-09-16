package com.daa.sort;

import com.daa.metrics.Metrics;

public class MergeSort {

    public static final int INSERTION_SORT_CUTOFF = 15;

    public static Metrics sort(int[] a) {
        Metrics metrics = new Metrics();
        sort(a, metrics);
        return metrics;
    }

    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) return;
        metrics.startTimer();
        mergeSort(a, new int[a.length], 0, a.length - 1, metrics);
        metrics.stopTimer();
    }

    private static void mergeSort(int[] a, int[] buf, int l, int r, Metrics metrics) {
        if (r - l + 1 <= INSERTION_SORT_CUTOFF) {
            insertionSort(a, l, r, metrics);
            return;
        }
        int m = (l + r) / 2;
        metrics.enterRecursion();
        mergeSort(a, buf, l, m, metrics);
        mergeSort(a, buf, m + 1, r, metrics);
        merge(a, buf, l, m, r, metrics);
        metrics.exitRecursion();
    }

    private static void merge(int[] a, int[] buf, int l, int m, int r, Metrics metrics) {
        System.arraycopy(a, l, buf, l, r - l + 1);
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) {
            if (metrics.compare(buf[i], buf[j]) <= 0) {
                a[k++] = buf[i++];
            } else {
                a[k++] = buf[j++];
            }
        }
        while (i <= m) a[k++] = buf[i++];
        while (j <= r) a[k++] = buf[j++];
    }

    public static void insertionSort(int[] a, int l, int r, Metrics metrics) {
        for (int i = l + 1; i <= r; i++) {
            int key = a[i], j = i - 1;
            while (j >= l && metrics.compare(a[j], key) > 0) {
                a[j + 1] = a[j--];
            }
            a[j + 1] = key;
        }
    }
}
