package dev.petrusdemelo.desafioi.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.math.BigInteger;
import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.petrusdemelo.desafioi.domain.UniqueDigit;
import dev.petrusdemelo.desafioi.domain.User;
import dev.petrusdemelo.desafioi.repository.UserRepository;
import dev.petrusdemelo.desafioi.services.CacheService;

@TestInstance(Lifecycle.PER_CLASS)
class CalculateUniqueDigitServiceImplTest {
  private CalculateUniqueDigitServiceImpl service;
  @Mock private UserRepository userRepository;
  @Mock private CacheService<UniqueDigit> cacheService;

  @BeforeAll
  void setUp(){
    MockitoAnnotations.openMocks(this);
    this.service = new CalculateUniqueDigitServiceImpl(userRepository, cacheService);
  }

  @BeforeEach
  void resetMocks(){
    reset(this.cacheService, this.userRepository);
  }
  
  @Test
  void itShouldCalculateUniqueDigitIfCacheIsEmpty() {
    // given
    var bigInteger = BigInteger.valueOf(9875);
    var k = 4;
    var cacheKey = this.cacheKey(bigInteger, k);

    when(this.cacheService.get(any()))
      .thenReturn(Optional.empty());

    // when
    var result = this.service.calculateUniqueDigit(bigInteger, k, Optional.empty());

    // then
    assertEquals(result, 8);
    var capture = ArgumentCaptor.forClass(UniqueDigit.class);
    verify(this.cacheService, times(1))
      .put(eq(cacheKey), capture.capture());
    
    var uniqueDigit = capture.getValue();
    assertTrue(uniqueDigit.getNumber().equals(bigInteger.toString()));
    assertEquals(uniqueDigit.getK(), k);
  }

  @Test
  void itShouldGetResultFromCacheIfCacheExists() {
    // given
    var bigInteger = BigInteger.valueOf(9875);
    var k = 4;
    var cacheKey = this.cacheKey(bigInteger, k);
    var uniqueDigit = new UniqueDigit(bigInteger, k);

    when(this.cacheService.get(cacheKey))
      .thenReturn(Optional.of(uniqueDigit));

    // when
    var result = this.service.calculateUniqueDigit(bigInteger, k, Optional.empty());

    // then
    assertEquals(result, 8);
    verify(this.cacheService, times(0))
      .put(any(), any());
  }

  @Test
  void itShouldAddUniqueDigitToUserIfUserExists() {
    // given
    var bigInteger = BigInteger.valueOf(9875);
    var k = 4;
    var uuid = UUID.randomUUID();

    var user = User.builder()
              .id(uuid)
              .name("John Doe")
              .email("john.doe@gmail.com")
              .build();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.of(user));

    // when
    this.service.calculateUniqueDigit(bigInteger, k, Optional.of(uuid));

    // then
    var captor = ArgumentCaptor.forClass(User.class);
    verify(this.userRepository, times(1))
      .save(captor.capture());

    var capturedUniqueDigit = captor.getValue().getUniqueDigits().get(0);
    assertEquals(capturedUniqueDigit.getNumber(), bigInteger.toString());
    assertEquals(capturedUniqueDigit.getK(), k);
  }

  private String cacheKey(BigInteger number, int k){
    return new StringBuilder()
          .append("uniquedigit::")
          .append("number::")
          .append(String.valueOf(number))
          .append("::k::")
          .append(String.valueOf(k))
          .toString();
  }
}
