# DAA Assignment 1 Report — Divide and Conquer Algorithms

**Student:** Nursultan Maratov  
**Course:** Design and Analysis of Algorithms  
**Instructor:** Taubakabyl Nurlybek  
**GitHub Repository:** https://github.com/Jozephjostar/DAA_Assignmet  
**Branch:** `main` | **Tag:** `v1.0`

---

## 1. Overview

This report presents theoretical analysis and experimental benchmark results for:
- **MergeSort**: divide-and-conquer sorting with a single reusable buffer and insertion sort cutoff ($n \le 15$).
- **QuickSort**: randomized pivot, 3-way partitioning (Dutch National Flag), and tail recursion elimination (smaller subproblem first).
- **QuickSelect**: expected $O(n)$ selection of the $k$-th order statistic.
- **Bonus Task A (+10%)**: Deterministic Select (Median of Medians) with guaranteed $O(n)$ worst-case.
- **Bonus Task B (+5%)**: 2D Closest Pair of Points in $O(n \log n)$ time.

Metrics were collected for array sizes $n \in \{1\,000, 10\,000, 100\,000, 1\,000\,000\}$ across three input distributions:
1. `random`: uniformly distributed random integers.
2. `sorted`: integers already in ascending order.
3. `duplicates`: integers drawn from the small range $[0, 9]$.

---

## 2. Asymptotic Complexity Summary

| Algorithm | Best Case | Average Case | Worst Case | Notes / Input Behavior |
| :--- | :---: | :---: | :---: | :--- |
| **MergeSort** | $\Theta(n \log n)$ | $\Theta(n \log n)$ | $\Theta(n \log n)$ | Always divides arrays in half and performs linear merge. Cutoff speeds up small blocks. |
| **QuickSort** | $\Theta(n \log n)$ / $\Theta(n)^*$ | $\Theta(n \log n)$ | $O(n^2)$ | Random pivot avoids worst case. 3-way partitioning achieves $\Theta(n)^*$ on duplicate arrays. |
| **QuickSelect** | $\Theta(n)$ | $\Theta(n)$ | $O(n^2)$ | Recurses into only one partition branch containing rank $k$. |
| **Insertion Sort** | $\Theta(n)$ | $\Theta(n^2)$ | $\Theta(n^2)$ | Fast on nearly-sorted data; used as base-case cutoff ($n \le 15$). |

---

## 3. Recurrence Relations & Master Theorem

Master Theorem form: $T(n) = a \cdot T(n / b) + f(n)$ where $c_{crit} = \log_b a$.

### 3.1. MergeSort
- **Recurrence:** $T(n) = 2T(n/2) + \Theta(n)$
- **Parameters:** $a = 2$, $b = 2$, $f(n) = \Theta(n)$.
- **Critical exponent:** $n^{\log_2 2} = n^1 = n$.
- **Case:** Case 2 ($f(n) = \Theta(n^{\log_b a})$ with $k = 0$).
- **Solution:** $T(n) = \Theta(n \log n)$.

### 3.2. QuickSort (Average / Balanced Case)
- **Recurrence:** $T(n) = 2T(n/2) + \Theta(n)$
- **Parameters:** $a = 2$, $b = 2$, $f(n) = \Theta(n)$ (linear partition).
- **Case:** Case 2 ($k = 0$).
- **Solution:** $T(n) = \Theta(n \log n)$.
- **Justification for Random Pivot:**  
  Choosing the pivot uniformly at random ensures that the split is reasonably balanced (within the middle 50% of elements) with probability at least $1/2$. Even an unbalanced split such as $1:9$ at every level produces recursion depth bounded by $O(\log n)$, keeping the expected total running time at $O(n \log n)$ on any input.

### 3.3. QuickSelect
- **Recurrence:** $T(n) = 1 \cdot T(n/2) + \Theta(n)$
- **Parameters:** $a = 1$, $b = 2$, $f(n) = \Theta(n)$.
- **Critical exponent:** $n^{\log_2 1} = n^0 = 1$.
- **Case:** Case 3 ($f(n) = \Omega(n^{0 + 1})$ and $1 \cdot (n/2) \le d \cdot n$ for $d = 1/2 < 1$).
- **Solution:** $T(n) = \Theta(n)$.

### 3.4. Bonus Tasks Recurrences
- **Median of Medians (Task A):**  
  $$T(n) \le T(n/5) + T(7n/10) + \Theta(n)$$  
  Since $n/5 + 7n/10 = 9n/10 < n$, the recursion tree costs form a decreasing geometric series summing to $O(n)$ in the worst case.
- **Closest Pair of Points (Task B):**  
  $$T(n) = 2T(n/2) + O(n) \implies T(n) = \Theta(n \log n)$$

---

## 4. Benchmark Results

Measured on macOS (median of 5 runs per configuration):

| Algorithm | Input Type | Array Size ($n$) | Time (ms) | Comparisons | Max Depth |
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

## 5. Visualizations & Observations

### 5.1. Execution Time vs $n$
![Execution Time vs n](time_vs_n.png)

- **MergeSort & QuickSort**: Follow $O(n \log n)$ slopes on random data ($\approx 86$ ms and $\approx 91$ ms for $n = 10^6$).
- **QuickSort on Duplicates**: Extremely fast ($13.2$ ms for $10^6$ elements) because 3-way partitioning collapses equal keys in one pass.
- **QuickSelect**: Exhibits linear $O(n)$ scaling ($10.3$ ms for $n = 10^6$).

### 5.2. Recursion Depth vs $n$
![Maximum Recursion Depth vs n](depth_vs_n.png)

- **MergeSort**: Exact logarithmic depth $\lceil \log_2(n / 16) \rceil + 1$ (depth 17 for $n = 10^6$).
- **QuickSort**: Strictly bounded to $\le \log_2 n + 1$ by recursing into the smaller partition first (depth $\le 12$ for $n = 10^6$, and only $1-2$ on duplicates).
- **QuickSelect**: Expected depth $O(\log n)$ (13 to 27 calls).

### 5.3. Asymptotic Ratio vs $n$
![Ratio vs n](ratio_vs_n.png)

Ratios verify theoretical bounds:
- MergeSort: $\frac{\text{Comparisons}}{n \log_2 n} \approx 0.96 - 1.00$
- QuickSort: $\frac{\text{Comparisons}}{n \log_2 n} \approx 1.10 - 1.24$
- QuickSelect: $\frac{\text{Comparisons}}{n} \approx 2.56 - 3.63$

---

## 6. Verification of $\Theta$-Bounds

For constants $c_1 \le \frac{f(n)}{g(n)} \le c_2$ with $n_0 = 1\,000$:

1. **MergeSort** ($g(n) = n \log_2 n$):
   - $c_1 = 0.95$, $c_2 = 1.05$, $n_0 = 1\,000$.
2. **QuickSort** ($g(n) = n \log_2 n$):
   - $c_1 = 1.00$, $c_2 = 1.30$, $n_0 = 1\,000$.
3. **QuickSelect** ($g(n) = n$):
   - $c_1 = 2.50$, $c_2 = 3.80$, $n_0 = 1\,000$.

The ratio curves flatten as $n$ increases, directly confirming Big-$\Theta$ bounds.

---

## 7. Bonus Tasks Analysis

### 7.1. Task A: QuickSelect vs Median of Medians ($n = 10\,000$)

| Distribution | Algorithm | Time (ms) | Comparisons | Worst-Case Guarantee |
| :--- | :--- | :---: | :---: | :---: |
| **Random** | **QuickSelect** | 0.11 ms | 23,338 | $O(n^2)$ |
| **Random** | **Median of Medians** | 1.41 ms | 83,907 | $O(n)$ guaranteed |
| **Sorted** | **QuickSelect** | 0.04 ms | 30,428 | $O(n^2)$ |
| **Sorted** | **Median of Medians** | 0.38 ms | 58,833 | $O(n)$ guaranteed |

**Why QuickSelect is faster in practice:**  
Median of Medians guarantees $O(n)$ worst case by dividing elements into groups of 5, sorting each group, and recursively finding the median of medians. This introduces high constant factors ($T(n) \le T(n/5) + T(7n/10) + c \cdot n$).  
In contrast, QuickSelect chooses a random pivot in $O(1)$ time and averages $\approx 3.4n$ comparisons, making it $5-10\times$ faster in practice.

### 7.2. Task B: Closest Pair of Points ($O(n \log n)$)
1. Points are sorted by $X$ coordinate.
2. The set is split at $X_{mid}$, computing $\delta = \min(\delta_L, \delta_R)$.
3. Points within $\delta$ of the midline form a vertical strip sorted by $Y$.
4. **Why checking 7 points suffices:** Within a $2\delta \times \delta$ box, points on each side must be at least $\delta$ apart from each other. By geometric packing, at most 8 points can fit in this region. Therefore, for each point, checking at most 7 subsequent points in $Y$-order guarantees correctness while maintaining $O(n)$ strip merging.
5. Unit tests confirmed exact distance matches against $O(n^2)$ brute-force.

---

## 8. Practical Engineering Decisions

1. **Single Reusable Buffer in MergeSort:** Allocating `int[n]` once in the entry call avoids $O(n \log n)$ temporary array allocations and eliminates Garbage Collector pauses.
2. **Insertion Sort Cutoff ($M = 15$):** For small subproblems ($n \le 15$), Insertion Sort is faster than recursive overhead, speeding up MergeSort by $\approx 15-20\%$.
3. **Tail Recursion Elimination in QuickSort:** Recursing into the smaller partition and looping over the larger one guarantees stack depth $\le \log_2 n + 1$, preventing stack overflow.
4. **3-Way Partitioning:** Elements equal to the pivot are placed in the middle and excluded from recursion, resulting in linear $O(n)$ time on duplicate-heavy inputs.

---

## 9. Conclusion

Theoretical bounds for MergeSort ($\Theta(n \log n)$), QuickSort ($\Theta(n \log n)$), and QuickSelect ($\Theta(n)$) have been confirmed empirically. Memory reuse, bounded recursion depth, and 3-way partitioning ensure robust and optimal real-world performance.
.