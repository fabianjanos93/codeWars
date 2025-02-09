package org.example;

import java.util.HashMap;

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
    System.out.println(elderAge(16, 16, 0, 10000000)); //result : 5
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);

    startTime = System.currentTimeMillis();
//    // System.out.println(oldElderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));
    System.out.println(oldElderAge(16, 16, 0, 10000000)); //result : 5

    estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
  }


  static long elderAge(long n, long m, long k, long newp) {
    // n is always the bigger side
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }
    long sum = 0;
    long width = n-1;
    long height = m-1;
    long offset = 0;

    long nSide = highestPowerOfTwoLessThan(n - 1);
    long mSide = highestPowerOfTwoLessThan(m - 1);
    long blockSize = Math.min(nSide, mSide);
    while (height > 0) {
      System.out.println("width: " + width + " | height: " + height);
      sum += stage0(width, height, k, newp, blockSize, offset) % newp;
      height -= blockSize;
      width -= blockSize;
      offset += blockSize;
      blockSize = highestPowerOfTwoLessThan(height);
    }
    return sum;
  }

  private static long stage0(long n, long m, long k, long newp, long blockSize, long offset) {
    long sum = 0;
    sum += stage1(n, m, k, newp) % newp;
    sum += stage1(m, m, k, newp) % newp;

    sum += stage2(n, k, newp, blockSize, offset);
    sum += stage2(m, k, newp, blockSize, offset);

    return sum;
  }

  private static long stage1(long n, long m, long k, long newp) {
    long nSideLength = 1;
    long totalLength = 0;
    long sum = 0;
    while (totalLength + nSideLength <= n && totalLength + nSideLength <= m) {
      long multiplier = Math.min(nSideLength, n-nSideLength + 1);
      System.out.println("Stage 1: nSideLength: " + nSideLength + " multiplier: " + multiplier);
      sum += recursiveStep(nSideLength, multiplier, k, newp);
      totalLength += nSideLength;
      nSideLength <<= 1;
    }
    return sum;
  }

  private static long stage2(long n, long k, long newp, long blockSize, long offset) {
    long startPointN =  blockSize * 2;
    long sum = 0;
    while (startPointN < n) {
      System.out.println("Stage 2:");
      long firstValueOfBlock = ((startPointN + offset) ^ offset) - k;
      long lastValueOfBlock = ((startPointN + offset + blockSize - 1 ) ^ offset) - k;
      System.out.println(
          "Start point: " + startPointN + " offset: " + offset + " blocksize: " + blockSize + " firstValueOfBlock: " + firstValueOfBlock + " lastValueOfBlock " + lastValueOfBlock + " size: " + Math.min(n - startPointN, blockSize));
      sum += sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp)
          * Math.min(n - startPointN, blockSize);
      startPointN += blockSize;
    }
    return sum;
  }


  public static long recursiveStep(long sideLength, long multiplier, long k, long newp) {
    long sum = sideLength / 2;
    System.out.println("Recursive: start: " + sideLength + " finish: " + ((sideLength * 2) - 1) + " multiplier: " + multiplier);
    sum +=
        (sumOfFullPowerBlock(sideLength, (sideLength * 2) - 1, k, newp) % newp * multiplier) % newp;
    for (long i = sideLength / 4; i > 0; i = i >> 1) {
      sum += recursiveStep(i, i, k, newp);
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
        long nextCellValue = (col ^ row + 1) - k;
        cellValue = cellValue >= 0 ? cellValue : 0;
        System.out.print(String.format("%2d ",
            cellValue));// + String.format("(%3d) ", nextCellValue-cellValue));
        if (cellValue > 0) {
//          if (row < m) {
//            cellValue = cellValue << 1;
//          }
          sum += cellValue;
        }
      }
      System.out.println();
//      System.out.print("             ".repeat((int) col + 1));
    }
    System.out.println();
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
    return previousPowerOfTwo << 1;
  }
}
