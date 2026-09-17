package daa.util;

import java.util.Random;

public final class ArrayUtils {
    private static final Random RANDOM = new Random(42);

    public static int[] generate(InputType input, int n) {
        return switch (input) {
            case RANDOM -> random(n);
            case SORTED -> sorted(n);
            case DUPLICATES -> duplicates(n);
        };
    }

    public static int[] random(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = RANDOM.nextInt();
        }
        return a;
    }

    public static int[] sorted(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        return a;
    }

    public static int[] duplicates(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = RANDOM.nextInt(10);
        }
        return a;
    }

    public static boolean isSorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static int[] copy(int[] a) {
        int[] c = new int[a.length];
        System.arraycopy(a, 0, c, 0, a.length);
        return c;
    }
}
