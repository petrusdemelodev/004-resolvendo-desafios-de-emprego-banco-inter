package dev.petrusdemelo.desafioi.services.dto;

import java.util.UUID;

public record UserDTO(
  UUID id,
  String name,
  String email
) {}
