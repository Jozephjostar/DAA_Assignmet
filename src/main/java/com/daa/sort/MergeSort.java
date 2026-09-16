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
        if (a == null || a.length <= 1) {
            return;
        }
        int[] buffer = new int[a.length];
        metrics.startTimer();
        mergeSort(a, buffer, 0, a.length - 1, metrics);
        metrics.stopTimer();
    }

    private static void mergeSort(int[] a, int[] buffer, int left, int right, Metrics metrics) {
        if (left >= right) {
            return;
        }

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

    private static void merge(int[] a, int[] buffer, int left, int mid, int right, Metrics metrics) {
        System.arraycopy(a, left, buffer, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (metrics.compare(buffer[i], buffer[j]) <= 0) {
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

    public static void insertionSort(int[] a, int left, int right, Metrics metrics) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left && metrics.compare(a[j], key) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
}
