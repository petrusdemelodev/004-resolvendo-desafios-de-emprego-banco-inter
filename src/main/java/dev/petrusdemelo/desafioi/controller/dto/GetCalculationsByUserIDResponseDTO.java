package dev.petrusdemelo.desafioi.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetCalculationsByUserIDResponseDTO(
  @Schema(description = "Calculations of the user")
  List<UniqueDigitResponseDTO> uniqueDigits
) {}