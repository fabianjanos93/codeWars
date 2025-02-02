package org.example;

public class Immortal {

  public static final int SIDE_LENGTH = 200;
  /**
   * set true to enable debug
   */
  static boolean debug = false;
  public static long mod = 0;

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    System.out.println(elderAge(SIDE_LENGTH, SIDE_LENGTH, 0, 1000007));
//    System.out.println(elderAge(54500, 43500, 342, 1000007)); // result: 808451
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
  }

  static long elderAge(long n, long m, long k, long newp) {
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
        String binaryString = Long.toBinaryString(cellValue);
//        System.out.print(String.format("%2d", cellValue) + " ");
        System.out.print(" ".repeat(8 - binaryString.length()) + binaryString + "(" + String.format("%2d", cellValue) + ") ");
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
    for (int i = 0 ; i < SIDE_LENGTH - 1  ; i++) {
      long value = colsum[i];
      String binaryStringValue = Long.toBinaryString(value);
      String binaryStringValuePadding = " ".repeat(30 - binaryStringValue.length()) + Long.toBinaryString(value);
      sumByColSoFar += value;
      String binaryStringSum = Long.toBinaryString(sumByColSoFar);
      String binaryStringSumPadding = " ".repeat(30 - binaryStringSum.length()) + Long.toBinaryString(sumByColSoFar);
      long increment = colsum[i + 1] - value;
      System.out.println(
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

  long calculateTriangleSumByLocalMax(long max) {
    Long.toBinaryString(max+1).length();
    return 0;
  }

  public long sudoRecursiveStep(long localMax, long n, long m, long previousSum) {
    long rowMax = localMax * 2;
    long colMax = localMax * 2 - 1;
    if (rowMax > n || colMax > m) {
//      TODO: SMOL GAIN
      return -1;
    }
    return 0;
  }

}
