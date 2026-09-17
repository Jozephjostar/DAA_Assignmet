package daa.algorithms;

import daa.metrics.Metrics;

public final class MergeSort implements Sorter {
    public static final int CUTOFF = 15;

    public static Metrics sort(int[] a) {
        Metrics metrics = new Metrics();
        new MergeSort().sort(a, metrics);
        return metrics;
    }

    @Override
    public void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) {
            return;
        }
        int[] aux = new int[a.length];
        sort(a, aux, 0, a.length - 1, metrics);
    }

    private void sort(int[] a, int[] aux, int left, int right, Metrics metrics) {
        if (right - left + 1 <= CUTOFF) {
            InsertionSort.sort(a, left, right, metrics);
            return;
        }
        metrics.enterRecursion();
        int mid = left + (right - left) / 2;
        sort(a, aux, left, mid, metrics);
        sort(a, aux, mid + 1, right, metrics);
        merge(a, aux, left, mid, right, metrics);
        metrics.exitRecursion();
    }

    private void merge(int[] a, int[] aux, int left, int mid, int right, Metrics metrics) {
        for (int k = left; k <= right; k++) {
            aux[k] = a[k];
        }
        int i = left;
        int j = mid + 1;
        for (int k = left; k <= right; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > right) {
                a[k] = aux[i++];
            } else if (metrics.compare(aux[j], aux[i]) < 0) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }
}
