package daa;

import daa.bench.Benchmark;
import daa.metrics.CsvWriter;
import daa.metrics.Result;

import java.nio.file.Path;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        List<Result> results = benchmark.run();
        CsvWriter.write(Path.of("results.csv"), results);
        Benchmark.generatePlots(results, Path.of("."));
    }
}
