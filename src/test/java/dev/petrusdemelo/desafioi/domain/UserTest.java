package dev.petrusdemelo.desafioi.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dev.petrusdemelo.desafioi.domain.exceptions.InvalidPublicKeySizeException;
import dev.petrusdemelo.desafioi.domain.exceptions.UserIsAlreadyEncryptedException;
import dev.petrusdemelo.desafioi.shared.PublicKeyParser.InvalidPublicKeyException;

@TestInstance(Lifecycle.PER_CLASS)
class UserTest {
  private String publicKey;
  private PrivateKey privateKey;
  private Cipher cipher;

  @BeforeAll
  void setUp(){
    try {
       // Generate RSA key pair
       var keyPairGen = KeyPairGenerator.getInstance("RSA");
       keyPairGen.initialize(2048);
       var keyPair = keyPairGen.generateKeyPair();
       this.privateKey = keyPair.getPrivate();
       this.publicKey = this.convertPublicKeyToPEM(keyPair.getPublic());

       // Initialize cipher with private key
       this.cipher = Cipher.getInstance("RSA");
       this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
    } catch (Exception e) {
      throw new RuntimeException("Error setting up test", e);
    }
  }
  
  private String convertPublicKeyToPEM(PublicKey publicKey) {
    String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    return "-----BEGIN PUBLIC KEY-----\n" + base64PublicKey + "\n-----END PUBLIC KEY-----";
  }

  @Test
  void itShouldAddPublicKeyToUserAndEncryptValues() throws Exception {
    // given
    var user = User.builder()
      .name("John Doe")
      .email("john.doe@gmail.com")
      .build();
    
    // when
    user.setPublicKey(this.publicKey);

    // then
    assertTrue(user.isEncrypted());
    assertEquals(this.decrypt(user.getEmail()), "john.doe@gmail.com");
    assertEquals(this.decrypt(user.getName()), "John Doe");
  }

  @Test
  void itShouldThrowAnErrorIfPublicKeyIsNotValid(){
    // given
    var user = User.builder()
      .name("John Doe")
      .email("john.doe@gmail.com")
      .build();
  
    // when && then
    assertThrows(InvalidPublicKeyException.class, 
      () -> user.setPublicKey("aaaaaaaaa")
    );
  }

  @Test
  void itShouldThrowAnErrorIfPublicKeyIsValidButNotEqualsTo2048(){
   // given
   var user = User.builder()
      .name("John Doe")
      .email("john.doe@gmail.com")
      .build();
  
    var not2048PublicKey = "-----BEGIN PUBLIC KEY-----MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGCIOrF90x9mOs9ls7BP50CGFAZGra+Mh+aP0uPBuB7Kci1dV9jA9ElU5Wb9/avwKiqksOPRUU0ZFEJNvJvzKvM/9pwVLQWGnh0cesSA+8mBsjpBbUN4ALVOKg9GpNvyXLoLUqY9lQ9Q6HKXGedF+S2gvlNKCDlbvT6gbc8gNvGTAgMBAAE=-----END PUBLIC KEY-----";

    // when && then
    assertThrows(InvalidPublicKeySizeException.class, 
      () -> user.setPublicKey(not2048PublicKey)
    );
  }

  @Test
  void itShouldThrowAnErrorIfAddPublicKeyToAnUserThatIsAlreadyEncrypted() throws Exception{
    // given
    var user = User.builder()
      .name("John Doe")
      .email("john.doe@gmail.com")
      .build();
    
    // when
    user.setPublicKey(this.publicKey);

    // then
    assertThrows(UserIsAlreadyEncryptedException.class, 
      () -> user.setPublicKey(this.publicKey)
    );
  }

  private String decrypt(String encryptedString) throws Exception {
    byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);
    byte[] decryptedBytes = this.cipher.doFinal(encryptedBytes);
    return new String(decryptedBytes);
  }
}
