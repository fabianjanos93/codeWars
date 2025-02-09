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
    System.out.println(elderAge(11, 13, 2, 1000007)); //result : 5
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);

    startTime = System.currentTimeMillis();
//    // System.out.println(oldElderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));
    System.out.println(oldElderAge(11, 13, 2, 1000007)); //result : 5

//    for(int i = 1 ; i < 70 ; i++) {
//      for(int j = i; j < 70 ; j++) {
//        long e = elderAge(i, j, 3, 1000007);
//        long o = oldElderAge(i, j, 3, 1000007);
//        if ( o != e) {
//          System.out.println(i + " " + j);
//        }
//      }
//    }

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
    long width = n - 1;
    long heightTop = m-1;
    long heightBottom = heightTop;
    long offset = 0;

    long blockSizeTop = Math.min(highestPowerOfTwoLessThan(n - 1), highestPowerOfTwoLessThan(m));
    long blockSizeBottom = highestPowerOfTwoLessThan(m - 1);
//    System.out.println("blockSizeTop: " + blockSizeTop + " blockSizeBottom: " + blockSizeBottom);
    while (heightBottom > 0 || heightTop > 0) {
//      System.out.println("widthTop: " + width + " | heightTop: " + heightTop);
      if (heightTop > 0) {
//        System.out.println("TOP");
//        System.out.println("widthTop: " + width + " | heightTop: " + heightTop + " | blockSizeTop: " + blockSizeTop);
        sum += stage0(width, heightTop, k, newp, blockSizeTop, offset) % newp;
        heightTop -= blockSizeTop;
        width -= blockSizeTop;
        blockSizeTop = Math.min(highestPowerOfTwoLessThan(heightTop + 1), highestPowerOfTwoLessThan(width));
      }
//      System.out.println("BOTTOM");
      sum += stage0(heightBottom, heightBottom, k, newp, blockSizeBottom, offset) % newp;
      heightBottom -= blockSizeBottom;
      offset += blockSizeBottom;
      blockSizeBottom = highestPowerOfTwoLessThan(heightBottom);
    }
    return sum % newp;
  }

  private static long stage0(long n, long m, long k, long newp, long blockSize, long offset) {
    long sum = 0;
    sum += stage1(n, m, k, newp) % newp;
    sum += stage2(n, k, newp, blockSize, offset);
    return sum;
  }

  private static long stage1(long n, long m, long k, long newp) {
    long nSideLength = 1;
    long totalLength = 0;
    long sum = 0;
    while (totalLength <= n && totalLength <= m) {
      long multiplier = Math.min(nSideLength, n - nSideLength + 1);
//      System.out.println("totalSideN: " + (totalLength + nSideLength) + "(n: " + n + ", m: " + m + ")" + " multiplier: " + multiplier);
      if (multiplier != 0) {
        sum += recursiveStep(nSideLength, multiplier, k, newp, true);
      }
      totalLength += nSideLength;
      nSideLength <<= 1;
    }
    return sum;
  }

  private static long stage2(long n, long k, long newp, long blockSize, long offset) {
    long startPointN = blockSize * 2;
    long sum = 0;
//    System.out.println("startPointN: " + startPointN + "(" + n + ")");
    while (startPointN <= n) {
//      System.out.println("Stage 2: startPoint: " + startPointN + " (" + n + ")");
      long firstValueOfBlock = ((startPointN + offset) ^ offset);
      long lastValueOfBlock = ((startPointN + offset + blockSize - 1) ^ offset);
//      System.out.println(
//          "Start point: " + startPointN + " offset: " + offset + " blocksize: " + blockSize
//              + " firstValueOfBlock: " + firstValueOfBlock + " lastValueOfBlock " + lastValueOfBlock
//              + " size: " + Math.min(n - startPointN + 1, blockSize));
      sum += sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp)
          * Math.min(n - startPointN + 1, blockSize);
      startPointN += blockSize;
    }
    return sum;
  }


  public static long recursiveStep(long sideLength, long multiplier, long k, long newp,
      boolean first) {
    long sum =
        (sumOfFullPowerBlock(sideLength, (sideLength * 2) - 1, k, newp) % newp * multiplier) % newp;
//      System.out.println("Recursive: start: " + sideLength + " finish: " + ((sideLength * 2) - 1) + " multiplier: " + multiplier + " sum: " + sum);
    for (long i = sideLength / (first ? 4 : 2); i > 0; i = i >> 1) {
//      System.out.println("next: " + i + " current: " + sideLength);
      sum += recursiveStep(i, i, k, newp, false);
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
//        System.out.print(String.format("%2d ", cellValue));
        if (cellValue > 0) {
//          if (row < m) {
//            cellValue = cellValue << 1;
//          }
          sum += cellValue;
        }
      }
//      System.out.println();
//      System.out.print("             ".repeat((int) col + 1));
    }
//    System.out.println();
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
