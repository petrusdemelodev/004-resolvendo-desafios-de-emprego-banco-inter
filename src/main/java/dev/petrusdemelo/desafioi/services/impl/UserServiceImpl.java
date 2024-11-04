package dev.petrusdemelo.desafioi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.petrusdemelo.desafioi.domain.User;
import dev.petrusdemelo.desafioi.repository.UserRepository;
import dev.petrusdemelo.desafioi.services.UserService;
import dev.petrusdemelo.desafioi.services.dto.UniqueDigitDTO;
import dev.petrusdemelo.desafioi.services.dto.UserDTO;
import dev.petrusdemelo.desafioi.services.exceptions.EmailAlreadyExistsException;
import dev.petrusdemelo.desafioi.services.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
  private final UserRepository userRepository;

  public UUID createUser(String name, String email, Optional<String> publicKey){
    var emailExists = this.userRepository.findByEmail(email);
    
    if(emailExists.isPresent()){
      throw new EmailAlreadyExistsException();
    }

    var user = User.builder()
                .email(email)
                .name(name)
                .build();
    
    publicKey.ifPresent(user::setPublicKey);

    this.userRepository.save(user);

    return user.getId();
  }  

  public UserDTO getUserByID(UUID userID) {
    var user = this.userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException());

    return new UserDTO(
      user.getId(),
      user.getName(),
      user.getEmail()
    );
  }

  public void deleteUserByID(UUID userID){
    this.userRepository.deleteById(userID);
  }

  public void updateUserByID(UUID userID, String name, String email, Optional<String> publicKey){
    var user = this.userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException());

    user.setName(name);
    user.setEmail(email);

    publicKey.ifPresent(user::setPublicKey);

    this.userRepository.save(user);
  }

  public List<UniqueDigitDTO> getCalculationsByUserID(UUID userID){
    var user = this.userRepository.findById(userID)
      .orElseThrow(() -> new UserNotFoundException());

    return user.getUniqueDigits().stream()
          .map(uniqueDigit -> {
            return new UniqueDigitDTO(
              uniqueDigit.getResult(),
              uniqueDigit.getNumber(),
              uniqueDigit.getK()
            );
          })
          .collect(Collectors.toList());
  }

  public void addPublicKey(UUID userID, String publicKey){
    var user = this.userRepository.findById(userID)
      .orElseThrow(() -> new UserNotFoundException());

    user.setPublicKey(publicKey);

    this.userRepository.save(user);
  }
}
