package daa.metrics;

public record Result(
    String algorithm,
    String input,
    int n,
    double timeMs,
    long comparisons,
    int maxDepth
) {}
