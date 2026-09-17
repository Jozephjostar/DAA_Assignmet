package daa.bench;

import daa.algorithms.InsertionSort;
import daa.algorithms.MergeSort;
import daa.algorithms.QuickSelect;
import daa.algorithms.QuickSort;
import daa.algorithms.Sorter;
import daa.metrics.Metrics;
import daa.metrics.Result;
import daa.util.ArrayUtils;
import daa.util.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class Benchmark {
    private static final int[] SIZES = {1_000, 10_000, 100_000, 1_000_000};
    private static final int RUNS = 5;

    @FunctionalInterface
    public interface Task {
        void run(int[] data, Metrics metrics);
    }

    public List<Result> run() {
        List<Result> results = new ArrayList<>();
        Sorter insertionSort = new InsertionSort();
        Sorter mergeSort = new MergeSort();
        Sorter quickSort = new QuickSort();
        QuickSelect quickSelect = new QuickSelect();

        benchmark("InsertionSort", 10_000, true, (data, metrics) -> insertionSort.sort(data, metrics), results);
        benchmark("MergeSort", Integer.MAX_VALUE, true, (data, metrics) -> mergeSort.sort(data, metrics), results);
        benchmark("QuickSort", Integer.MAX_VALUE, true, (data, metrics) -> quickSort.sort(data, metrics), results);
        benchmark("QuickSelect", Integer.MAX_VALUE, false, (data, metrics) -> quickSelect.select(data, data.length / 2, metrics), results);

        return results;
    }

    private void benchmark(String algorithm, int maxN, boolean mustSort, Task task, List<Result> out) {
        try {
            for (InputType input : InputType.values()) {
                for (int n : SIZES) {
                    if (n > maxN) {
                        continue;
                    }
                    Result result = medianOfRuns(algorithm, input, n, mustSort, task);
                    out.add(result);
                    System.out.printf(Locale.ROOT, "%-14s %-11s n=%-9d %10.3f ms  comparisons=%-11d max_depth=%d%n",
                            algorithm, input.label(), n, result.timeMs(), result.comparisons(), result.maxDepth());
                }
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("[skip] " + algorithm + ": " + e.getMessage());
        }
    }

    private Result medianOfRuns(String algorithm, InputType input, int n, boolean mustSort, Task task) {
        Result[] runs = new Result[RUNS];
        for (int r = 0; r < RUNS; r++) {
            int[] data = ArrayUtils.generate(input, n);
            Metrics metrics = new Metrics();
            metrics.startTimer();
            task.run(data, metrics);
            metrics.stopTimer();
            if (mustSort && !ArrayUtils.isSorted(data)) {
                throw new IllegalStateException(algorithm + " failed to sort");
            }
            runs[r] = new Result(algorithm, input.label(), n, metrics.timeMs(), metrics.getComparisons(), metrics.getMaxDepth());
        }
        Arrays.sort(runs, Comparator.comparingDouble(Result::timeMs));
        return runs[RUNS / 2];
    }
}
