package daa.metrics;

public final class Metrics {
    private long comparisons;
    private int currentDepth;
    private int maxDepth;
    private long startTime;
    private long timeNs;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        timeNs = System.nanoTime() - startTime;
    }

    public double timeMs() {
        return timeNs / 1_000_000.0;
    }

    public int compare(int a, int b) {
        comparisons++;
        return Integer.compare(a, b);
    }

    public void addComparisons(long count) {
        comparisons += count;
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getTimeNs() {
        return timeNs;
    }
}
