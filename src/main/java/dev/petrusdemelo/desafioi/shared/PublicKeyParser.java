package dev.petrusdemelo.desafioi.shared;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyParser {
  private static final String ALGORITHM = "RSA";
	private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
	private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";

  private PublicKeyParser() {}

  public static PublicKey parsePublicKey(String publicKey) {
    PublicKey pubKey = null;

    try {
      String base64PublicKey = publicKey
        .replace(PUBLIC_KEY_HEADER, "")
        .replace(PUBLIC_KEY_FOOTER, "")
        .replaceAll("\\s", "")
        .trim();      

      var keyBytes = Base64.getDecoder().decode(base64PublicKey);
      var spec = new X509EncodedKeySpec(keyBytes);
      var keyFactory = KeyFactory.getInstance(ALGORITHM);
      pubKey = keyFactory.generatePublic(spec);
      return pubKey;
    } catch (Exception e) {
      throw new InvalidPublicKeyException("Invalid public key", e);
    }
  }

  public static class InvalidPublicKeyException extends RuntimeException {
    public InvalidPublicKeyException(String message, Throwable cause) {
      super(message, cause);
    }
  }  
}
