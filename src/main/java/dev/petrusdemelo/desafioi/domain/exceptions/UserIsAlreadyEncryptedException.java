package dev.petrusdemelo.desafioi.domain.exceptions;

public class UserIsAlreadyEncryptedException extends RuntimeException {
  public UserIsAlreadyEncryptedException() {
    super("User is already encrypted");
  }
}
