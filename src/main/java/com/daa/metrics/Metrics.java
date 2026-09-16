package com.daa.metrics;

public class Metrics {
    private long comparisons;
    private int currentDepth;
    private int maxDepth;
    private long startTimeNs;
    private long elapsedTimeNs;

    public Metrics() {
        reset();
    }

    public void reset() {
        this.comparisons = 0;
        this.currentDepth = 0;
        this.maxDepth = 0;
        this.startTimeNs = 0;
        this.elapsedTimeNs = 0;
    }

    public void startTimer() {
        this.startTimeNs = System.nanoTime();
    }

    public void stopTimer() {
        this.elapsedTimeNs = System.nanoTime() - this.startTimeNs;
    }

    public void incComparisons() {
        this.comparisons++;
    }

    public void incComparisons(long delta) {
        this.comparisons += delta;
    }

    public boolean isLessThan(int a, int b) {
        this.comparisons++;
        return a < b;
    }

    public boolean isLessOrEqual(int a, int b) {
        this.comparisons++;
        return a <= b;
    }

    public boolean isGreaterThan(int a, int b) {
        this.comparisons++;
        return a > b;
    }

    public int compare(int a, int b) {
        this.comparisons++;
        return Integer.compare(a, b);
    }

    public void enterRecursion() {
        this.currentDepth++;
        if (this.currentDepth > this.maxDepth) {
            this.maxDepth = this.currentDepth;
        }
    }

    public void exitRecursion() {
        if (this.currentDepth > 0) {
            this.currentDepth--;
        }
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public long getElapsedTimeNs() {
        return elapsedTimeNs;
    }

    public double getTimeMs() {
        return elapsedTimeNs / 1_000_000.0;
    }

    @Override
    public String toString() {
        return String.format("Metrics{time=%.3f ms, comparisons=%d, maxDepth=%d}",
                getTimeMs(), comparisons, maxDepth);
    }
}
