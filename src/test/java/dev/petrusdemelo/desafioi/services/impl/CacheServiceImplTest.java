package dev.petrusdemelo.desafioi.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class CacheServiceImplTest {
  private CacheServiceImpl<String> cacheService;

  @BeforeEach
  void setup() {
    cacheService = new CacheServiceImpl<>();
  }

  @Test
  void itShouldReturnOptionalEmptyWhenKeyIsNotPresent() {
    // given
    var lookupValue = "John Doe";
    var key = this.cacheKey(lookupValue);

    // when
    var cachedValue = this.cacheService.get(key);

    // then
    assertTrue(cachedValue.isEmpty());
  }

  @Test
  void itShouldReturnOptionalWithValueWhenKeyIsPresent() {
    // given
    var lookupValue = "John Doe";
    var key = this.cacheKey(lookupValue);
    this.cacheService.put(key, lookupValue);

    // when
    var cachedValue = this.cacheService.get(key);

    // then
    assertTrue(cachedValue.isPresent());
    assertEquals(cachedValue.get(), lookupValue);
  }

  @Test
  void itShouldRemoveEldestEntryWhenCacheSizeIsGreaterThanMaxEntries() {
    // given
    var lookupValue = "John Doe";
    var key = this.cacheKey(lookupValue);
    this.cacheService.put(key, lookupValue);

    for(int i = 0; i < 10; i++){
      var currentLookupValue = "John Doe " + i;
      var currentKey = this.cacheKey(currentLookupValue);
      this.cacheService.put(currentKey, currentLookupValue);
    }

    // when
    var cachedValue = this.cacheService.get(key);

    // then
    assertTrue(cachedValue.isEmpty());
  }

  private String cacheKey(String value){
    return "key:" + value;
  }
  
}
