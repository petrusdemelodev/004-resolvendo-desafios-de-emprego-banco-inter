package dev.petrusdemelo.desafioi.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.petrusdemelo.desafioi.services.dto.UniqueDigitDTO;
import dev.petrusdemelo.desafioi.services.dto.UserDTO;

public interface UserService {
  UUID createUser(String name, String email, Optional<String> publicKey);
  UserDTO getUserByID(UUID userID);
  void deleteUserByID(UUID userID);
  void updateUserByID(UUID userID, String name, String email, Optional<String> publicKey);
  List<UniqueDigitDTO> getCalculationsByUserID(UUID userID);
  void addPublicKey(UUID userID, String publicKey);
}
