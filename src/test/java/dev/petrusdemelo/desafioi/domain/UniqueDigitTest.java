package dev.petrusdemelo.desafioi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class UniqueDigitTest {
  @Test
  void itShouldReturnTheRightValue() {
    // given
    var number = BigInteger.valueOf(9875);
    var k = 4;

    // when
    var uniqueDigit = new UniqueDigit(number, k);
    var anotherUniqueDigit = new UniqueDigit(number, 1);

    // then
    assertThat(uniqueDigit.getResult()).isEqualTo(8);
    assertThat(anotherUniqueDigit.getResult()).isEqualTo(2);
  }
}
