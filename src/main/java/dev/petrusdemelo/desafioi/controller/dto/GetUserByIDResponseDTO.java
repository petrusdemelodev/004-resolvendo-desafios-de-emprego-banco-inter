package dev.petrusdemelo.desafioi.controller.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetUserByIDResponseDTO(
  @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
  UUID id,

  @Schema(description = "User name", example = "John Doe")
  String name,

  @Schema(description = "User email", example = "john.doe@gmail.com")
  String email
) {}
