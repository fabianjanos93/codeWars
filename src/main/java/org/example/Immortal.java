package org.example;

public class Immortal {

  /**
   * set true to enable debug
   */
  static boolean debug = false;
  public static long mod = 0;
  public static final int SIDE_LENGTH = 40;
  public static final int K = 0;

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    System.out.println(elderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));
//    System.out.println(elderAge(54500, 43500, 342, 1000007)); // result: 808451
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
    System.out.println(elderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));

    System.out.println(highestPowerOfTwoLessThan(100));
  }

  static long elderAge(long n, long m, long k, long newp) {
    mod = newp;
    long sum = 0;

    // n is always the longer side
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }

    long upperTriangleSizeLimit = highestPowerOfTwoLessThan(m);
    sum += (calculateTriangleSumByLocalMax(upperTriangleSizeLimit - 1, k) << 1) % newp;
    return sum;
  }


  static long oldElderAge(long n, long m, long k, long newp) {
    mod = newp;
    long sum = 0;
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }
    long[] colsum = new long[SIDE_LENGTH];
    for (long col = 0; col < m; col++) {
      for (long row = col + 1; row < n; row++) {
        long cellValue = (col ^ row) - k;
        cellValue = cellValue >= 0 ? cellValue : 0;
        String binaryString = Long.toBinaryString(cellValue);
//        System.out.print(String.format("%2d", cellValue) + " ");
        System.out.print(
            " ".repeat(8 - binaryString.length()) + binaryString + "(" + String.format("%2d",
                cellValue) + ") ");
        colsum[(int) row] += cellValue;
        if (cellValue > 0) {
          if (row < m) {
            cellValue = cellValue << 1;
          }
          sum += cellValue;
        }
      }
      System.out.println();
      System.out.print("             ".repeat((int) col + 1));
    }

    // Print values
    long sumByColSoFar = 0;
    for (int i = 0; i < SIDE_LENGTH - 1; i++) {
      long value = colsum[i];
      String binaryStringValue = Long.toBinaryString(value);
      String binaryStringValuePadding =
          " ".repeat(30 - binaryStringValue.length()) + Long.toBinaryString(value);
      sumByColSoFar += value;
      String binaryStringSum = Long.toBinaryString(sumByColSoFar);
      String binaryStringSumPadding =
          " ".repeat(30 - binaryStringSum.length()) + Long.toBinaryString(sumByColSoFar);
      long increment = colsum[i + 1] - value;
      System.out.println(
          String.format("%3d", i) +
              String.format("%10d", value) +
              "(binary: " + binaryStringValuePadding +
              "  to Next: " + String.format("%15d", increment) +
              ")[SUM: " + String.format("%15d", sumByColSoFar) +
              "(" + binaryStringSumPadding + ")]");
    }
    return sum % newp; // do it!
  }

  static long elderAge2(long n, long m, long k, long newp) {
    long sum = 0;
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }
    return 0;
  }

  static long calculateTriangleSumByLocalMax(long max, long k) {
    int n = Long.toBinaryString(max).length();
    int binaryLength = ((n - 1) * 3) + 1;
    long baseTriangleSum = Long.parseLong("1".repeat(n) + "0".repeat(binaryLength - n), 2);

    long repetition = (max + 1) / 2;
    for (int i = 1; i < k; i++) {
      baseTriangleSum -= i * repetition;
    }
    baseTriangleSum -= k * repetition * (max - k + 1);
    return baseTriangleSum;
  }

  public static int highestPowerOfTwoLessThan(int N) {
    if (N < 1) {
      return 0; // Edge case
    }
    N |= (N >> 1);
    N |= (N >> 2);
    N |= (N >> 4);
    N |= (N >> 8);
    N |= (N >> 16);
    return N - (N >> 1);
  }
}
