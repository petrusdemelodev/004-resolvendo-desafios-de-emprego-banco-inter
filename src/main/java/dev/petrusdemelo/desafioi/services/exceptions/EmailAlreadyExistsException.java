package dev.petrusdemelo.desafioi.services.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public EmailAlreadyExistsException() {
    super("Email already exists");
  }  
}
