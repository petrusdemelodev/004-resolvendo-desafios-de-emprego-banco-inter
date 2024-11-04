package dev.petrusdemelo.desafioi.domain.exceptions;

public class InvalidPublicKeySizeException extends RuntimeException {
  public InvalidPublicKeySizeException(String message) {
    super(message);
  }
}
