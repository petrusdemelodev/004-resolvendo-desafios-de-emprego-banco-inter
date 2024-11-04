package dev.petrusdemelo.desafioi.services;

import java.util.Optional;

public interface CacheService<T> {
  void put(String key, T value);
  Optional<T> get(String key);
}
