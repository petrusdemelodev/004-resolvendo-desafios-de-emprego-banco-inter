package dev.petrusdemelo.desafioi.domain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;

import dev.petrusdemelo.desafioi.domain.exceptions.InvalidPublicKeySizeException;
import dev.petrusdemelo.desafioi.domain.exceptions.UserIsAlreadyEncryptedException;
import dev.petrusdemelo.desafioi.shared.PublicKeyParser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;
  
  @Column(name = "name", nullable = false, columnDefinition = "TEXT")
  @Setter(AccessLevel.NONE)
  private String name;

  @Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true)
  @Setter(AccessLevel.NONE)
  private String email;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @Builder.Default
  private List<UniqueDigit> uniqueDigits = new ArrayList<>();

  @Column(name = "private_key", nullable = true, columnDefinition = "TEXT")
  private String publicKey;

  @Column(name = "is_encrypted", nullable = false, columnDefinition = "BOOLEAN")
  private boolean isEncrypted;

  @Transient
  private Cipher cipher;

  public void setEmail(String email) {
    this.email = this.getEncryptedValue(email);
  }

  public void setName(String name) {
    this.name = this.getEncryptedValue(name);
  }

  public void addUniqueDigit(UniqueDigit uniqueDigit) {
    var newUniqueDigit = uniqueDigit.clone();
    newUniqueDigit.setUser(this);
    this.uniqueDigits.add(newUniqueDigit);
  }

  public void setPublicKey(String publicKey){
    if(this.isEncrypted){
      throw new UserIsAlreadyEncryptedException();
    }

    this.publicKey = publicKey;
    this.isEncrypted = true;
    this.email = this.getEncryptedValue(this.email);
    this.name = this.getEncryptedValue(this.name);
  }

  private String getEncryptedValue(String value){
    if(!this.isEncrypted){
      return value;
    }
    
    if(this.cipher == null){
      this.initCipher();
    }

    try {
      var bytes = this.cipher
          .doFinal(value.getBytes());

      return Base64.getEncoder().encodeToString(bytes);
    } catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  private void initCipher(){
    if(this.publicKey == null){
      throw new RuntimeException("Public key is null");
    }

    PublicKey publicKey = PublicKeyParser.parsePublicKey(this.publicKey);
    Cipher cipherInstance;
    try {
      cipherInstance = Cipher.getInstance("RSA");
      cipherInstance.init(Cipher.ENCRYPT_MODE, publicKey);
    } catch(Exception e){
      throw new RuntimeException(e);
    }

    int keySize = cipherInstance.getOutputSize(0) * Byte.SIZE;

    if(keySize != 2048){
      throw new InvalidPublicKeySizeException("Invalid key size");
    }

    this.cipher = cipherInstance;
  }
}
