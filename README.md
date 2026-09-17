# DAA Assignment 1 — Fast Sorting & Selection Engine

**Student:** Nursultan Maratov  
**Course:** Design and Analysis of Algorithms  
**Instructor:** Taubakabyl Nurlybek  
**GitHub Repository:** https://github.com/Jozephjostar/DAA_Assignmet  
**Branch:** `main` | **Tag:** `v1.0`

---

## Overview

This project implements and benchmarks Divide-and-Conquer algorithms in Java:
- **MergeSort**: single reusable memory buffer, insertion sort cutoff for $n \le 15$.
- **QuickSort**: 3-way partitioning (Dutch National Flag), random pivot, tail recursion elimination ($\text{depth} \le \log_2 n + 1$).
- **QuickSelect**: expected $O(n)$ selection of the $k$-th smallest element.
- **Bonus Task A (+10%)**: Deterministic Median of Medians with guaranteed $O(n)$ worst-case.
- **Bonus Task B (+5%)**: Closest Pair of Points in 2D with $O(n \log n)$ divide-and-conquer.

---

## Project Structure

```text
DAA Assignmet/
├── pom.xml                 # Maven configuration
├── build.gradle            # Gradle configuration
├── results.csv             # Benchmark data
├── depth_vs_n.png          # Recursion depth plot
├── ratio_vs_n.png          # Asymptotic ratio plot
├── time_vs_n.png           # Execution time plot
├── REPORT.md               # Assignment report
├── README.md               # Project guide
├── GITHUB_LINK.txt         # GitHub repository link
├── src/
│   ├── main/java/daa/
│   │   ├── Main.java                 # Benchmark runner entry point
│   │   ├── algorithms/               # MergeSort, QuickSort, QuickSelect, InsertionSort, Partition
│   │   ├── bench/Benchmark.java      # Benchmarking logic
│   │   ├── bonus/                    # MedianOfMediansSelect, ClosestPairOfPoints
│   │   ├── metrics/                  # Metrics, CsvWriter, Result
│   │   └── util/                     # ArrayUtils, InputType
│   └── test/java/daa/
│       ├── AllTestsRunner.java       # Standalone test runner (26 tests)
│       ├── algorithms/               # Unit tests for sorting and selection
│       └── bonus/BonusTasksTest.java # Unit tests for bonus tasks
```

---

## How to Run

### In IntelliJ IDEA
1. Open the project folder in IntelliJ IDEA.
2. **Run All Tests**:
   - Open `src/test/java/daa/AllTestsRunner.java` and click the green **Play** button next to `main()`.
   - Or select **AllTestsRunner** from the run configuration dropdown and click **Run**.
3. **Run Benchmark**:
   - Open `src/main/java/daa/Main.java` and click the green **Play** button next to `main()`.
   - Or select **Main** from the run configuration dropdown and click **Run**.
   - Results will be printed to the console and saved to `results.csv`.

### From Terminal (Gradle)
```bash
./gradlew test         # Run all unit tests via Gradle
./gradlew runAllTests  # Run AllTestsRunner (26 tests with summary)
./gradlew run          # Run benchmark (Main)
```

### From Terminal (Maven)
```bash
mvn test               # Run all unit tests
mvn compile exec:java  # Run benchmark (Main)
```

---

## Test Suites

The test suite contains **26 unit tests** covering:
- Correctness against `Arrays.sort` on hundreds of random arrays.
- Edge cases: empty arrays, single-element arrays, all-equal elements, sorted and reverse-sorted arrays.
- Recursion depth limits for QuickSort on sorted arrays ($n = 100\,000$).
- Deterministic Select vs QuickSelect comparisons.
- Closest Pair of Points verified against $O(n^2)$ brute-force.

---

## Deliverables

- [x] Clean Java implementation of all algorithms and bonuses.
- [x] 26 passing JUnit 5 unit tests.
- [x] `results.csv` with benchmark timings and comparisons for $n \in \{10^3, 10^4, 10^5, 10^6\}$.
- [x] 3 generated plots: `time_vs_n.png`, `depth_vs_n.png`, `ratio_vs_n.png`.
- [x] Detailed analysis in `REPORT.md`.
- [x] Git history with feature branches and `v1.0` tag on `main`.
.