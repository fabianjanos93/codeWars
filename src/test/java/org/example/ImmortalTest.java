package org.example;

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
}