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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

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

    public static void generatePlots(List<Result> results, Path dir) {
        plotMetric(results, dir.resolve("time_vs_n.png"), "Execution Time vs N", "Time (ms)", r -> r.timeMs());
        plotMetric(results, dir.resolve("depth_vs_n.png"), "Max Recursion Depth vs N", "Depth", r -> (double) r.maxDepth());
        plotMetric(results, dir.resolve("ratio_vs_n.png"), "Asymptotic Ratio vs N", "Ratio", r -> {
            if ("QuickSelect".equals(r.algorithm())) {
                return (double) r.comparisons() / r.n();
            } else {
                return (double) r.comparisons() / (r.n() * (Math.log(r.n()) / Math.log(2)));
            }
        });
    }

    private interface ValueExtractor {
        double extract(Result r);
    }

    private static void plotMetric(List<Result> results, Path file, String title, String yLabel, ValueExtractor extractor) {
        int w = 900;
        int h = 600;
        int padL = 90;
        int padR = 190;
        int padT = 60;
        int padB = 60;

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        double minY = 0.0;
        double maxY = 1.0;
        for (Result r : results) {
            double v = extractor.extract(r);
            if (v > maxY) maxY = v;
        }
        maxY *= 1.15;

        g.setColor(new Color(230, 230, 230));
        for (int i = 0; i <= 5; i++) {
            int y = padT + (h - padT - padB) * i / 5;
            g.drawLine(padL, y, w - padR, y);
            double val = maxY - (maxY - minY) * i / 5;
            g.setColor(Color.GRAY);
            g.drawString(String.format(Locale.ROOT, "%.1f", val), 15, y + 5);
            g.setColor(new Color(230, 230, 230));
        }

        g.setColor(Color.BLACK);
        g.drawLine(padL, h - padB, w - padR, h - padB);
        g.drawLine(padL, padT, padL, h - padB);

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString(title, padL, 35);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString("Input size n", w / 2 - 30, h - 20);

        int[] xCoords = new int[SIZES.length];
        for (int i = 0; i < SIZES.length; i++) {
            xCoords[i] = padL + (w - padL - padR) * i / (SIZES.length - 1);
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf(SIZES[i]), xCoords[i] - 15, h - padB + 20);
        }

        Map<String, List<Result>> series = new LinkedHashMap<>();
        for (Result r : results) {
            String key = r.algorithm() + " (" + r.input() + ")";
            series.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        Color[] colors = {
                new Color(31, 119, 180), new Color(255, 127, 14), new Color(44, 160, 44),
                new Color(214, 39, 40), new Color(148, 103, 189), new Color(140, 86, 75),
                new Color(227, 119, 194), new Color(127, 127, 127), new Color(188, 189, 34),
                new Color(23, 190, 207), new Color(50, 50, 150), new Color(150, 50, 50)
        };

        int colorIdx = 0;
        int legY = padT + 20;

        for (Map.Entry<String, List<Result>> entry : series.entrySet()) {
            Color col = colors[colorIdx % colors.length];
            colorIdx++;
            g.setColor(col);

            List<Point> pts = new ArrayList<>();
            for (Result r : entry.getValue()) {
                int sIdx = -1;
                for (int i = 0; i < SIZES.length; i++) {
                    if (SIZES[i] == r.n()) {
                        sIdx = i;
                        break;
                    }
                }
                if (sIdx != -1) {
                    int px = xCoords[sIdx];
                    double val = extractor.extract(r);
                    int py = (int) ((h - padB) - (val - minY) / (maxY - minY) * (h - padT - padB));
                    pts.add(new Point(px, py));
                }
            }

            g.setStroke(new BasicStroke(2.0f));
            for (int i = 0; i < pts.size() - 1; i++) {
                g.drawLine(pts.get(i).x, pts.get(i).y, pts.get(i + 1).x, pts.get(i + 1).y);
            }
            for (Point p : pts) {
                g.fillOval(p.x - 4, p.y - 4, 8, 8);
            }

            g.fillRect(w - padR + 15, legY - 10, 14, 14);
            g.setColor(Color.BLACK);
            g.drawString(entry.getKey(), w - padR + 35, legY + 2);
            legY += 22;
        }

        g.dispose();
        try {
            ImageIO.write(img, "png", file.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
