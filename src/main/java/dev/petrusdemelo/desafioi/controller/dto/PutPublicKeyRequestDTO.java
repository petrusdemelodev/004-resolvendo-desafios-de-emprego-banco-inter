package dev.petrusdemelo.desafioi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PutPublicKeyRequestDTO(
  @NotBlank
  @Schema(description = "Public key to be added")
  String publicKey
) {}
