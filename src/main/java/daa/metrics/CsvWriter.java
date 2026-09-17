package daa.metrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class CsvWriter {
    public static void write(Path path, List<Result> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("algorithm,input,n,time_ms,comparisons,max_depth\n");
        for (Result r : results) {
            sb.append(String.format(Locale.ROOT, "%s,%s,%d,%.3f,%d,%d\n",
                    r.algorithm(), r.input(), r.n(), r.timeMs(), r.comparisons(), r.maxDepth()));
        }
        try {
            Files.writeString(path, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
