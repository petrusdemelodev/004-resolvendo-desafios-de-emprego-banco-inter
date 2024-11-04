package dev.petrusdemelo.desafioi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.petrusdemelo.desafioi.controller.dto.CalculateUniqueDigitRequestDTO;
import dev.petrusdemelo.desafioi.services.CalculateUniqueDigitService;

@TestInstance(Lifecycle.PER_CLASS)
class CalculateUniqueDigitControllerTest {
  private CalculateUniqueDigitController controller;
  @Mock private CalculateUniqueDigitService service;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.controller = new CalculateUniqueDigitController(this.service);
  }

  @BeforeEach
  void resetMocks(){
    reset(this.service);
  }

  @Test
  void itShouldReturnUniqueDigitValue(){
    // given
    var request = new CalculateUniqueDigitRequestDTO("9875", 4, null);
      
    when(this.service.calculateUniqueDigit(any(BigInteger.class), anyInt(), any()))
      .thenReturn(8);

    // when
    var response = this.controller.calculateUniqueDigit(request);
    var body = response.getBody();

    // then
    assertEquals(8, body);
    verify(this.service, times(1))
      .calculateUniqueDigit(any(BigInteger.class), anyInt(), any());
  }
}
