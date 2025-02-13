package org.example;

import static org.example.Immortal.elderAge;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ImmortalTest {

  @Test
  void square8() {
    assertEquals(224, elderAge(8, 8, 0, 1000007));
  }

  @Test
  void square16() {
    assertEquals(1920, elderAge(16, 16, 0, 1000007));
  }

  @Test
  void rect8times14() {
    assertEquals(776, elderAge(8, 14, 0, 1000007));
  }

  @Test
  void rect12times14() {
    assertEquals(1212, elderAge(12, 14, 0, 1000007));
  }

  @Test
  void rect14times14() {
    assertEquals(1442, elderAge(14, 14, 0, 1000007));
  }

  @Test
  void rect14times14k1() {
    assertEquals(1260, elderAge(14, 14, 1, 1000007));
  }

  @Test
  void rect30times50k10() {
    assertEquals(26928, elderAge(30, 50, 10, 1000007));
  }

  @Test
  void rect11times13() {
    assertEquals(993, elderAge(11, 13, 0, 1000007));
  }

  @Test
  void rect10times15() {
    assertEquals(1095, elderAge(10, 15, 0, 1000007));
  }

  @Test
  void rect545times435k432() {
    assertEquals(808451, elderAge(545, 435, 342, 1000007));
  }

  @Test
  void testLast() {
    assertEquals(5456283, elderAge(28827050410L, 35165045587L, 7109602, 13719506));
  }

  @Test
  void angry1() {
    assertEquals(2246776, elderAge(15742492581642L, 9300208161284L, 8032745, 10204231));
  }

  @Test
  void angry2() {
    assertEquals(1957420, elderAge(40284095990599L, 275562788235L, 487565, 7654922));
  }

  @Test
  void angry3() {
    assertEquals(109398, elderAge(181073735732752L, 42244706068104L, 5617604, 197822));
  }

  @Test
  void interested1() {
    assertEquals(206, elderAge(997, 153, 16, 224));
  }

  @Test
  void interested2() {
    assertEquals(20, Immortal.oldElderAge(840, 148, 6, 40));
    assertEquals(20, elderAge(840, 148, 6, 40));
  }

  @Test
  void excited1() {
    assertEquals(2055, elderAge(155586, 39693, 8479, 2688));
  }

  @Test
  void first200times200() {
    for (int newp = 2; newp < 70; newp++) {
      for (int i = 1; i < 200; i++) {
        for (int j = i; j < 200; j++) {
          long e = Immortal.elderAge(i, j, 6, newp);
          long o = Immortal.oldElderAge(i, j, 6, newp);
          if (o != e) {
            System.out.println(i + " " + j + " " + newp);
            assertEquals(o, e);
          }
        }
      }
    }
  }


}