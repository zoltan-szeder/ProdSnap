package org.szederz.prodsnap.utils.net.http.filters.request.hmac;

import org.junit.Test;

import static org.junit.Assert.*;

public class HmacSha256GeneratorTest {
  @Test
  public void testHMACGeneration() throws Exception {
    byte[] actual = new HmacSha256Generator("secret").generateHMAC("test message");
    String expected = "3BCEBF43C85D20BBA6E3B6BA278AF1D2BA3AB0D57DE271B0AD30B833E851C5A6";

    assertEquals(expected, toHex(actual));
  }

  public String toHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder();
    for (byte b : bytes) {
      builder.append(String.format("%02X", b));
    }
    return builder.toString();
  }

}