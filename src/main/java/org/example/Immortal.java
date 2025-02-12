package org.example;


public class Immortal {

  /**
   * set true to enable debug
   */
  static boolean debug = false;


  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    System.out.println(elderAge(9, 32, 6, 40)); //result : 5456283
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);

    startTime = System.currentTimeMillis();
    System.out.println(oldElderAge(9, 32, 6, 40)); //result : 5456283
    estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
  }


  static long elderAge(long n, long m, long k, long newp) {
//    System.out.println(n + ", " + m + ", " + k + ", " + newp);
    // n is always the bigger side
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }
    long sum = 0;
    long width = n;
    long heightTop = m;
    long heightBottom = heightTop;
    long offset = 0;

    long blockSizeTop = highestPowerOfTwoLessThan(n - 1);
    long blockSizeBottom = highestPowerOfTwoLessThan(m - 1);
    while ((heightBottom > 0 && blockSizeBottom > 0) || (heightTop > 0 && blockSizeTop > 0)) {
      if (heightTop > 0 && blockSizeTop > 0) {
        sum += stage1(width, heightTop, k, newp, offset);
        heightTop -= blockSizeTop;
        width -= blockSizeTop;
        if (isPowerOfTwo(n) && isPowerOfTwo(m)) {
          blockSizeTop = highestPowerOfTwoLessThan(heightTop-1);
        } else {
          blockSizeTop = Math.max(highestPowerOfTwoLessThan(width-1), highestPowerOfTwoLessThan(heightTop-1));
        }
      }
      if (blockSizeBottom > 0) {
        sum += (stage1(heightBottom, heightBottom, k, newp, offset));
        heightBottom -= blockSizeBottom;
        offset += blockSizeBottom;
        blockSizeBottom = highestPowerOfTwoLessThan(heightBottom - 1);
      }
    }
    return sum % newp;
  }

  private static boolean isPowerOfTwo(long n) {
    return Long.toBinaryString(n).replace("0", "").length() == 1;
  }

  private static long stage1(long n, long m, long k, long newp, long offsetM) {
    long nSideLength = 1;
    long totalLength = 0;
    long sum = 0;
    while (totalLength + 1 <= n) {
      long multiplier = Math.min(nSideLength, n - nSideLength);
      long remainingN = n - totalLength - 1;
      if (m < nSideLength && n <= totalLength + nSideLength) {
        if( (n - nSideLength) > 0) {
          sum += (recursiveStep(nSideLength, m, multiplier, k, newp, true, false));
        }
        sum += (calculateRest(offsetM, offsetM + totalLength + 1, remainingN, m, k, newp));
        break;
      }
      if (multiplier != 0) {
        sum += (recursiveStep(nSideLength, m, multiplier, k, newp, true, true));
      }
      totalLength += nSideLength;
      nSideLength <<= 1;
    }
    return sum;
  }

  public static long recursiveStep(long sideLength, long m, long multiplier, long k, long newp,
      boolean first, boolean calculated) {
    long recursiveStartPoint = first ? sideLength / 2 : 0;
    long sum = 0;
    if (calculated) {
      sum =
          (sumOfFullPowerBlock(sideLength, (sideLength * 2) - 1, k, newp) * Math.min(
              multiplier, m));
    }
    if (sum > 0 || !calculated) {
      for (long i = sideLength / (first ? 4 : 2); i > 0; i = i >> 1) {
        if (recursiveStartPoint <= m) {
          sum += (recursiveStep(i, m - recursiveStartPoint, i, k, newp, false, true));
          recursiveStartPoint += i;
        }
      }
      return sum;
    } else {
      return 0;
    }
  }

  private static long calculateRest(long offsetM, long offsetN, long n, long m, long k, long newp) {
    long nSidePower = highestPowerOfTwoLessThan(n);
    long mSidePower = highestPowerOfTwoLessThan(m);
    long side = Math.max(nSidePower, mSidePower);
    long multiplier;
    if (m > side && n > side) {
      multiplier = side;
    } else {
      multiplier = Math.min(m, n);
    }
    long firstValueOfBlock = (offsetN ^ offsetM);
    long lastValueOfBlock = ((offsetN + side - 1) ^ offsetM);
    long sum = sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp) * multiplier;
    if (side <= n && side <= m) {
      firstValueOfBlock = (offsetN + side ^ offsetM);
      lastValueOfBlock = ((offsetN + side * 2 - 1) ^ offsetM);
      multiplier = n - side;
      sum += (sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp) * multiplier);

      firstValueOfBlock = (offsetN ^ offsetM + side);
      lastValueOfBlock = ((offsetN + side - 1) ^ offsetM + side);
      multiplier = m - side;
      sum +=( sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp) * multiplier);
    }
    if (side <= n) {
      offsetN += side;
      n -= side;
    }
    if (side <= m) {
      offsetM += side;
      m -= side;
    }
    if (n > 0 && m > 0) {
      sum += (calculateRest(offsetM, offsetN, n, m, k, newp));
    }

    return sum;
  }

  public static long sumOfFullPowerBlock(long n, long m, long k, long newp) {
    m = m - k;
    if (m < 1) {
      return 0;
    }
    n = n - k < 1 ? 1 : n - k;

    long nWhole = n / newp;
    long mWhole = m / newp;
    n %= newp;
    m %= newp;
    if (nWhole == mWhole) {
      return (((m - n + 1) * (n + m)) / 2);
    } else {
      long sum = 0;
      sum += (((newp - n) * (n + newp - 1)) / 2);
      sum += (newp % 2 == 0 ? (newp / 2) * ((mWhole - nWhole - 1) % 2) : 0) % newp;
      sum += (((1 + m) * (m)) / 2) % newp;
      return sum;
    }
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
  static long oldElderAge(long n, long m, long k, long newp) {
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

}
