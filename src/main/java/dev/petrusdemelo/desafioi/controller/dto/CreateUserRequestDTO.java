package dev.petrusdemelo.desafioi.controller.dto;

import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequestDTO(
  @Schema(description = "User name", example = "John Doe")
  @NotEmpty
  String name,

  @Schema(description = "User email", example = "john.doe@gmail.com")
  @Email
  @NotEmpty
  String email,

  @Schema(description = "User public key", example = "")
  Optional<String> publicKey
) {}
