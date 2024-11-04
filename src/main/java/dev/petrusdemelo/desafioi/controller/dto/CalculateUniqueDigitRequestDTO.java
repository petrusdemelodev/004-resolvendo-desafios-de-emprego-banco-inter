package dev.petrusdemelo.desafioi.controller.dto;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CalculateUniqueDigitRequestDTO(
  @Schema(
    description = "Number to calculate the unique digit", 
    example = "123"
  )
  @NotBlank
  String number,
  
  @Schema(
    description = "Number of times the unique digit should be calculated", 
    example = "1", 
    nullable = true
  )
  Integer k,

  @Schema(
    description = "User's ID", 
    example = "123e4567-e89b-12d3-a456-426614174000"
  )
  Optional<UUID> userID
) {
  public BigInteger getBigIntegerNumber() {
    final String MESSAGE_ERROR = "Number must be a positive integer between 1 and 10ˆ1000000";
    BigInteger bigInteger;

    try {
      bigInteger = new BigInteger(this.number());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(MESSAGE_ERROR);
    }

    if(bigInteger.compareTo(BigInteger.ONE) < 0 
       || bigInteger.compareTo(BigInteger.TEN.pow(1000000)) > 0
    ) {
      throw new IllegalArgumentException(MESSAGE_ERROR);
    }


    return bigInteger;
  }

  public int getK(){
    final String MESSAGE_ERROR = "k must be a positive integer between 1 and 10ˆ5";
    if(this.k() == null){
      return 1;
    }

    if(!(this.k() >= 1 && this.k() <= Math.pow(10, 5))){
      throw new IllegalArgumentException(MESSAGE_ERROR);
    }

    return this.k();
  }
}
