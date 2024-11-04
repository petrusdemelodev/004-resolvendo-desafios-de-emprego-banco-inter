package dev.petrusdemelo.desafioi.controller;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.petrusdemelo.desafioi.controller.dto.CreateUserRequestDTO;
import dev.petrusdemelo.desafioi.controller.dto.CreateUserResponseDTO;
import dev.petrusdemelo.desafioi.controller.dto.GetCalculationsByUserIDResponseDTO;
import dev.petrusdemelo.desafioi.controller.dto.GetUserByIDResponseDTO;
import dev.petrusdemelo.desafioi.controller.dto.PutPublicKeyRequestDTO;
import dev.petrusdemelo.desafioi.controller.dto.UniqueDigitResponseDTO;
import dev.petrusdemelo.desafioi.controller.dto.UpdateUserRequestDTO;
import dev.petrusdemelo.desafioi.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  @PostMapping
  public ResponseEntity<CreateUserResponseDTO> createUser(
    @RequestBody @Valid CreateUserRequestDTO createUserRequestDTO
  ) {
    var createdUserID = this.userService.createUser(
      createUserRequestDTO.name(), 
      createUserRequestDTO.email(),
      createUserRequestDTO.publicKey()
    );

    var response = new CreateUserResponseDTO(createdUserID);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{userID}")
  public ResponseEntity<GetUserByIDResponseDTO> getUserByID(@PathVariable UUID userID){
    var userDTO = this.userService.getUserByID(userID);
    var response = new GetUserByIDResponseDTO(
      userDTO.id(),
      userDTO.name(),
      userDTO.email()
    );
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{userID}")
  public ResponseEntity<Void> deleteUserByID(@PathVariable UUID userID){
    this.userService.deleteUserByID(userID);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{userID}")
  public ResponseEntity<Void> updateUserByID(
    @PathVariable UUID userID,
    @RequestBody @Valid UpdateUserRequestDTO updateUserRequestDTO
  ){
    this.userService.updateUserByID(
      userID,
      updateUserRequestDTO.name(),
      updateUserRequestDTO.email(),
      updateUserRequestDTO.publicKey()
    );
    
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{userID}/calculations")
  public ResponseEntity<GetCalculationsByUserIDResponseDTO> getCalculationsByUserID(
    @PathVariable UUID userID){
      var listOfUniqueDigits = this.userService.getCalculationsByUserID(userID);

      var list = listOfUniqueDigits.stream()
        .map(uniqueDigitDTO -> new UniqueDigitResponseDTO(
          uniqueDigitDTO.result(),
          uniqueDigitDTO.number(),
          uniqueDigitDTO.k()
        ))
        .collect(Collectors.toList());

      var response = new GetCalculationsByUserIDResponseDTO(list);
      return ResponseEntity.ok(response);
  }

  @PutMapping("/{userID}/publicKey")
  public ResponseEntity<Void> addPublicKey(
    @PathVariable UUID userID,
    @RequestBody PutPublicKeyRequestDTO putPublicKeyRequestDTO){
      this.userService.addPublicKey(
          userID, 
          putPublicKeyRequestDTO.publicKey()
      );
      return ResponseEntity.noContent().build();
  }
}
