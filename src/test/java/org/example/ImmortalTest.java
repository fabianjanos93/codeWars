package org.example;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ImmortalTest {

  @Test
  void square8() {
    assertEquals(224, Immortal.elderAge(8, 8, 0, 1000007));
  }
  @Test
  void square16() {
    assertEquals(1920, Immortal.elderAge(16, 16, 0, 1000007));
  }

  @Test
  void rect8times14() {
    assertEquals(776, Immortal.elderAge(8, 14, 0, 1000007));
  }

  @Test
  void rect12times14() {
    assertEquals(1212, Immortal.elderAge(12, 14, 0, 1000007));
  }

  @Test
  void rect14times14() {
    assertEquals(1442, Immortal.elderAge(14, 14, 0, 1000007));
  }

  @Test
  void rect14times14k1() {
    assertEquals(1260, Immortal.elderAge(14, 14, 1, 1000007));
  }
  @Test
  void rect30times50k10() {
    assertEquals(26928, Immortal.elderAge(30, 50, 10, 1000007));
  }
  @Test
  void rect11times13() {
    assertEquals(993, Immortal.elderAge(11, 13, 0, 1000007));
  }
  @Test
  void rect10times15() {
    assertEquals(1095, Immortal.elderAge(10, 15, 0, 1000007));
  }

  @Test
  void rect545times435k432() {
    assertEquals(808451, Immortal.elderAge(545, 435, 342, 1000007));
  }
  @Test
  void first70times70() {
    for(int i = 1 ; i < 70 ; i++) {
      for(int j = i; j < 70 ; j++) {
        long e = Immortal.elderAge(i, j, 3, 1000007);
        long o = Immortal.oldElderAge(i, j, 3, 1000007);
        if ( o != e) {
          assertEquals(o,e);
        }
      }
    }
  }
}