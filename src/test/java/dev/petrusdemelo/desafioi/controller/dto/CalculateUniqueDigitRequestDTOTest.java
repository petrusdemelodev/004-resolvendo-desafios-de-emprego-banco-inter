package dev.petrusdemelo.desafioi.controller.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestInstance(Lifecycle.PER_CLASS)
class CalculateUniqueDigitRequestDTOTest {
  @Test
  void itShouldReturnBigInteger() {
    // given
    var request = new CalculateUniqueDigitRequestDTO("9875", 4, Optional.empty());

    // when
    var result = request.getBigIntegerNumber();
    var integer = request.getK();

    // then
    assertTrue(result.equals(new BigInteger("9875")));
    assertEquals(integer, 4);
  }

  @Test
  void itShouldReturnErrorIfNumberIsNotParsebleToBigInteger() {
    // given
    var request = new CalculateUniqueDigitRequestDTO("asdasdasd2312adsdas", 4, Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      request.getBigIntegerNumber();
    });
  }

  @Test
  void itShouldReturnBigIntegerDoesntRespectTheRange() {
    // given
    var bigInteger = BigInteger.TEN.pow(1000000).add(BigInteger.ONE);
    var request = new CalculateUniqueDigitRequestDTO(bigInteger.toString(), 4, Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      request.getBigIntegerNumber();
    });
  }

  @ParameterizedTest()
  @ValueSource(ints = { 0, 1000000 })
  void itThrowAnErrorIfIntegerDoesntRespectTheRange(Integer i) {
    // given
    var request = new CalculateUniqueDigitRequestDTO("9875", i, Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      request.getK();
    });
  }
}
