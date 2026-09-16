package com.daa.select;

import com.daa.metrics.Metrics;
import com.daa.sort.QuickSort;
import java.util.Random;

public class QuickSelect {

    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Invalid rank k=" + k);
        }

        Random rnd = new Random();
        metrics.startTimer();
        int res = quickSelect(a, 0, a.length - 1, k, metrics, rnd);
        metrics.stopTimer();
        return res;
    }

    private static int quickSelect(int[] a, int l, int r, int k, Metrics metrics, Random rnd) {
        metrics.enterRecursion();
        try {
            if (l >= r) return a[l];

            int[] bounds = QuickSort.partition3Way(a, l, r, metrics, rnd);
            int lt = bounds[0], gt = bounds[1];

            if (k >= lt && k <= gt) return a[k];
            if (k < lt) return quickSelect(a, l, lt - 1, k, metrics, rnd);
            return quickSelect(a, gt + 1, r, k, metrics, rnd);
        } finally {
            metrics.exitRecursion();
        }
    }
}
