package dev.petrusdemelo.desafioi.controller.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateUserResponseDTO(
  @Schema(description = "User id", example = "123e4567-e89b-12d3-a456-426614174000")
  UUID id
) {}
