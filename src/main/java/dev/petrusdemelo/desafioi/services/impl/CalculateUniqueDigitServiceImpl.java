package dev.petrusdemelo.desafioi.services.impl;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.petrusdemelo.desafioi.domain.UniqueDigit;
import dev.petrusdemelo.desafioi.repository.UserRepository;
import dev.petrusdemelo.desafioi.services.CacheService;
import dev.petrusdemelo.desafioi.services.CalculateUniqueDigitService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculateUniqueDigitServiceImpl implements CalculateUniqueDigitService {
  private final UserRepository userRepository;
  private final CacheService<UniqueDigit> cacheService;
  
  public int calculateUniqueDigit(BigInteger bigInteger, int k, Optional<UUID> userID) {
    var cacheKey = this.cacheKey(bigInteger, k);
    var uniqueDigit = this.cacheService.get(cacheKey)
      .orElseGet(() -> this.createNewUniqueDigit(bigInteger, k));

    if(userID.isPresent()){
      this.userRepository.findById(userID.get()).ifPresent(user -> {
        user.addUniqueDigit(uniqueDigit);
        this.userRepository.save(user);
      });
    }
    
    return uniqueDigit.getResult();
  }  

  private UniqueDigit createNewUniqueDigit(BigInteger bigInteger, int k){
    var uniqueDigit = new UniqueDigit(bigInteger, k);
    var cacheKey = this.cacheKey(bigInteger, k);
    this.cacheService.put(cacheKey, uniqueDigit);
    return uniqueDigit;
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
