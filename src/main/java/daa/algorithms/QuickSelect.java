package daa.algorithms;

import daa.metrics.Metrics;

public final class QuickSelect {
    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0 || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Invalid input: array empty or k out of bounds");
        }
        return quickSelect(a, 0, a.length - 1, k, metrics);
    }

    private static int quickSelect(int[] a, int left, int right, int k, Metrics metrics) {
        while (left <= right) {
            int[] bounds = Partition.partition(a, left, right, metrics);
            int lt = bounds[0];
            int gt = bounds[1];

            if (k >= lt && k <= gt) {
                return a[k];
            } else if (k < lt) {
                metrics.enterRecursion();
                int res = quickSelect(a, left, lt - 1, k, metrics);
                metrics.exitRecursion();
                return res;
            } else {
                metrics.enterRecursion();
                int res = quickSelect(a, gt + 1, right, k, metrics);
                metrics.exitRecursion();
                return res;
            }
        }
        return a[k];
    }
}
