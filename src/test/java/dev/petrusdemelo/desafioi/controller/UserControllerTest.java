package dev.petrusdemelo.desafioi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.petrusdemelo.desafioi.controller.dto.CreateUserRequestDTO;
import dev.petrusdemelo.desafioi.controller.dto.PutPublicKeyRequestDTO;
import dev.petrusdemelo.desafioi.controller.dto.UpdateUserRequestDTO;
import dev.petrusdemelo.desafioi.services.UserService;
import dev.petrusdemelo.desafioi.services.dto.UniqueDigitDTO;
import dev.petrusdemelo.desafioi.services.dto.UserDTO;

@TestInstance(Lifecycle.PER_CLASS)
class UserControllerTest {
  private UserController userController;
  @Mock private UserService userService;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.userController = new UserController(this.userService);
  }

  @BeforeEach
  void resetMocks(){
    reset(this.userService);
  }

  @Test
  void itShouldCreateUser() {
    // given
    var request = new CreateUserRequestDTO("John Doe", "john.doe@gmail.com", Optional.empty());
    var uuid = UUID.randomUUID();
    when(this.userService.createUser(request.name(), request.email(), request.publicKey()))
      .thenReturn(uuid);

    // when
    var response = this.userController.createUser(request);
    var body = response.getBody();

    // then
    assertEquals(body.id(), uuid);
    verify(this.userService, times(1))
      .createUser(request.name(), request.email(), request.publicKey());
  }

  @Test
  void itShouldGetUserByID() {
    // given
    var uuid = UUID.randomUUID();
    var userDTO = new UserDTO(uuid, "John Doe", "john.doe@gmail.com");    
    when(this.userService.getUserByID(uuid))
      .thenReturn(userDTO);

    // when
    var response = this.userController.getUserByID(uuid);
    var body = response.getBody();

    // then
    assertEquals(body.id(), userDTO.id());
    assertEquals(body.name(), userDTO.name());
    assertEquals(body.email(), userDTO.email());
  }

  @Test
  void itShouldDeleteUserByID() {
    // given
    var uuid = UUID.randomUUID();

    // when
    this.userController.deleteUserByID(uuid);

    // then
    verify(this.userService, times(1))
      .deleteUserByID(uuid);
  }

  @Test
  void itShouldUpdateUserByID() {
    // given
    var uuid = UUID.randomUUID();
    var request = new UpdateUserRequestDTO("John Doe", "john.doe@gmail.com", Optional.empty());
    
    // when
    this.userController.updateUserByID(uuid, request);

    // then
    verify(this.userService, times(1))
      .updateUserByID(uuid, request.name(), request.email(), request.publicKey());
  }

  @Test
  void itShouldReturnAnListOfListOfUniqueDigitDTO() {
    // given
    var uuid = UUID.randomUUID();
    var result = List.of(
      new UniqueDigitDTO(1, "1", 1),
      new UniqueDigitDTO(2, "2", 2),
      new UniqueDigitDTO(3, "3", 3)
    );
    
    when(this.userService.getCalculationsByUserID(uuid))
      .thenReturn(result);

    // when
    var response = this.userController.getCalculationsByUserID(uuid);
    var body = response.getBody().uniqueDigits();

    // then
    verify(this.userService, times(1))
      .getCalculationsByUserID(uuid);
      
    for(var i = 0; i < 3; i++){
      assertEquals(body.get(i).result(), result.get(i).result());
    }
  }

  @Test
  void itShouldCallServiceAndAddPublicKey() {
    // given    
    var uuid = UUID.randomUUID();
    var publicKey = "publicKey";
    var request = new PutPublicKeyRequestDTO(publicKey);

    // when
    this.userController.addPublicKey(uuid, request);

    // then
    verify(this.userService, times(1))
      .addPublicKey(uuid, publicKey);
  }
}
