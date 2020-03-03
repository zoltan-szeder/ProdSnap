package org.szederz.prodsnap.utils.net.http.filters.request.hmac;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class HmacSha256Generator {
  private String signatureType = "HmacSHA256";
  private Charset charset = StandardCharsets.UTF_8;
  private final String secret;

  public HmacSha256Generator(String secret) {
    this.secret = secret;
  }

  public byte[] generateHMAC(String message) {
    return generateHMAC(message, this.secret);
  }

  public byte[] generateHMAC(String message, String secret) {
    return generateHMAC(
      secret.getBytes(charset),
      message.getBytes(charset));
  }

  public byte[] generateHMAC(byte[] secretBytes, byte[] messageBytes) {
    try {
      Mac mac = Mac.getInstance(signatureType);
      mac.init(new SecretKeySpec(secretBytes, signatureType));
      return mac.doFinal(messageBytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
