package com.daa.metrics;

/**
 * Class for collecting algorithm performance metrics:
 * - Number of comparisons
 * - Maximum recursion depth
 * - Execution time in milliseconds (via System.nanoTime())
 *
 * Instances are passed directly to algorithms instead of using global state.
 */
public class Metrics {
    private long comparisons;
    private int currentDepth;
    private int maxDepth;
    private long startTimeNs;
    private long elapsedTimeNs;

    public Metrics() {
        reset();
    }

    /**
     * Resets all collected metrics to zero.
     */
    public void reset() {
        this.comparisons = 0;
        this.currentDepth = 0;
        this.maxDepth = 0;
        this.startTimeNs = 0;
        this.elapsedTimeNs = 0;
    }

    /**
     * Starts execution timer.
     */
    public void startTimer() {
        this.startTimeNs = System.nanoTime();
    }

    /**
     * Stops execution timer and calculates elapsed time.
     */
    public void stopTimer() {
        this.elapsedTimeNs = System.nanoTime() - this.startTimeNs;
    }

    /**
     * Increments the comparison counter by 1.
     */
    public void incComparisons() {
        this.comparisons++;
    }

    /**
     * Increments the comparison counter by a specific delta.
     */
    public void incComparisons(long delta) {
        this.comparisons += delta;
    }

    /**
     * Compares two integers (a < b) and increments the comparison counter.
     */
    public boolean isLessThan(int a, int b) {
        this.comparisons++;
        return a < b;
    }

    /**
     * Compares two integers (a <= b) and increments the comparison counter.
     */
    public boolean isLessOrEqual(int a, int b) {
        this.comparisons++;
        return a <= b;
    }

    /**
     * Compares two integers (a > b) and increments the comparison counter.
     */
    public boolean isGreaterThan(int a, int b) {
        this.comparisons++;
        return a > b;
    }

    /**
     * Standard three-way integer comparison, increments the comparison counter.
     */
    public int compare(int a, int b) {
        this.comparisons++;
        return Integer.compare(a, b);
    }

    /**
     * Called when entering a recursive call frame.
     * Updates current and maximum recursion depths.
     */
    public void enterRecursion() {
        this.currentDepth++;
        if (this.currentDepth > this.maxDepth) {
            this.maxDepth = this.currentDepth;
        }
    }

    /**
     * Called when exiting a recursive call frame.
     */
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
