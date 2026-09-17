package daa.algorithms;

import daa.metrics.Metrics;

public final class InsertionSort implements Sorter {
    @Override
    public void sort(int[] a, Metrics metrics) {
        sort(a, 0, a.length - 1, metrics);
    }

    public static void sort(int[] a, int left, int right, Metrics metrics) {
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
