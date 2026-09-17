package daa.algorithms;

import daa.metrics.Metrics;
import java.util.concurrent.ThreadLocalRandom;

public final class Partition {
    public static int[] partition(int[] a, int left, int right, Metrics metrics) {
        return partition3Way(a, left, right, metrics);
    }

    public static int[] partition3Way(int[] a, int left, int right, Metrics metrics) {
        int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
        int pivot = a[pivotIndex];
        a[pivotIndex] = a[left];
        a[left] = pivot;

        int lt = left;
        int gt = right;
        int i = left + 1;

        while (i <= gt) {
            int cmp = metrics.compare(a[i], pivot);
            if (cmp < 0) {
                int tmp = a[lt];
                a[lt] = a[i];
                a[i] = tmp;
                lt++;
                i++;
            } else if (cmp > 0) {
                int tmp = a[i];
                a[i] = a[gt];
                a[gt] = tmp;
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }
}
