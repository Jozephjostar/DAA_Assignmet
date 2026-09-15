package com.daa.benchmark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * High-quality chart generator producing PNG plots directly via Java AWT.
 * Generates:
 * 1. time_vs_n.png - Running time (ms) vs n
 * 2. depth_vs_n.png - Maximum recursion depth vs n
 * 3. ratio_vs_n.png - Asymptotic ratio vs n (checking Theta bound)
 */
public class PlotGenerator {

    public static class Record {
        public final String algorithm;
        public final String input;
        public final int n;
        public final double timeMs;
        public final long comparisons;
        public final int maxDepth;

        public Record(String algorithm, String input, int n, double timeMs, long comparisons, int maxDepth) {
            this.algorithm = algorithm;
            this.input = input;
            this.n = n;
            this.timeMs = timeMs;
            this.comparisons = comparisons;
            this.maxDepth = maxDepth;
        }

        public String seriesKey() {
            return algorithm + " (" + input + ")";
        }

        public double getRatio() {
            if (algorithm.equalsIgnoreCase("QuickSelect")) {
                // comparisons / n for QuickSelect (linear theoretical growth)
                return (double) comparisons / n;
            } else {
                // comparisons / (n * log2(n)) for MergeSort and QuickSort
                double log2n = Math.log(n) / Math.log(2.0);
                return (double) comparisons / (n * log2n);
            }
        }
    }

    public static List<Record> loadCsv(String csvPath) throws IOException {
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                String algo = parts[0];
                String input = parts[1];
                int n = Integer.parseInt(parts[2]);
                double time = Double.parseDouble(parts[3]);
                long comps = Long.parseLong(parts[4]);
                int depth = Integer.parseInt(parts[5]);
                records.add(new Record(algo, input, n, time, comps, depth));
            }
        }
        return records;
    }

    public static void generateAllPlots(String csvPath, String outputDir) throws IOException {
        List<Record> records = loadCsv(csvPath);

        // Group records by series
        Map<String, List<Record>> seriesMap = new LinkedHashMap<>();
        for (Record r : records) {
            seriesMap.computeIfAbsent(r.seriesKey(), k -> new ArrayList<>()).add(r);
        }

        // Sort each series by n
        for (List<Record> list : seriesMap.values()) {
            list.sort(Comparator.comparingInt(r -> r.n));
        }

        renderPlot(seriesMap, "Time vs n", "Array Size (n)", "Execution Time (ms, log scale)",
                new File(outputDir, "time_vs_n.png"), r -> r.timeMs, true);

        renderPlot(seriesMap, "Max Recursion Depth vs n", "Array Size (n)", "Max Recursion Depth",
                new File(outputDir, "depth_vs_n.png"), r -> (double) r.maxDepth, false);

        renderPlot(seriesMap, "Ratio vs n (Theta Bound Check)", "Array Size (n)",
                "Ratio [comps / (n * log2(n)) or comps / n]",
                new File(outputDir, "ratio_vs_n.png"), Record::getRatio, false);
    }

    private static void renderPlot(Map<String, List<Record>> seriesMap, String title,
                                   String xLabel, String yLabel, File outFile,
                                   java.util.function.Function<Record, Double> valueExtractor,
                                   boolean logY) throws IOException {
        int width = 1000;
        int height = 650;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background
        g.setColor(new Color(250, 252, 255));
        g.fillRect(0, 0, width, height);

        int padLeft = 90;
        int padRight = 240;
        int padTop = 60;
        int padBottom = 80;

        int plotWidth = width - padLeft - padRight;
        int plotHeight = height - padTop - padBottom;

        // Plot background
        g.setColor(Color.WHITE);
        g.fillRect(padLeft, padTop, plotWidth, plotHeight);
        g.setColor(new Color(220, 224, 230));
        g.drawRect(padLeft, padTop, plotWidth, plotHeight);

        // Compute ranges
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (List<Record> list : seriesMap.values()) {
            for (Record r : list) {
                double v = valueExtractor.apply(r);
                if (logY && v <= 0) v = 0.001;
                minY = Math.min(minY, v);
                maxY = Math.max(maxY, v);
            }
        }

        if (logY) {
            minY = Math.max(0.001, minY);
            minY = Math.pow(10, Math.floor(Math.log10(minY)));
            maxY = Math.pow(10, Math.ceil(Math.log10(maxY)));
        } else {
            minY = 0;
            maxY = maxY * 1.15;
            if (maxY == 0) maxY = 10;
        }

        int[] xValues = {1_000, 10_000, 100_000, 1_000_000};

        // Grid & X-axis ticks
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < xValues.length; i++) {
            double normX = (double) i / (xValues.length - 1);
            int x = padLeft + (int) (normX * plotWidth);

            g.setColor(new Color(235, 238, 242));
            g.drawLine(x, padTop, x, padTop + plotHeight);

            g.setColor(new Color(80, 90, 100));
            String lbl = String.format("%,d", xValues[i]);
            int lblW = fm.stringWidth(lbl);
            g.drawString(lbl, x - lblW / 2, padTop + plotHeight + 20);
        }

        // Y-axis grid & labels
        int numYTicks = 6;
        for (int i = 0; i <= numYTicks; i++) {
            double frac = (double) i / numYTicks;
            int y = padTop + plotHeight - (int) (frac * plotHeight);

            g.setColor(new Color(235, 238, 242));
            g.drawLine(padLeft, y, padLeft + plotWidth, y);

            g.setColor(new Color(80, 90, 100));
            String yText;
            if (logY) {
                double val = minY * Math.pow(maxY / minY, frac);
                if (val >= 1) {
                    yText = String.format("%.1f", val);
                } else {
                    yText = String.format("%.3f", val);
                }
            } else {
                double val = minY + frac * (maxY - minY);
                if (maxY < 10) {
                    yText = String.format("%.2f", val);
                } else {
                    yText = String.format("%.0f", val);
                }
            }
            int tw = fm.stringWidth(yText);
            g.drawString(yText, padLeft - tw - 10, y + 4);
        }

        // Palette for distinct series
        Color[] colors = {
                new Color(31, 119, 180), // blue
                new Color(51, 160, 44),  // green
                new Color(227, 26, 28),  // red
                new Color(255, 127, 0),  // orange
                new Color(106, 61, 154), // purple
                new Color(177, 89, 40),  // brown
                new Color(20, 160, 170), // teal
                new Color(230, 80, 150), // pink
                new Color(100, 100, 100) // gray
        };

        // Draw Series
        int sIdx = 0;
        for (Map.Entry<String, List<Record>> entry : seriesMap.entrySet()) {
            Color color = colors[sIdx % colors.length];
            sIdx++;
            List<Record> list = entry.getValue();

            g.setColor(color);
            g.setStroke(new BasicStroke(2.2f));

            int prevX = -1, prevY = -1;
            for (Record r : list) {
                int xIndex = -1;
                for (int i = 0; i < xValues.length; i++) {
                    if (xValues[i] == r.n) {
                        xIndex = i;
                        break;
                    }
                }
                if (xIndex == -1) continue;

                double normX = (double) xIndex / (xValues.length - 1);
                int px = padLeft + (int) (normX * plotWidth);

                double v = valueExtractor.apply(r);
                if (logY && v <= 0) v = 0.001;

                double normY;
                if (logY) {
                    normY = (Math.log10(v) - Math.log10(minY)) / (Math.log10(maxY) - Math.log10(minY));
                } else {
                    normY = (v - minY) / (maxY - minY);
                }
                normY = Math.max(0.0, Math.min(1.0, normY));
                int py = padTop + plotHeight - (int) (normY * plotHeight);

                if (prevX != -1) {
                    g.draw(new Line2D.Double(prevX, prevY, px, py));
                }
                prevX = px;
                prevY = py;

                g.fill(new Ellipse2D.Double(px - 4, py - 4, 8, 8));
            }
        }

        // Titles and Labels
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.setColor(new Color(20, 30, 45));
        int titleW = g.getFontMetrics().stringWidth(title);
        g.drawString(title, padLeft + (plotWidth - titleW) / 2, 35);

        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.setColor(new Color(50, 60, 75));
        int xlW = g.getFontMetrics().stringWidth(xLabel);
        g.drawString(xLabel, padLeft + (plotWidth - xlW) / 2, height - 30);

        // Y-axis label (vertical)
        Graphics2D gRotated = (Graphics2D) g.create();
        gRotated.setFont(new Font("SansSerif", Font.BOLD, 13));
        gRotated.setColor(new Color(50, 60, 75));
        gRotated.translate(25, padTop + plotHeight / 2);
        gRotated.rotate(-Math.PI / 2);
        int ylW = gRotated.getFontMetrics().stringWidth(yLabel);
        gRotated.drawString(yLabel, -ylW / 2, 0);
        gRotated.dispose();

        // Draw Legend on the right side
        int legX = padLeft + plotWidth + 20;
        int legY = padTop + 10;
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(new Color(20, 30, 45));
        g.drawString("Legend", legX, legY);
        legY += 15;

        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sIdx = 0;
        for (String sName : seriesMap.keySet()) {
            Color color = colors[sIdx % colors.length];
            sIdx++;

            g.setColor(color);
            g.fillRect(legX, legY + 2, 14, 10);
            g.setColor(new Color(40, 50, 60));
            g.drawString(sName, legX + 20, legY + 11);
            legY += 20;
        }

        g.dispose();
        ImageIO.write(image, "PNG", outFile);
    }
}
