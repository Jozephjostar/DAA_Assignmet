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
            throw new IllegalArgumentException("Invalid rank k=" + k + " for array length " + a.length);
        }

        Random rnd = new Random();
        metrics.startTimer();
        int result = quickSelect(a, 0, a.length - 1, k, metrics, rnd);
        metrics.stopTimer();
        return result;
    }

    private static int quickSelect(int[] a, int left, int right, int k, Metrics metrics, Random rnd) {
        metrics.enterRecursion();
        try {
            if (left >= right) {
                return a[left];
            }

            int[] bounds = QuickSort.partition3Way(a, left, right, metrics, rnd);
            int lt = bounds[0];
            int gt = bounds[1];

            if (k >= lt && k <= gt) {
                return a[k];
            } else if (k < lt) {
                return quickSelect(a, left, lt - 1, k, metrics, rnd);
            } else {
                return quickSelect(a, gt + 1, right, k, metrics, rnd);
            }
        } finally {
            metrics.exitRecursion();
        }
    }
}
