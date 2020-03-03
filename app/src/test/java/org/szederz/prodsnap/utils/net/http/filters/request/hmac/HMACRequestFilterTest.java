package org.szederz.prodsnap.utils.net.http.filters.request.hmac;

import org.junit.Test;
import org.szederz.prodsnap.utils.net.http.Request;

import static org.junit.Assert.assertEquals;
import static org.szederz.prodsnap.utils.function.Functional.functionOf;
import static org.szederz.prodsnap.utils.function.Functional.supplierOf;

public class HMACRequestFilterTest {

  @Test
  public void testHMACGeneration() {
    Request request = createFilter("secret", 123456789L)
      .apply(Request.builder()
        .method("GET")
        .url("https://localhost:8080/api/test")
        .build());

    assertEquals("123456789", request.getHeaders().get("Authorization-Time").get(0));
    assertEquals("HMAC <mocked data>", request.getHeaders().get("Authorization").get(0));
  }

  private HMACRequestFilter createFilter(String secret, final long currentTime) {
    return new HMACRequestFilter(
      supplierOf(currentTime),
      functionOf(byte[].class, "<mocked data>"),
      supplierOf(secret)
    );
  }
}