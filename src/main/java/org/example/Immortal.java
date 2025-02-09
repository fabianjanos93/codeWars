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
    System.out.println(elderAge(18, 60, 0, 10000000)); //result : 5
    long estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);

    startTime = System.currentTimeMillis();
//    // System.out.println(oldElderAge(SIDE_LENGTH, SIDE_LENGTH, K, 1000007));
    System.out.println(oldElderAge(18, 60, 0, 10000000)); //result : 5

    estimatedTime = System.currentTimeMillis() - startTime;
    System.out.println("run milis:" + estimatedTime);
  }

  static long secondElderAge(long n, long m, long k, long newp) {
    long sum = 0;
    boolean thirdSectionTopCalculated = false;
    boolean thirdSectionBottomCalculated = false;
    long startValueTopM = 0;
    long startValueBottomN = 0;
    long startValueBottomM = 0;

    long stage1aTime = 0;
    long stage1bTime = 0;
    long stage2Time = 0;
    long stage3Time = 0;
    long stage4Time = 0;

    HashMap<Long, Long> remainingSmallerBlockToSums = new HashMap<>();

    // n is always the longer side
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }

    for (long i = 1; i <= m; i++) {
      long startValue = nextPowerOfTwo(i);
      long endValueN = i - 1;
      long endValueM = i - 1;

      boolean setBottom = false;
      boolean setTop = false;

      long startTime = System.currentTimeMillis();
      // First Section
      long nextPowerOfTwo = nextPowerOfTwo(i);
      long remainingSmallerBlocks = nextPowerOfTwo - i;
      while (remainingSmallerBlocks > 0) {
        long previousPowerOfTwo = highestPowerOfTwoLessThan(remainingSmallerBlocks);

        stage1aTime += System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        long mEnd = i + remainingSmallerBlocks;
        if (mEnd > n) {
          remainingSmallerBlocks -= previousPowerOfTwo;
          continue;
        }
        long sum1 = sumOfFullPowerBlock(previousPowerOfTwo, (previousPowerOfTwo * 2) - 1, k, newp);
        if (mEnd <= m) {
          sum1 = sum1 << 1;
          if (!setBottom) {
            endValueM = mEnd;
            setBottom = true;
          }
        }
        if (mEnd <= n) {
          if (!setTop) {
            endValueN = mEnd;
            setTop = true;
          }
        }
        sum += sum1 % newp;
        remainingSmallerBlocks -= previousPowerOfTwo;
      }

      stage1bTime += System.currentTimeMillis() - startTime;
      startTime = System.currentTimeMillis();
      // Second Section
      while (nextPowerOfTwo(endValueN + 1) <= n) {
        endValueN = nextPowerOfTwo(endValueN + 1);
        if (endValueN <= m - 1) {
//          System.out.println(i + " Update setBottom to " + nextPowerOfTwo(endValueM + 1));
          endValueM = endValueN;
        }
      }
      if (startValue < endValueN) {
        long sum1 = sumOfFullPowerBlock(startValue, endValueN - 1, k, newp);
        long sum2 = sumOfFullPowerBlock(startValue, endValueM - 1, k, newp);
//        System.out.println(
//            i + " |sum1: " + sum1 + " (" + startValue + ", " + (endValueN - 1) + ") |sum2: "
//                + sum2);
        sum += sum1;
        // System.out.println(i + " second section sum1 above: " + sum1);
        sum += sum2 > 0 ? sum2 : 0;
        // System.out.println(i + " second section sum2 below: " + sum2);
      }
      stage2Time += System.currentTimeMillis() - startTime;
      startTime = System.currentTimeMillis();
      // Third Section
      // Bottom
//      System.out.println("endv: " + endValueN);
      if (!thirdSectionBottomCalculated && (endValueM < n)) {
        long thirdSectionBottom =
            sumOfFullPowerBlock(endValueM, endValueM * 2 - 1, k, newp) * (m - endValueM);
        sum += thirdSectionBottom;
//        System.out.println("bottom sum: " + (m - endValueM) + " " + thirdSectionBottom);
        thirdSectionBottomCalculated = true;
      }
      // Top
      if (!thirdSectionTopCalculated && (endValueN < m)) {
        long thirdSectionTop =
            sumOfFullPowerBlock(endValueN, endValueN * 2 - 1, k, newp) * (n - endValueN);
//        System.out.println("top sum: " + (n - endValueN) + " " + thirdSectionTop);
        sum += thirdSectionTop;
        thirdSectionTopCalculated = true;
      }
      stage3Time += System.currentTimeMillis() - startTime;
      startTime = System.currentTimeMillis();

      // Fourth Section
      // Bottom
      // System.out.println("endM: " + endValueM);
      boolean nothingLeftBottom = false;
      if (i > highestPowerOfTwoLessThan(m) || !thirdSectionBottomCalculated) {
        long startN = Math.max(endValueN, startValueBottomN);
        if (i > startValueBottomM && startN < m) {
          while (!nothingLeftBottom) {
            long blockSideSize = Math.max(highestPowerOfTwoLessThan(m - startN + 1),
                highestPowerOfTwoLessThan(m - i + 1));
            while (startN < m) {
              if (blockSideSize == 0) {
                nothingLeftBottom = true;
                break;
              }
              long firstValueOfBlock = ((i - 1) ^ startN) - k;
              long lastValueOfBlock = ((i - 1) ^ (startN + blockSideSize - 1)) - k;
              long blockSum;

              if ((startN + blockSideSize - 1) <= m) {
                if (i + blockSideSize - 1 <= m) {
//                  System.out.println(1);
                  blockSum = sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp)
                      * blockSideSize;
                  sum += blockSum;
                  nothingLeftBottom = true;
                } else {
//                  System.out.println(2);
//                  System.out.println("lineSum: " + lineSum + " blockSide: " +  (m - i + 1));
                  sum += sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k,
                      newp) * (m - i + 1);
                  nothingLeftBottom = true;
                }
              } else {
                if (i + blockSideSize - 1 <= m) {
//                  System.out.println(3);
                  sum += sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k,
                      newp) * (m - startN);
                  nothingLeftBottom = true;
                } else {
                  startValueBottomN += blockSideSize;
                  nothingLeftBottom = false;
                  break;
                }
              }
              startN += blockSideSize;
            }
            startValueBottomM += blockSideSize;
          }
        }
      }

      // Top
      boolean nothingLeftTop = false;
      if (i > highestPowerOfTwoLessThan(m) || !thirdSectionTopCalculated) {
        long startN = Math.max(endValueN, startValueBottomN);
        if (i > startValueTopM && startN < n) {
          while (!nothingLeftTop) {
            long blockSideSize = Math.max(highestPowerOfTwoLessThan(n - startN + 1),
                highestPowerOfTwoLessThan(m - i + 1));
            while (startN < n) {
              if (blockSideSize == 0) {
                nothingLeftTop = true;
                break;
              }
              long firstValueOfBlock = ((i - 1) ^ startN) - k;
              long lastValueOfBlock = ((i - 1) ^ (startN + blockSideSize - 1)) - k;
              long blockSum;

              if ((startN + blockSideSize - 1) <= n) {
                if (i + blockSideSize - 1 <= m) {
//                  System.out.println(1);
                  blockSum = sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k, newp)
                      * blockSideSize;
                  sum += blockSum;
                  nothingLeftTop = true;
                } else {
//                  System.out.println(2);
//                  System.out.println("lineSum: " + lineSum + " blockSide: " +  (m - i + 1));
                  sum += sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k,
                      newp) * (m - i + 1);
                  nothingLeftTop = true;
                }
              } else {
                if (i + blockSideSize - 1 <= m) {
//                  System.out.println(3);
                  sum += sumOfFullPowerBlock(firstValueOfBlock, lastValueOfBlock, k,
                      newp) * (n - startN);
                  nothingLeftTop = true;
                } else {
                  startValueBottomN += blockSideSize;
                  nothingLeftTop = false;
                  break;
                }
              }
              startN += blockSideSize;
            }
            startValueTopM += blockSideSize;
          }
        }
      }

      stage4Time += System.currentTimeMillis() - startTime;
    }
    System.out.println("stage1aTime " + stage1aTime);
    System.out.println("stage1bTime " + stage1bTime);
    System.out.println("stage2Time " + stage2Time);
    System.out.println("stage3Time " + stage3Time);
    System.out.println("stage4Time " + stage4Time);
    return sum % newp;
  }


  static long elderAge(long n, long m, long k, long newp) {
    // n is always the bigger side
    if (n < m) {
      long temp = m;
      m = n;
      n = temp;
    }
    long sum = 0;
    long blockSize = Math.min(highestPowerOfTwoLessThan(n), highestPowerOfTwoLessThan(m));
    long height = m;

    while(height > 1) {
      sum += stage4(n, height, k, newp, blockSize) % newp;
      height -= blockSize;
      blockSize = highestPowerOfTwoLessThan(height);
    }
    return sum;
  }

  private static long stage4(long n, long m, long k, long newp, long blockSize) {
    long sum = 0;
    sum += stage1(n, m, k, newp) % newp;
    sum += stage1(m, m, k, newp) % newp;

    sum = stage2(n, k, newp, blockSize, sum);
    sum = stage2(m, k, newp, blockSize, sum);

    return sum;
  }

  private static long stage2(long n, long k, long newp, long blockSize, long sum) {
    long startPoint = blockSize;
    while (startPoint < n) {
      sum += sumOfFullPowerBlock(startPoint, startPoint + blockSize - 1, k, newp) * Math.min(n - startPoint,
          blockSize);
      startPoint += blockSize;
    }
    return sum;
  }

  private static long stage1(long n, long m, long k, long newp) {
    long nSideLength = 1;
    long totalLength = 0;
    long sum = 0;
    while (totalLength + nSideLength <= n && totalLength + nSideLength <= m) {
      sum += recursiveStep(nSideLength, k, newp);
      totalLength += nSideLength;
      System.out.println(totalLength);
      nSideLength <<= 1;
    }
    return sum;
  }


  public static long recursiveStep(long sideLength, long k, long newp) {
    long sum = 0;
    sum +=
        (sumOfFullPowerBlock(sideLength, (sideLength * 2) - 1, k, newp) % newp * sideLength) % newp;
    for (long i = sideLength / 2; i > 0; i = i >> 1) {
      sum += recursiveStep(i, k, newp);
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
    return previousPowerOfTwo << (n == previousPowerOfTwo ? 0 : 1);
  }
}
