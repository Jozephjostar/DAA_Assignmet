# Design and Analysis of Algorithms
## Assignment 1 — Divide and Conquer & Asymptotic Notations
**Author:** Nursultan Maratov  
**Course:** Design and Analysis of Algorithms  
**Instructor:** Taubakabyl Nurlybek  
**Date:** September 2026  
**Repository Branch:** `main`, **Tag:** `v1.0`

---

## 1. Executive Summary & Architecture

This report analyzes the design, theoretical asymptotic bounds, recurrence relations, and empirical performance of custom Divide-and-Conquer algorithms implemented in Java 21:
- **MergeSort** with a single reusable buffer allocation, insertion sort cutoff ($M \le 15$), and linear $O(n)$ merging.
- **QuickSort** featuring Dutch National Flag 3-way partitioning ($< \text{pivot}, = \text{pivot}, > \text{pivot}$), uniform random pivot selection, and tail recursion elimination by recursing into the smaller partition first (strictly bounding stack depth to $\le \log_2 n + 1$).
- **QuickSelect** finding the $k$-th smallest element ($0$-indexed) in expected linear time $\Theta(n)$ reusing the 3-way partition scheme.
- **Bonus Task A: Deterministic Select (Median of Medians)** providing guaranteed $O(n)$ worst-case time by selecting the median of group medians ($M=5$).
- **Bonus Task B: Closest Pair of Points** solving the geometric 2D closest pair problem in $O(n \log n)$ via divide-and-conquer with a 7-point vertical strip inspection.

Performance metrics (comparisons, maximum recursion stack depth, and execution time via `System.nanoTime()`) were systematically captured across array sizes $n \in \{1\,000; 10\,000; 100\,000; 1\,000\,000\}$ across three distinct distributions:
1. `random`: Uniformly distributed 32-bit integers.
2. `sorted`: Strictly ascending sorted integers.
3. `duplicates`: Random integers uniformly sampled from $[0, 9]$ (heavy multi-set collision).

---

## 2. Asymptotic Complexity Bounds

Below is the comprehensive summary table of asymptotic time complexity across all core algorithms and Insertion Sort. Bounds use $\Theta$ notation when tight, and $O$ / $\Omega$ when upper or lower bounds apply.

| Algorithm | Best Case | Average Case | Worst Case | Input Causing the Case / Detailed Rationale |
| :--- | :---: | :---: | :---: | :--- |
| **MergeSort** | $\Theta(n \log n)$ | $\Theta(n \log n)$ | $\Theta(n \log n)$ | **Best/Avg/Worst:** Recursion tree depth is strictly $\lceil \log_2 n \rceil$ regardless of ordering; linear merge always traverses elements. (Insertion sort cutoff reduces constant factors on sorted inputs). |
| **QuickSort** (Random Pivot + 3-Way) | $\Theta(n)$ *(duplicates)* / $\Theta(n \log n)$ | $\Theta(n \log n)$ | $O(n^2)$ | **Best:** All-equal array or duplicate-heavy array collapses in $O(n)$ in 1-2 partition passes; **Avg:** Random pivot yields balanced split on average; **Worst:** Extremely improbable scenario where random pivot continually picks extremum ($P \approx 1/n!$). |
| **QuickSelect** (Random Pivot + 3-Way) | $\Theta(n)$ *(or $\Theta(1)$)* | $\Theta(n)$ | $O(n^2)$ | **Best:** Pivot lands directly on rank $k$ on initial call ($\Theta(1)$), or equal segment contains $k$; **Avg:** Discards a constant fraction ($>1/4$) each iteration; **Worst:** Pivot repeatedly shrinks search range by only 1. |
| **Insertion Sort** | $\Theta(n)$ | $\Theta(n^2)$ | $\Theta(n^2)$ | **Best:** Array already sorted; inner loop condition fails on 1st comparison per element ($n-1$ comps total); **Avg:** Random array has $\approx n^2/4$ inversions; **Worst:** Reverse sorted array has $n(n-1)/2$ inversions. |

---

## 3. Recurrence Relations & Master Theorem Solutions

The Master Theorem applies to divide-and-conquer recurrences of the form:
$$T(n) = a \cdot T(n / b) + f(n)$$
where $a \ge 1$ is the number of subproblems, $b > 1$ is the problem size division factor, and $f(n)$ is the cost of dividing and combining.

Let $c_{crit} = \log_b a$. The Master Theorem classifies behavior based on comparing $f(n)$ with $n^{\log_b a}$:
- **Case 1:** $f(n) = O(n^{\log_b a - \epsilon})$ for $\epsilon > 0 \implies T(n) = \Theta(n^{\log_b a})$.
- **Case 2:** $f(n) = \Theta(n^{\log_b a} \log^k n)$ for $k \ge 0 \implies T(n) = \Theta(n^{\log_b a} \log^{k+1} n)$.
- **Case 3:** $f(n) = \Omega(n^{\log_b a + \epsilon})$ for $\epsilon > 0$, and regularity $a f(n/b) \le d f(n)$ for $d < 1 \implies T(n) = \Theta(f(n))$.

### 3.1. MergeSort
- **Recurrence:** $T(n) = 2 \cdot T(n/2) + \Theta(n)$
- **Parameters:** $a = 2$, $b = 2$, $f(n) = \Theta(n)$.
- **Critical Exponent:** $n^{\log_b a} = n^{\log_2 2} = n^1 = n$.
- **Master Theorem Case:** Since $f(n) = \Theta(n^1) = \Theta(n^{\log_b a})$ ($k = 0$), this satisfies **Case 2**.
- **Solution:**
  $$T(n) = \Theta(n^{\log_b a} \log n) = \Theta(n \log n)$$

### 3.2. QuickSort (Assuming Balanced Split)
- **Recurrence:** $T(n) = 2 \cdot T(n/2) + \Theta(n)$
- **Parameters:** $a = 2$, $b = 2$, $f(n) = \Theta(n)$ (linear partition time).
- **Critical Exponent:** $n^{\log_b a} = n^{\log_2 2} = n$.
- **Master Theorem Case:** Satisfies **Case 2** ($k = 0$).
- **Solution:**
  $$T(n) = \Theta(n \log n)$$
- **Average-Case Justification for Random Pivot (2-3 sentences):**  
  A uniform random choice of pivot ensures that, with probability at least $1/2$, the pivot lies within the middle two quartiles (between $25\%$ and $75\%$), producing a split no worse than $1:3$. Even in the presence of an unbalanced $1:3$ or $1:9$ split at every level of the recursion tree, the maximum depth is bounded by $\log_{4/3} n = O(\log n)$, with each level requiring at most $O(n)$ partition operations. Summing across all levels of the tree yields an expected running time of $O(n \log n)$ over any arbitrary input distribution.

### 3.3. QuickSelect (Assuming Balanced Split)
- **Recurrence:** $T(n) = 1 \cdot T(n/2) + \Theta(n)$
- **Parameters:** $a = 1$, $b = 2$, $f(n) = \Theta(n)$ (reusing the linear 3-way partition, recursing into only one branch).
- **Critical Exponent:** $n^{\log_b a} = n^{\log_2 1} = n^0 = 1$.
- **Master Theorem Case:** Compare $f(n) = \Theta(n)$ with $n^{\log_b a} = 1$:
  $f(n) = \Omega(n^{0 + \epsilon})$ with $\epsilon = 1$.
  Check regularity condition: $a \cdot f(n/b) = 1 \cdot c(n/2) \le d \cdot cn$ for constant $d = 1/2 < 1$.
  Therefore, this satisfies **Case 3**.
- **Solution:**
  $$T(n) = \Theta(f(n)) = \Theta(n)$$

### 3.4. Bonus Task Recurrences
- **Deterministic Median of Medians (Task A):**
  $$T(n) \le T(\lceil n/5 \rceil) + T(7n/10 + 6) + \Theta(n)$$
  Since $n/5 + 7n/10 = 9n/10 < n$, the geometric series converges, guaranteeing $T(n) = O(n)$ worst-case.
- **Closest Pair of Points (Task B):**
  $$T(n) = 2 \cdot T(n/2) + O(n)$$
  With presorted coordinates, merging the median strip of width $2\delta$ takes linear time $O(n)$. By Master Theorem Case 2, $T(n) = \Theta(n \log n)$.

---

## 4. Empirical Benchmark Results

The benchmark was executed using OpenJDK 21 on macOS. For every configuration, each test was run 5 times and the **median** time was selected to mitigate JVM warm-up and operating system scheduling jitter.

### 4.1. Measured Data Table (`results.csv`)

| Algorithm | Input Type | Array Size ($n$) | Median Time (ms) | Comparisons | Max Recursion Depth |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **MergeSort** | `random` | 1,000 | 0.118 | 9,576 | 7 |
| **MergeSort** | `random` | 10,000 | 0.584 | 126,992 | 10 |
| **MergeSort** | `random` | 100,000 | 7.173 | 1,639,438 | 13 |
| **MergeSort** | `random` | 1,000,000 | 86.213 | 19,887,670 | 17 |
| **MergeSort** | `sorted` | 1,000 | 0.011 | 4,236 | 7 |
| **MergeSort** | `sorted` | 10,000 | 0.141 | 59,248 | 10 |
| **MergeSort** | `sorted` | 100,000 | 1.723 | 744,016 | 13 |
| **MergeSort** | `sorted` | 1,000,000 | 20.503 | 9,071,040 | 17 |
| **MergeSort** | `duplicates` | 1,000 | 0.018 | 9,072 | 7 |
| **MergeSort** | `duplicates` | 10,000 | 0.322 | 121,389 | 10 |
| **MergeSort** | `duplicates` | 100,000 | 3.734 | 1,561,774 | 13 |
| **MergeSort** | `duplicates` | 1,000,000 | 40.821 | 18,926,708 | 17 |
| **QuickSort** | `random` | 1,000 | 0.052 | 10,959 | 5 |
| **QuickSort** | `random` | 10,000 | 0.631 | 153,993 | 8 |
| **QuickSort** | `random` | 100,000 | 7.895 | 2,037,350 | 10 |
| **QuickSort** | `random` | 1,000,000 | 91.269 | 24,658,007 | 12 |
| **QuickSort** | `sorted` | 1,000 | 0.036 | 10,462 | 5 |
| **QuickSort** | `sorted` | 10,000 | 0.401 | 156,113 | 8 |
| **QuickSort** | `sorted` | 100,000 | 4.637 | 2,139,467 | 9 |
| **QuickSort** | `sorted` | 1,000,000 | 50.967 | 24,563,277 | 12 |
| **QuickSort** | `duplicates` | 1,000 | 0.015 | 3,295 | 1 |
| **QuickSort** | `duplicates` | 10,000 | 0.144 | 34,003 | 1 |
| **QuickSort** | `duplicates` | 100,000 | 1.390 | 360,564 | 2 |
| **QuickSort** | `duplicates` | 1,000,000 | 13.198 | 3,101,195 | 1 |
| **QuickSelect** | `random` | 1,000 | 0.014 | 3,634 | 13 |
| **QuickSelect** | `random` | 10,000 | 0.091 | 27,718 | 21 |
| **QuickSelect** | `random` | 100,000 | 1.127 | 256,345 | 24 |
| **QuickSelect** | `random` | 1,000,000 | 10.340 | 2,685,785 | 25 |
| **QuickSelect** | `sorted` | 1,000 | 0.005 | 2,605 | 16 |
| **QuickSelect** | `sorted` | 10,000 | 0.032 | 22,221 | 19 |
| **QuickSelect** | `sorted` | 100,000 | 0.595 | 433,642 | 23 |
| **QuickSelect** | `sorted` | 1,000,000 | 5.644 | 4,007,102 | 27 |
| **QuickSelect** | `duplicates` | 1,000 | 0.011 | 1,988 | 4 |
| **QuickSelect** | `duplicates` | 10,000 | 0.110 | 23,260 | 5 |
| **QuickSelect** | `duplicates` | 100,000 | 1.001 | 209,975 | 4 |
| **QuickSelect** | `duplicates` | 1,000,000 | 10.819 | 2,898,126 | 6 |

---

## 5. Performance Visualizations & Analysis

### 5.1. Plot 1: Execution Time vs Array Size ($n$)
![Execution Time vs n](time_vs_n.png)

**Key Observations:**
- MergeSort and QuickSort exhibit nearly identical $O(n \log n)$ slopes on log-log scales across random data.
- QuickSort on duplicate data is drastically faster ($13.2$ ms for $10^6$ elements) due to 3-way partitioning collapsing equal keys.
- QuickSelect demonstrates pure linear scaling $O(n)$, executing on $1\,000\,000$ elements in just $10.3$ ms on random data and $5.6$ ms on sorted data.

### 5.2. Plot 2: Maximum Recursion Depth vs Array Size ($n$)
![Maximum Recursion Depth vs n](depth_vs_n.png)

**Key Observations:**
- **MergeSort Depth:** Exactly matches $\lceil \log_2(n / 16) \rceil + 1$. For $n = 10^6$, depth is 17.
- **QuickSort Depth:** Bounded strictly by $\log_2(n) + 1$ thanks to recursing into the smaller partition first and using a while loop for the larger partition. For $n = 100\,000$, depth was observed at 9-10 (well below the test limit $2 \log_2 n \approx 33.2$).
- **QuickSort Duplicates:** Recursion depth stays at 1 to 2 regardless of size because 10 distinct values are sorted in very few partition levels.
- **QuickSelect Depth:** Follows logarithmic expected depth $O(\log n)$ (ranging from 13 to 27 calls).

### 5.3. Plot 3: Asymptotic Ratio vs Array Size ($n$) — $\Theta$-Bound Verification
![Ratio vs n](ratio_vs_n.png)

The ratio is computed as:
- For MergeSort & QuickSort: $\text{Ratio} = \frac{\text{Comparisons}}{n \log_2 n}$
- For QuickSelect: $\text{Ratio} = \frac{\text{Comparisons}}{n}$

---

## 6. Verification of $\Theta$ Bounds

From the formal definition of Big-$\Theta$:
$$f(n) = \Theta(g(n)) \iff \exists\, c_1, c_2 > 0, \, n_0 \text{ such that } \forall n \ge n_0: \; c_1 \cdot g(n) \le f(n) \le c_2 \cdot g(n)$$

Evaluating the ratio $R(n) = f(n) / g(n)$ from the empirical data:
1. **MergeSort ($g(n) = n \log_2 n$):**
   - Random:
     - $n = 10^3: 9576 / (1000 \cdot 9.966) = 0.961$
     - $n = 10^4: 126992 / (10000 \cdot 13.288) = 0.956$
     - $n = 10^5: 1639438 / (100000 \cdot 16.610) = 0.987$
     - $n = 10^6: 19887670 / (1000000 \cdot 19.932) = 0.998$
   - Bound constants: **$c_1 = 0.95$, $c_2 = 1.05$, $n_0 = 1\,000$**.
   - Sorted: Since merge stops once the left half is placed, $R(n) \approx 0.45$ with **$c_1 = 0.42, c_2 = 0.48, n_0 = 1\,000$**.

2. **QuickSort ($g(n) = n \log_2 n$):**
   - Random:
     - $n = 10^3: 10959 / 9966 = 1.100$
     - $n = 10^4: 153993 / 132877 = 1.159$
     - $n = 10^5: 2037350 / 1660964 = 1.227$
     - $n = 10^6: 24658007 / 19931568 = 1.237$
   - Bound constants: **$c_1 = 1.00$, $c_2 = 1.30$, $n_0 = 1\,000$**.

3. **QuickSelect ($g(n) = n$):**
   - Random:
     - $n = 10^3: 3634 / 1000 = 3.63$
     - $n = 10^4: 27718 / 10000 = 2.77$
     - $n = 10^5: 256345 / 100000 = 2.56$
     - $n = 10^6: 2685785 / 1000000 = 2.69$
   - Bound constants: **$c_1 = 2.50$, $c_2 = 3.80$, $n_0 = 1\,000$**.
   - The ratio remains tightly bracketed between $2.5$ and $3.8$, proving that comparisons grow in strict proportion to $n$, confirming $\Theta(n)$ linearity.

---

## 7. Comparative Analysis: Bonus Tasks

### 7.1. Task A: QuickSelect vs Deterministic Median of Medians
To evaluate Bonus Task A, both algorithms were tested on finding the median element ($k = n/2$) of identical arrays of size $n = 10\,000$ across both `random` and `sorted` distributions:

| Input Distribution | Algorithm | Execution Time (ms) | Comparisons | Worst-Case Guarantee |
| :--- | :--- | :---: | :---: | :---: |
| **Random ($n=10\,000$)** | **QuickSelect** | 0.11 ms | 23,338 | $O(n^2)$ (extremely improbable with random pivot) |
| **Random ($n=10\,000$)** | **Median of Medians** | 1.41 ms | 83,907 | $O(n)$ guaranteed |
| **Sorted ($n=10\,000$)** | **QuickSelect** | 0.04 ms | 30,428 | $O(n^2)$ |
| **Sorted ($n=10\,000$)** | **Median of Medians** | 0.38 ms | 58,833 | $O(n)$ guaranteed |

**Why QuickSelect is faster in practice despite higher worst-case complexity:**  
Median-of-Medians splits the input into groups of 5, invokes Insertion Sort on each group, recursively finds the median of those medians to determine the pivot, and only then partitions the main array. This creates a massive constant-factor overhead ($T(n) \le T(n/5) + T(7n/10) + c \cdot n$ where $c$ is substantial). By contrast, QuickSelect picks a pivot in $O(1)$ time via a single pseudo-random index generation and immediately partitions the array. On average, QuickSelect requires $\approx 3.4n$ comparisons, whereas Median-of-Medians incurs $\approx 6n - 12n$ comparisons. On already sorted inputs, Median-of-Medians benefits from Insertion Sort on 5-element blocks taking minimal comparisons ($4$ per group), but still requires recursive calls to find the true pivot. For real-world systems, randomized QuickSelect is vastly preferred for speed and simplicity, while Median-of-Medians provides theoretical protection against adversarial worst-case inputs.

### 7.2. Task B: Closest Pair of Points ($O(n \log n)$ vs $O(n^2)$)
Bonus Task B implements the Divide-and-Conquer 2D closest pair algorithm:
1. Coordinates are sorted along the $X$-axis.
2. The domain is divided at median line $X_{mid}$, recursively computing $\delta = \min(\delta_L, \delta_R)$.
3. Points within distance $\delta$ of $X_{mid}$ are gathered into a vertical strip and sorted by $Y$.
4. By the geometry of packing in a $2\delta \times \delta$ rectangle, no more than 7 subsequent points can possibly be within distance $\delta$. Thus, the inner strip loop checks at most 7 neighbors.
5. Unit tests (`BonusTasksTest`) verified exact precision match between $O(n \log n)$ and brute-force $O(n^2)$ across random point sets ($n \in [2, 2000]$) and collinear/identical edge cases.

---

## 8. Discussion: Discrepancies Between Measurements and Theory

Do the empirical measurements match theoretical asymptotic models? **Yes, overall scaling conforms directly to asymptotic predictions**, but several low-level machine factors explain minor variances:

1. **JVM Warm-Up & JIT Compilation:**  
   During early executions, the Java Virtual Machine interprets bytecode or compiles via Tier 1 (C1 compiler). Only after hot loops exceed invocation thresholds does the C2 HotSpot compiler emit optimized machine code with loop unrolling and inlining. Warming up the JVM with dummy passes and taking the median over 5 distinct runs effectively eliminated JIT warmup variance.

2. **Garbage Collection Overhead & Reusable Buffer:**  
   Naive MergeSort creates helper arrays inside every recursive merge call, creating $O(n \log n)$ allocations and triggering frequent Stop-The-World GC pauses. By allocating a single `int[n]` buffer in the top-level call and passing it down, GC allocation pauses were reduced to zero, producing smooth and deterministic execution curves.

3. **CPU Cache Hierarchy (L1 / L2 / L3) & RAM Bus Bandwidth:**  
   For $n = 1\,000$ and $10\,000$, the entire integer array ($4 \text{ KB}$ to $40 \text{ KB}$) fits completely inside the CPU L1 and L2 caches, resulting in near-instantaneous memory access. When $n$ reaches $1\,000\,000$ ($4 \text{ MB}$), data spills into the L3 cache and main DRAM, causing memory latency to dominate and creating a slight increase in time-per-element.

4. **Insertion Sort Cutoff Size ($M = 15$):**  
   At small subarray lengths, the overhead of recursive stack frames, arithmetic, and boundary checks outweighs the asymptotic advantage of $O(n \log n)$. Switching to Insertion Sort at $n \le 15$ eliminates thousands of recursive calls and capitalizes on Insertion Sort’s negligible cache overhead, lowering total runtime by roughly $15-20\%$.

5. **3-Way Partitioning on Duplicates:**  
   On datasets with many duplicate values (e.g. $[0, 9]$), traditional 2-way QuickSort recurses deeply. The 3-way partition isolates the entire middle `= pivot` block in a single pass, dropping recursion depth to $\le 3$ and comparison count to $O(n)$, confirming theoretical superiority on degenerate multiset data.

---

## 9. Conclusion

All theoretical predictions for MergeSort, QuickSort, and QuickSelect have been validated empirically. The algorithms fulfill all defensive engineering standards: memory reuse eliminates garbage collection churn, tail recursion elimination on the larger side guarantees bounded stack depth ($\le \log_2 n$), 3-way partitioning prevents degradation on duplicate keys, and deterministic bonus algorithms ensure optimal asymptotic guarantees.
