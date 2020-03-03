package org.szederz.prodsnap.utils.net.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.szederz.prodsnap.utils.net.http.RequestFormatter.format;

public class RequestFormatTest {
  @Test
  public void testRequest() {
    assertUnifiedFormat(
      "GET /api/endpoint",
      Request.builder()
        .method("GET")
        .url("http://localhost/api/endpoint"));
  }

  @Test
  public void testHeader() {
    assertUnifiedFormat(
      "GET /api/endpoint\n" +
        "Test-Header: test",
      Request.builder()
        .method("GET")
        .url("http://localhost/api/endpoint")
        .header("Test-Header", "test"));
  }

  @Test
  public void testHeadersAreOrdered() {
    assertUnifiedFormat(
      "GET /api/endpoint\n" +
        "A-Header: test1\n" +
        "B-Header: test2\n" +
        "C-Header: test3",
      Request.builder()
        .method("GET")
        .url("http://localhost/api/endpoint")
        .header("B-Header", "test2")
        .header("C-Header", "test3")
        .header("A-Header", "test1")
    );
  }

  @Test
  public void testHeaderValuesAreOrdered() {
    assertUnifiedFormat(
      "GET /api/endpoint\n" +
        "Test-Header: test1\n" +
        "Test-Header: test2",
      Request.builder()
        .method("GET")
        .url("http://localhost/api/endpoint")
        .header("Test-Header", "test2")
        .header("Test-Header", "test1"));
  }

  @Test
  public void testRequestBody() {
    assertUnifiedFormat(
      "GET /api/endpoint\n" +
        "Test-Header: test\n" +
        "\n" +
        "test message",

      Request.builder()
        .method("GET")
        .url("http://localhost/api/endpoint")
        .header("Test-Header", "test")
        .body("test message")
    );
  }

  @Test
  public void testHeadersAreUnified() {
    assertUnifiedFormat(
      "GET /api/endpoint\n" +
        "Test-Header: test1\n" +
        "Test-Header: test2",
      Request.builder()
        .method("GET")
        .url("http://localhost/api/endpoint")
        .header("tEst-hEAdEr", "test1")
        .header("TEST-HEADER", "test2")
    );
  }

  private void assertUnifiedFormat(String expected, Request.Builder builder) {
    assertEquals(expected, format(builder.build()));
  }
}