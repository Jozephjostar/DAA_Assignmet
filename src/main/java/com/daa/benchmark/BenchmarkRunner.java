package com.daa.benchmark;

import com.daa.metrics.Metrics;
import com.daa.select.QuickSelect;
import com.daa.sort.MergeSort;
import com.daa.sort.QuickSort;

import java.io.PrintWriter;
import java.util.*;

public class BenchmarkRunner {

    private static final int[] SIZES = {1_000, 10_000, 100_000, 1_000_000};
    private static final String[] TYPES = {"random", "sorted", "duplicates"};
    private static final String[] ALGOS = {"MergeSort", "QuickSort", "QuickSelect"};

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");

        Random rnd = new Random(2026);
        warmUp(rnd);

        List<String> rows = new ArrayList<>();
        rows.add("algorithm,input,n,time_ms,comparisons,max_depth");

        for (String algo : ALGOS) {
            for (String type : TYPES) {
                for (int n : SIZES) {
                    int[] base = generateArray(type, n, rnd);
                    double[] times = new double[5];
                    long[] comps = new long[5];
                    int[] depths = new int[5];

                    for (int r = 0; r < 5; r++) {
                        int[] arr = base.clone();
                        Metrics m = new Metrics();

                        if (algo.equals("MergeSort")) MergeSort.sort(arr, m);
                        else if (algo.equals("QuickSort")) QuickSort.sort(arr, m);
                        else QuickSelect.select(arr, n / 2, m);

                        times[r] = m.getTimeMs();
                        comps[r] = m.getComparisons();
                        depths[r] = m.getMaxDepth();
                    }

                    Integer[] idx = {0, 1, 2, 3, 4};
                    Arrays.sort(idx, Comparator.comparingDouble(i -> times[i]));
                    int med = idx[2];

                    double medTime = times[med];
                    long medComps = comps[med];
                    int medDepth = depths[med];

                    System.out.printf("%s | %s | n=%d | %.3f ms | comps=%d | depth=%d\n",
                            algo, type, n, medTime, medComps, medDepth);

                    rows.add(String.format(Locale.US, "%s,%s,%d,%.4f,%d,%d",
                            algo, type, n, medTime, medComps, medDepth));
                }
            }
        }

        try (PrintWriter pw = new PrintWriter("results.csv")) {
            for (String row : rows) pw.println(row);
        }

        PlotGenerator.generateAllPlots("results.csv", ".");
    }

    private static void warmUp(Random rnd) {
        for (int i = 0; i < 30; i++) {
            int[] arr = generateArray("random", 3000, rnd);
            MergeSort.sort(arr.clone(), new Metrics());
            QuickSort.sort(arr.clone(), new Metrics());
            QuickSelect.select(arr.clone(), 1500, new Metrics());
        }
    }

    public static int[] generateArray(String type, int n, Random rnd) {
        int[] a = new int[n];
        if (type.equals("random")) {
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        } else if (type.equals("sorted")) {
            for (int i = 0; i < n; i++) a[i] = i;
        } else {
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(10);
        }
        return a;
    }
}
