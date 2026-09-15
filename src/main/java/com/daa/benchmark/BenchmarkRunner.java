package com.daa.benchmark;

import com.daa.metrics.Metrics;
import com.daa.select.QuickSelect;
import com.daa.sort.MergeSort;
import com.daa.sort.QuickSort;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Benchmark runner executing:
 * - Algorithms: MergeSort, QuickSort, QuickSelect
 * - Sizes: n = 1_000, 10_000, 100_000, 1_000_000
 * - Inputs: random, sorted, duplicates (0 to 9)
 * - 5 runs per case, taking the median run (addressing JVM warm-up)
 * - Exports to results.csv
 * - Generates time_vs_n.png, depth_vs_n.png, ratio_vs_n.png
 */
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
        System.out.println("==================================================");
        System.out.println("Starting Benchmark for DAA Assignment 1");
        System.out.println("Algorithms: MergeSort, QuickSort, QuickSelect");
        System.out.println("Sizes: 1,000; 10,000; 100,000; 1,000,000");
        System.out.println("Input Types: random, sorted, duplicates");
        System.out.println("5 runs per case, taking median time");
        System.out.println("==================================================");

        warmUpJvm();

        List<String> csvRows = new ArrayList<>();
        csvRows.add("algorithm,input,n,time_ms,comparisons,max_depth");

        Random rnd = new Random(2026);

        for (String algo : ALGORITHMS) {
            for (String inputType : INPUT_TYPES) {
                for (int n : SIZES) {
                    System.out.printf("Running %-11s | Input: %-10s | n = %,9d ... ", algo, inputType, n);
                    System.out.flush();

                    // Generate base array
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
                            int k = n / 2; // select median element
                            QuickSelect.select(workArray, k, metrics);
                        }

                        runResults.add(new RunResult(metrics.getTimeMs(), metrics.getComparisons(), metrics.getMaxDepth()));
                    }

                    // Sort by timeMs to find median run
                    runResults.sort(Comparator.comparingDouble(res -> res.timeMs));
                    RunResult medianRun = runResults.get(RUNS_PER_CASE / 2);

                    System.out.printf("Median Time: %8.3f ms | Comps: %,12d | Depth: %2d\n",
                            medianRun.timeMs, medianRun.comparisons, medianRun.maxDepth);

                    String csvRow = String.format(Locale.US, "%s,%s,%d,%.4f,%d,%d",
                            algo, inputType, n, medianRun.timeMs, medianRun.comparisons, medianRun.maxDepth);
                    csvRows.add(csvRow);
                }
            }
        }

        // Write to results.csv
        String csvFileName = "results.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFileName))) {
            for (String row : csvRows) {
                pw.println(row);
            }
            System.out.println("Successfully saved results to " + csvFileName);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
            e.printStackTrace();
        }

        // Generate Plots
        try {
            System.out.println("Generating PNG plots: time_vs_n.png, depth_vs_n.png, ratio_vs_n.png...");
            PlotGenerator.generateAllPlots(csvFileName, ".");
            System.out.println("Plots successfully generated!");
        } catch (Exception e) {
            System.err.println("Error generating plots: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("==================================================");
        System.out.println("Benchmark completed successfully!");
        System.out.println("==================================================");
    }

    private static void warmUpJvm() {
        System.out.print("Warming up JVM JIT compiler... ");
        Random rnd = new Random(101);
        for (int i = 0; i < 50; i++) {
            int[] arr1 = generateArray("random", 5_000, rnd);
            int[] arr2 = arr1.clone();
            int[] arr3 = arr1.clone();

            MergeSort.sort(arr1, new Metrics());
            QuickSort.sort(arr2, new Metrics());
            QuickSelect.select(arr3, 2500, new Metrics());
        }
        System.out.println("Done.");
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
                    a[i] = rnd.nextInt(10); // 0 to 9
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown input type: " + inputType);
        }
        return a;
    }
}
