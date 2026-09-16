package com.daa.metrics;

public class Metrics {
    private long comparisons;
    private int currentDepth;
    private int maxDepth;
    private long startTime;
    private double timeMs;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        timeMs = (System.nanoTime() - startTime) / 1_000_000.0;
    }

    public int compare(int a, int b) {
        comparisons++;
        return Integer.compare(a, b);
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        if (currentDepth > 0) {
            currentDepth--;
        }
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public double getTimeMs() {
        return timeMs;
    }
}
