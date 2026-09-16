package com.daa.bonus;

import com.daa.metrics.Metrics;
import com.daa.sort.MergeSort;

public class MedianOfMediansSelect {

    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("Array is null or empty");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("Invalid rank k=" + k);

        metrics.startTimer();
        int res = selectHelper(a, 0, a.length - 1, k, metrics);
        metrics.stopTimer();
        return res;
    }

    private static int selectHelper(int[] a, int l, int r, int k, Metrics metrics) {
        while (l <= r) {
            if (l == r) return a[l];

            metrics.enterRecursion();
            int pivot = getMedianOfMedians(a, l, r, metrics);
            int[] bounds = partition(a, l, r, pivot, metrics);
            metrics.exitRecursion();

            int lt = bounds[0], gt = bounds[1];
            if (k >= lt && k <= gt) return a[k];
            if (k < lt) r = lt - 1;
            else l = gt + 1;
        }
        return a[l];
    }

    private static int getMedianOfMedians(int[] a, int l, int r, Metrics metrics) {
        int n = r - l + 1;
        if (n <= 5) {
            MergeSort.insertionSort(a, l, r, metrics);
            return a[l + n / 2];
        }

        int numGroups = (n + 4) / 5;
        for (int i = 0; i < numGroups; i++) {
            int subL = l + i * 5;
            int subR = Math.min(subL + 4, r);
            MergeSort.insertionSort(a, subL, subR, metrics);
            swap(a, l + i, subL + (subR - subL) / 2);
        }

        return selectHelper(a, l, l + numGroups - 1, l + numGroups / 2, metrics);
    }

    private static int[] partition(int[] a, int l, int r, int pivot, Metrics metrics) {
        int lt = l, gt = r, i = l;
        while (i <= gt) {
            int cmp = metrics.compare(a[i], pivot);
            if (cmp < 0) swap(a, lt++, i++);
            else if (cmp > 0) swap(a, i, gt--);
            else i++;
        }
        return new int[]{lt, gt};
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
