package com.daa.benchmark;

import com.daa.metrics.Metrics;
import com.daa.select.QuickSelect;
import com.daa.sort.MergeSort;
import com.daa.sort.QuickSort;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BenchmarkRunner {

    private static final int[] SIZES = {1_000, 10_000, 100_000, 1_000_000};
    private static final String[] INPUT_TYPES = {"random", "sorted", "duplicates"};
    private static final String[] ALGORITHMS = {"MergeSort", "QuickSort", "QuickSelect"};
    private static final int RUNS_PER_CASE = 5;

    public static class RunResult {
        public final double timeMs;
        public final long comparisons;
        public final int maxDepth;

        public RunResult(double timeMs, long comparisons, int maxDepth) {
            this.timeMs = timeMs;
            this.comparisons = comparisons;
            this.maxDepth = maxDepth;
        }
    }

    public static void main(String[] args) {
        warmUpJvm();

        List<String> csvRows = new ArrayList<>();
        csvRows.add("algorithm,input,n,time_ms,comparisons,max_depth");

        Random rnd = new Random(2026);

        for (String algo : ALGORITHMS) {
            for (String inputType : INPUT_TYPES) {
                for (int n : SIZES) {
                    int[] baseArray = generateArray(inputType, n, rnd);

                    List<RunResult> runResults = new ArrayList<>();
                    for (int r = 0; r < RUNS_PER_CASE; r++) {
                        int[] workArray = baseArray.clone();
                        Metrics metrics = new Metrics();

                        if (algo.equals("MergeSort")) {
                            MergeSort.sort(workArray, metrics);
                        } else if (algo.equals("QuickSort")) {
                            QuickSort.sort(workArray, metrics);
                        } else if (algo.equals("QuickSelect")) {
                            QuickSelect.select(workArray, n / 2, metrics);
                        }

                        runResults.add(new RunResult(metrics.getTimeMs(), metrics.getComparisons(), metrics.getMaxDepth()));
                    }

                    runResults.sort(Comparator.comparingDouble(res -> res.timeMs));
                    RunResult medianRun = runResults.get(RUNS_PER_CASE / 2);

                    System.out.printf("%-11s | %-10s | n=%-7d | time=%8.3f ms | comps=%-11d | depth=%d\n",
                            algo, inputType, n, medianRun.timeMs, medianRun.comparisons, medianRun.maxDepth);

                    String csvRow = String.format(Locale.US, "%s,%s,%d,%.4f,%d,%d",
                            algo, inputType, n, medianRun.timeMs, medianRun.comparisons, medianRun.maxDepth);
                    csvRows.add(csvRow);
                }
            }
        }

        String csvFileName = "results.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFileName))) {
            for (String row : csvRows) {
                pw.println(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PlotGenerator.generateAllPlots(csvFileName, ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void warmUpJvm() {
        Random rnd = new Random(101);
        for (int i = 0; i < 50; i++) {
            int[] arr1 = generateArray("random", 5_000, rnd);
            int[] arr2 = arr1.clone();
            int[] arr3 = arr1.clone();

            MergeSort.sort(arr1, new Metrics());
            QuickSort.sort(arr2, new Metrics());
            QuickSelect.select(arr3, 2500, new Metrics());
        }
    }

    public static int[] generateArray(String inputType, int n, Random rnd) {
        int[] a = new int[n];
        switch (inputType) {
            case "random":
                for (int i = 0; i < n; i++) {
                    a[i] = rnd.nextInt();
                }
                break;
            case "sorted":
                for (int i = 0; i < n; i++) {
                    a[i] = i;
                }
                break;
            case "duplicates":
                for (int i = 0; i < n; i++) {
                    a[i] = rnd.nextInt(10);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown input type: " + inputType);
        }
        return a;
    }
}
