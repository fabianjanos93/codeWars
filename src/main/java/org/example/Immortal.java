package org.example;

public class Immortal {

  /**
   * set true to enable debug
   */
  static boolean debug = false;
  public static long mod = 0;
  public static final int SIDE_LENGTH = 700000;
  public static final int K = 0;

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
//    System.out.println(elderAge(SIDE_LENGTH, SIDE_LENGTH + 4, K, 1000007));
    System.out.println(elderAge(545000, 435000, 342, 1000007)); // result: 808451
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);

    startTime = System.currentTimeMillis();
//    System.out.println(oldElderAge(SIDE_LENGTH, SIDE_LENGTH + 4, K, 1000007));
    System.out.println(oldElderAge(545000, 435000, 342, 1000007)); // result: 808451
    estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
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

    long highestPowerOfTwo = highestPowerOfTwoLessThan(m);
    sum += ((calculateFullSquareRows(highestPowerOfTwo - 1, k) << 1) % newp);

    long perfectSquareRows = sumOfLowerRectangle(highestPowerOfTwo, (highestPowerOfTwo * 2) - 1);
    //Mirrored part
    sum += (perfectSquareRows * (m - highestPowerOfTwo) << 1) % newp;
    //Rest
    long restOfBiggestSquare = (highestPowerOfTwo * 2) - m;
    sum += perfectSquareRows * (Math.min(restOfBiggestSquare, n - m)) % newp;

    sum += remainingTriangle(highestPowerOfTwo, n, m, k, newp) << 1;

    if (restOfBiggestSquare > n - m) {
      sum += remainingBlock(restOfBiggestSquare, n, highestPowerOfTwo, k, newp);
    }
    return sum % newp;
  }


  static long remainingTriangle(long nStart, long n, long m, long k, long newp) {
    long sum = 0;
    for (long col = nStart; col < m; col++) {
      for (long row = col + 1; row < n; row++) {
        long cellValue = (col ^ row) - k;
        if (cellValue > 0) {
          if (row < m) {
            cellValue = cellValue << 1;
          }
          sum += cellValue;
        }
      }
    }
    return sum % newp; // do it!
  }

  static long remainingBlock(long nStart, long n, long mEnd, long k, long newp) {
    long sum = 0;
    for (long col = 0; col < mEnd; col++) {
      for (long row = nStart; row < n; row++) {
        long cellValue = (col ^ row) - k;
        if (cellValue > 0) {
          sum += cellValue;
        }
      }
    }
    return sum % newp; // do it!
  }

  static long oldElderAge(long n, long m, long k, long newp) {
    mod = newp;
    long sum = 0;
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }
    for (long col = 0; col < m; col++) {
      for (long row = col + 1; row < n; row++) {
        long cellValue = (col ^ row) - k;
        cellValue = cellValue >= 0 ? cellValue : 0;
//        String binaryString = Long.toBinaryString(cellValue);
//        System.out.print(
//            " ".repeat(8 - binaryString.length()) + binaryString + "(" + String.format("%2d",
//                cellValue) + ") ");
        if (cellValue > 0) {
          if (row < m) {
            cellValue = cellValue << 1;
          }
          sum += cellValue;
        }
      }
//      System.out.println();
//      System.out.print("             ".repeat((int) col + 1));
    }
    return sum % newp;
  }

  static long calculateFullSquareRows(long max, long k) {
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

  public static long sumOfLowerRectangle(long N, long M) {
    return ((M - N + 1) * (N + M)) / 2;
  }

  public static long highestPowerOfTwoLessThan(long N) {
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
