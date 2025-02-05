package org.example;

public class Immortal {

  /**
   * set true to enable debug
   */
  static boolean debug = false;
  public static long mod = 0;
  public static final int SIDE_LENGTH = 2;
  public static final int K = 0;

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
//    // System.out.println(elderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));
//    // System.out.println(elderAge(545, 435, 342, 1000007)); // result: 808451
    System.out.println(elderAge(120, 254, 2, 10000000)); //result : 5
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);

    startTime = System.currentTimeMillis();
//    // System.out.println(oldElderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));
//    // System.out.println(oldElderAge(545, 435, 342, 1000007)); // result: 808451
    System.out.println(oldElderAge(120, 254, 2, 10000000)); //result : 5

    // "TeStS:

//    // System.out.println(elderAge(12, 1, 0, 100000) == oldElderAge(12, 1, 0, 100000));
//    // System.out.println(elderAge(12, 2, 0, 100000) == oldElderAge(12, 2, 0, 100000));
//    // System.out.println(elderAge(12, 3, 0, 100000) == oldElderAge(12, 3, 0, 100000));
//    // System.out.println(elderAge(12, 4, 0, 100000) == oldElderAge(12, 4, 0, 100000));
//    // System.out.println(elderAge(12, 5, 0, 100000) == oldElderAge(12, 5, 0, 100000));
//    // System.out.println(elderAge(12, 6, 0, 100000) == oldElderAge(12, 6, 0, 100000));
//    // System.out.println(elderAge(12, 7, 0, 100000) == oldElderAge(12, 7, 0, 100000));
//    // System.out.println(elderAge(12, 8, 0, 100000) == oldElderAge(12, 8, 0, 100000));
//    // System.out.println(elderAge(12, 9, 0, 100000) == oldElderAge(12, 9, 0, 100000));
//    // System.out.println(elderAge(12, 10, 0, 100000) == oldElderAge(12,10, 0, 100000));
//    // System.out.println(elderAge(12, 11, 0, 100000) == oldElderAge(12,11, 0, 100000));
//    // System.out.println(elderAge(12, 10, 0, 100000) == oldElderAge(12,10, 0, 100000));

    estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
  }

  static long elderAge(long n, long m, long k, long newp) {
    long sum = 0;

    // n is always the longer side
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }

    for (long i = 1; i <= m; i++) {

      long startValue = nextPowerOfTwo(i);
      long endValueN = i;
      long endValueM = i;
      boolean set = false;

      // First Section
      long nextPowerOfTwo = nextPowerOfTwo(i);
      long remainingSmallerBlocks = nextPowerOfTwo - i;
      while (remainingSmallerBlocks > 0) {
        if (i + remainingSmallerBlocks > n) {
          break;
        }
        long previousPowerOfTwo = highestPowerOfTwoLessThan(remainingSmallerBlocks);
        long sum1 = sumOfFullPowerBlock(previousPowerOfTwo, (previousPowerOfTwo * 2) - 1, k, newp);
        // System.out.println(i + " " + m + " " + remainingSmallerBlocks);
        if (i + remainingSmallerBlocks <= m) {
          sum1 = sum1 << 1;
          if (!set) {
            endValueM = i + remainingSmallerBlocks;
            // System.out.println(endValueM);
            set = true;
          }
        }
        sum += sum1 % newp;
        // System.out.println(
//            i + " first section: " + sum1 + "(from: " + previousPowerOfTwo + ", to: " + (
//                (previousPowerOfTwo * 2) - 1) + ")");
        remainingSmallerBlocks -= previousPowerOfTwo;
      }

      // Second Section
      while (nextPowerOfTwo(endValueN + 1) <= n) {
        endValueN = nextPowerOfTwo(endValueN + 1);
        if (nextPowerOfTwo(endValueM + 1) <= m - 1) {
          endValueM = nextPowerOfTwo(endValueM + 1);
        }
      }
      if (startValue < endValueN) {
        long sum1 = sumOfFullPowerBlock(startValue, endValueN - 1, k, newp);
        long sum2 = sumOfFullPowerBlock(startValue, endValueM - 1, k, newp);
//        // System.out.println(i + " | " + sum1 + " | " + sum2);
        sum += sum1;
        // System.out.println(i + " second section sum1 above: " + sum1);
        sum += sum2;
        // System.out.println(i + " second section sum2 below: " + sum2);
      }

      // Third Section
      //Bottom
      // System.out.println("endM: " + endValueM);
      for (long j = endValueM; j < m; j++) {
        long cellValue = (i - 1 ^ j) - k;
        if (cellValue > 0) {
          sum += cellValue;
          // System.out.println(i + "|" + j + " cell from rest bottom: " + cellValue);
        }
      }

      //Top
      for (long j = endValueN; j < n; j++) {
        long cellValue = (i - 1 ^ j) - k;
        if (cellValue > 0) {
          sum += cellValue;
          // System.out.println(i + "|" + j + " cell from rest top: " + cellValue);
        }
      }
    }
    return sum % newp;
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
      for (long row = 0; row < n; row++) {
        long cellValue = (col ^ row) - k;
        cellValue = cellValue >= 0 ? cellValue : 0;
        String binaryString = Long.toBinaryString(cellValue);
//        System.out.print(
//            " ".repeat(8 - binaryString.length()) + binaryString + "(" + String.format("%2d",
//                cellValue) + ") ");
        if (cellValue > 0) {
//          if (row < m) {
//            cellValue = cellValue << 1;
//          }
          sum += cellValue;
        }
      }
      // System.out.println();
//      System.out.print("             ".repeat((int) col + 1));
    }
    // System.out.println();
    return sum % newp;
  }

  public static long sumOfFullPowerBlock(long n, long m, long k, long newp) {
    m = m - k;
    if (m < 1) {
      return 0;
    }
    n = n - k < 1 ? 1 : n - k;
    return (((m - n + 1) * (n + m)) / 2) % newp;
  }

  public static long highestPowerOfTwoLessThan(long n) {
    if (n < 1) {
      return 0; // Edge case
    }
    n |= (n >> 1);
    n |= (n >> 2);
    n |= (n >> 4);
    n |= (n >> 8);
    n |= (n >> 16);
    return n - (n >> 1);
  }

  public static long nextPowerOfTwo(long n) {

    long previousPowerOfTwo = highestPowerOfTwoLessThan(n);
    return previousPowerOfTwo << (n == previousPowerOfTwo ? 0 : 1);
  }
}
