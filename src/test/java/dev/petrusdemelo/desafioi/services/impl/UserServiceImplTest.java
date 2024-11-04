package dev.petrusdemelo.desafioi.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.petrusdemelo.desafioi.domain.UniqueDigit;
import dev.petrusdemelo.desafioi.domain.User;
import dev.petrusdemelo.desafioi.repository.UserRepository;
import dev.petrusdemelo.desafioi.services.exceptions.EmailAlreadyExistsException;
import dev.petrusdemelo.desafioi.services.exceptions.UserNotFoundException;

@TestInstance(Lifecycle.PER_CLASS)
class UserServiceImplTest {
  private UserServiceImpl userServiceImpl;
  @Mock private UserRepository userRepository;

  @BeforeAll
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.userServiceImpl = new UserServiceImpl(this.userRepository);
  }
  
  @BeforeEach
  void resetMocks(){
    reset(this.userRepository);
  }

  @Test
  void itShouldCreateAnUserAndSaveOnRepository() {
    // given
    var email = "john.doe@gmail.com";
    var name = "John Doe";

    when(this.userRepository.findByEmail(email))
      .thenReturn(Optional.empty());

    // when
    this.userServiceImpl.createUser(name, email, Optional.empty());

    // then
    var captor = ArgumentCaptor.forClass(User.class);

    verify(this.userRepository, times(1))
      .save(captor.capture());

    var user = captor.getValue();
    assertEquals(email, user.getEmail());
    assertEquals(name, user.getName());
  }

  void itShouldThrowAnErrorIfEmailAlreadyExists() {
    // given
    var email = "john.doe@gmail.com";
    var name = "John Doe";

    when(this.userRepository.findByEmail(email))
      .thenReturn(Optional.of(User.builder().build()));

    // when & then
    assertThrows(EmailAlreadyExistsException.class, () -> {
      this.userServiceImpl.createUser(name, email, Optional.empty());
    });
    
    verify(this.userRepository, times(0))
      .save(any());
  }

  @Test
  void itShouldReturnAnUserDTOIfUserExists() {
    // given
    var uuid = UUID.randomUUID();
    var user = User.builder()
                .id(uuid)
                .name("John Doe")
                .email("john.doe@gmail.com")  
                .build();
    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.of(user));

    // when
    var userDTO = this.userServiceImpl.getUserByID(uuid);

    // then
    assertEquals(uuid, userDTO.id());
    assertEquals(user.getName(), userDTO.name());
    assertEquals(user.getEmail(), userDTO.email());

    verify(this.userRepository, times(1))
      .findById(uuid);
  }

  @Test
  void itShouldThrowAnErrorIfUserDoesnotExist() {
    // given
    var uuid = UUID.randomUUID();
    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      this.userServiceImpl.getUserByID(uuid);
    });

    verify(this.userRepository, times(1))
      .findById(uuid);
  }

  @Test
  void itShouldDeleteUserByID() {
    // given
    var uuid = UUID.randomUUID();

    // when
    this.userServiceImpl.deleteUserByID(uuid);

    // then
    verify(this.userRepository, times(1))
      .deleteById(uuid);
  }

  @Test
  void itShouldUpdateUserByID() {
    // given
    var uuid = UUID.randomUUID();
    var user = User.builder()
                .id(uuid)
                .name("John Doe")
                .email("john.doe@gmail.com")
                .build();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.of(user));  
    
    // when
    this.userServiceImpl.updateUserByID(uuid, "Jane Doe", "jane.doe@gmail.com", Optional.empty());

    // then
    var captor = ArgumentCaptor.forClass(User.class);
    verify(this.userRepository, times(1))
      .save(captor.capture());
    
    var updatedUser = captor.getValue();
    assertEquals("Jane Doe", updatedUser.getName());
    assertEquals("jane.doe@gmail.com", updatedUser.getEmail());
  }

  @Test
  void itShouldThrowAnErrorIfUserDoesnotExistWhenUpdating() {
    // given
    var uuid = UUID.randomUUID();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.empty());  
    
    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      this.userServiceImpl.updateUserByID(uuid, "Jane Doe", "jane.doe@gmail.com", Optional.empty());
    });

    verify(this.userRepository, times(0))
      .save(any());
  }

  @Test
  void itShouldReturnAnListOfUniqueDigitsIfUserExists(){
    // given
    var uuid = UUID.randomUUID();
    var uniqueDigit = new UniqueDigit(BigInteger.valueOf(9875), 4);
    var user = User.builder()
                .id(uuid)
                .name("John Doe")
                .email("john.doe@gmail.com")
                .uniqueDigits(List.of(uniqueDigit))
                .build();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.of(user));  
    
    // when
    var listOfUniqueDigits = this.userServiceImpl.getCalculationsByUserID(uuid);

    // then
    assertEquals(1, listOfUniqueDigits.size());
    assertEquals(uniqueDigit.getResult(), listOfUniqueDigits.get(0).result());
    assertEquals(uniqueDigit.getNumber(), listOfUniqueDigits.get(0).number());
    assertEquals(uniqueDigit.getK(), listOfUniqueDigits.get(0).k());
  }

  @Test
  void itShouldThrowAnErrorIfUserDoesntExist(){
    // given
    var uuid = UUID.randomUUID();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.empty());  
    
    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      this.userServiceImpl.getCalculationsByUserID(uuid);
    });
  }

  @Test
  void itShouldReturnAnErrorIfUserDoesnotExistWhenAddingPublicKey(){
    // given
    var uuid = UUID.randomUUID();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.empty());  
    
    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      this.userServiceImpl.addPublicKey(uuid, "publicKey");
    });

    verify(this.userRepository, times(0))
      .save(any());
  }

  @Test
  void itShouldAddPublicKeyToUserIfUserExists(){
    // given
    var validPublicKey = "-----BEGIN PUBLIC KEY----- MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQB8lgwSZCCJD2C+eSIok1X3FCbitOCrbf/Nmi1k1e1g0QBQdPiANU9Zzpl0pujK+E9Gtf585R5UfnMzPR8afq0bAIBmiysLGuP5WjT9cGcrwitgqPU4gCvoRuBmbMvfPKs1TJcmjt2h+fgyZoSDLkZQf2FCe4yEjDd/TnG2UWVAvbSmbr7UGE/tpnreQMPtQk6t/FLHXp5fJ3uloxDKMQAiH9aMZvgQgikBt70NzoruNvJhXutuu6qWXNfgYI8MIcoUzE6iAx8bNoqS1VFA3WB8X/WcathU8J+jfLa+GJu+cODXJem3bquD9ZPfUubDBzZxm1nsnXC8uF2eFliP/sMBAgMBAAE= -----END PUBLIC KEY-----";

    var uuid = UUID.randomUUID();

    var user = User.builder()
          .id(uuid)
          .name("John Doe")
          .email("john.doe@gmail.com")
          .build();

    when(this.userRepository.findById(uuid))
      .thenReturn(Optional.of(user));  

    // when
    this.userServiceImpl.addPublicKey(uuid, validPublicKey);
    
    // then
    var captor = ArgumentCaptor.forClass(User.class);

    verify(this.userRepository, times(1))
      .save(captor.capture());
    
    var updatedUser = captor.getValue();
    assertEquals(validPublicKey, updatedUser.getPublicKey());
  }
}
