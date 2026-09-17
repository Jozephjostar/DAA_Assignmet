package daa.algorithms;

import daa.metrics.Metrics;

public final class QuickSort implements Sorter {
    public static Metrics sort(int[] a) {
        Metrics metrics = new Metrics();
        new QuickSort().sort(a, metrics);
        return metrics;
    }

    @Override
    public void sort(int[] a, Metrics metrics) {
        if (a == null || a.length <= 1) {
            return;
        }
        quickSort(a, 0, a.length - 1, metrics);
    }

    private void quickSort(int[] a, int left, int right, Metrics metrics) {
        while (left < right) {
            int[] bounds = Partition.partition(a, left, right, metrics);
            int lt = bounds[0];
            int gt = bounds[1];

            int leftSize = lt - left;
            int rightSize = right - gt;

            if (leftSize < rightSize) {
                if (left < lt - 1) {
                    metrics.enterRecursion();
                    quickSort(a, left, lt - 1, metrics);
                    metrics.exitRecursion();
                }
                left = gt + 1;
            } else {
                if (gt + 1 < right) {
                    metrics.enterRecursion();
                    quickSort(a, gt + 1, right, metrics);
                    metrics.exitRecursion();
                }
                right = lt - 1;
            }
        }
    }
}
