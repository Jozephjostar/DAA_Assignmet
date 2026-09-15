# DAA Assignment 1 — Fast Sorting & Selection Engine

**Student:** Nursultan Maratov  
**Course:** Design and Analysis of Algorithms  
**Instructor:** Taubakabyl Nurlybek  
**Release:** `v1.0` (Branch: `main`)

This repository implements high-performance, memory-safe Divide-and-Conquer algorithms in Java 21, complete with an empirical benchmarking harness, automated AWT plot generation, comprehensive JUnit 5 test suites, and theoretical asymptotic analysis.

---

## Features

1. **MergeSort (`com.daa.sort.MergeSort`)**
   - **Reusable Buffer:** Helper array `int[n]` allocated once in the top-level call and passed through recursive calls (zero memory churn).
   - **Cutoff:** Subarrays with $\le 15$ elements are sorted using Insertion Sort.
   - **Linear Merge:** In-place buffer merge operating in strictly $O(n)$ time.

2. **QuickSort (`com.daa.sort.QuickSort`)**
   - **Random Pivot:** Uniform random pivot selection to prevent $O(n^2)$ worst-case degradation on sorted or reverse-sorted inputs.
   - **Smaller Side First:** Recursion into the smaller partition combined with while-loop iteration on the larger partition (tail recursion elimination), strictly bounding recursion depth to $\le \log_2(n) + 1$.
   - **3-Way Partitioning:** Dutch National Flag partition (`< pivot`, `= pivot`, `> pivot`) ensuring $O(n)$ time on heavy duplicate datasets.

3. **QuickSelect (`com.daa.select.QuickSelect`)**
   - **Partition Reuse:** Reuses the exact same 3-way partition method as QuickSort.
   - **One Side Only:** Recurses only into the subarray containing index $k$.
   - **Input Validation:** Throws descriptive `IllegalArgumentException` on null/empty arrays or out-of-bounds ranks.

4. **Metrics & Benchmark Engine (`com.daa.metrics` & `com.daa.benchmark`)**
   - `Metrics` tracks comparisons, current & maximum recursion stack depth, and elapsed nanoseconds (`System.nanoTime()`).
   - `BenchmarkRunner` runs all algorithms on $n \in \{10^3, 10^4, 10^5, 10^6\}$ across `random`, `sorted`, and `duplicates` inputs, running each case 5 times and saving the median to `results.csv`.
   - `PlotGenerator` renders high-resolution PNG charts:
     - `time_vs_n.png`
     - `depth_vs_n.png`
     - `ratio_vs_n.png`

5. **Bonus Tasks (+15%)**
   - **Task A (+10%): Deterministic Select (`MedianOfMediansSelect`)** with guaranteed $O(n)$ worst-case time using groups of 5.
   - **Task B (+5%): Closest Pair of Points in 2D (`ClosestPairOfPoints`)** using $O(n \log n)$ Divide-and-Conquer with 7-point strip check, validated against brute-force $O(n^2)$.

---

## Project Structure

```
.
├── .idea/
│   └── runConfigurations/      # Pre-configured IntelliJ IDEA Run Configurations
│       ├── All_Tests.xml       # Run all JUnit 5 tests
│       └── BenchmarkRunner.xml # Run benchmark & generate plots
├── DAA Assignmet.iml           # IntelliJ IDEA module definition
├── build.gradle                # Gradle build script (Java 21 toolchain)
├── pom.xml                     # Maven build script
├── lib/                        # Bundled standalone JUnit 5 & Jupiter jars
├── results.csv                 # Benchmark measurements
├── time_vs_n.png               # Execution time vs n plot
├── depth_vs_n.png              # Maximum recursion depth vs n plot
├── ratio_vs_n.png              # Asymptotic ratio Theta check plot
├── REPORT.md                   # Full theoretical and experimental report
├── README.md                   # Project documentation & run guide
└── src/
    ├── main/java/com/daa/
    │   ├── bonus/
    │   │   ├── ClosestPairOfPoints.java
    │   │   └── MedianOfMediansSelect.java
    │   ├── benchmark/
    │   │   ├── BenchmarkRunner.java
    │   │   └── PlotGenerator.java
    │   ├── metrics/
    │   │   └── Metrics.java
    │   ├── select/
    │   │   └── QuickSelect.java
    │   └── sort/
    │       ├── MergeSort.java
    │       └── QuickSort.java
    └── test/java/com/daa/
        ├── AllTestsRunner.java
        ├── bonus/
        │   └── BonusTasksTest.java
        ├── select/
        │   └── QuickSelectTest.java
        └── sort/
            ├── MergeSortTest.java
            └── QuickSortTest.java
```

---

## How to Open and Run in IntelliJ IDEA

1. **Open the Project:**
   - Launch **IntelliJ IDEA**.
   - Select **File -> Open...** (or click **Open** on the Welcome screen).
   - Choose the project folder:
     `/Users/nursultanmaratov/.gemini/antigravity/scratch/DAA Assignmet`
   - Click **Open**.

2. **Verify Project SDK (JDK 21):**
   - Go to **File -> Project Structure... -> Project** (or press `Cmd + ;`).
   - Under **SDK**, select **JDK 21** (e.g. JetBrains Runtime 21 or OpenJDK 21).
   - Set **Language level** to **21**.
   - Click **OK**.

3. **Run Unit Tests in IntelliJ IDEA:**
   - **Method A (Shared Run Configuration):** In the top-right toolbar, select **All Tests** from the run configuration dropdown and click the green **Play (Run)** button (or press `Ctrl + R` / `Shift + F10`).
   - **Method B (Test Runner Class):** Navigate in the Project tree to `src/test/java/com/daa/AllTestsRunner.java`, right-click on the file, and choose **Run 'AllTestsRunner.main()'**.
   - **Method C (Individual Test Suites):** Right-click any test class (such as `MergeSortTest`, `QuickSortTest`, `QuickSelectTest`, or `BonusTasksTest`) and click **Run**.
   - All 26 tests will run with detailed green checkmarks and output reports in the Run tool window.

4. **Run Benchmark & Generate Plots in IntelliJ IDEA:**
   - In the top-right toolbar, select **BenchmarkRunner** from the run configurations dropdown.
   - Click the green **Play (Run)** button (or right-click `src/main/java/com/daa/benchmark/BenchmarkRunner.java` and select **Run 'BenchmarkRunner.main()'**).
   - The benchmark will execute the 5-run median cycle for each algorithm and input type, outputting results directly to the console.
   - Upon completion, `results.csv`, `time_vs_n.png`, `depth_vs_n.png`, and `ratio_vs_n.png` are regenerated in the project root directory.

---

## Command-Line Execution (CLI)

If running from a terminal, the project can be built and tested via multiple independent methods:

### Method 1: Direct Java Compilation (Zero Dependencies, uses bundled `lib/`)
```bash
# 1. Compile main sources
javac -cp "lib/*:src/main/java" -d out/production src/main/java/com/daa/*/*.java

# 2. Compile test sources
javac -cp "lib/*:out/production:src/test/java" -d out/test src/test/java/com/daa/*.java src/test/java/com/daa/*/*.java

# 3. Run all tests
java -cp "lib/*:out/production:out/test" com.daa.AllTestsRunner

# 4. Run benchmark and update plots
java -cp "out/production" com.daa.benchmark.BenchmarkRunner
```

### Method 2: Maven
```bash
mvn clean test
mvn exec:java -Dexec.mainClass="com.daa.benchmark.BenchmarkRunner"
```

### Method 3: Gradle
```bash
./gradlew test
./gradlew run
```

---

## Deliverables Checklist

- [x] **Source Code:** Modular architecture, zero global state, safe recursion, reusable buffer, 3-way partition, random pivot, smaller-side first.
- [x] **results.csv:** Real benchmark timings and counters across all sizes and distributions.
- [x] **Plots:** `time_vs_n.png`, `depth_vs_n.png`, `ratio_vs_n.png`.
- [x] **Report:** Comprehensive `REPORT.md` including asymptotic tables, Master Theorem recurrences, ratio analysis, $c_1/c_2/n_0$ bounds, and machine-level discrepancy discussion.
- [x] **README:** Full build and IntelliJ IDEA operational instructions.
- [x] **Git Workflow:** Feature branches (`feature/mergesort`, `feature/quicksort`, `feature/select`, `feature/metrics`, `feature/bonus`), atomic conventional commits, and release tag `v1.0` on `main`.
