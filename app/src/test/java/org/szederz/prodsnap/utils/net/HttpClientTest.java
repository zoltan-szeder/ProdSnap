package org.szederz.prodsnap.utils.net;

import org.junit.Before;
import org.junit.Test;
import org.szederz.prodsnap.utils.function.Function;
import org.szederz.prodsnap.utils.net.http.client.HttpClient;
import org.szederz.prodsnap.utils.net.http.HttpRequestFilter;
import org.szederz.prodsnap.utils.net.http.HttpResponseFilter;
import org.szederz.prodsnap.utils.net.http.Request;
import org.szederz.prodsnap.utils.net.http.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpClientTest {
  private HttpURLConnection conn = mock(HttpURLConnection.class);
  private ByteArrayOutputStream os = new ByteArrayOutputStream();
  private String calledEndpoint;

  private HttpClient client = new HttpClient(new Function<String, HttpURLConnection>() {
    @Override
    public HttpURLConnection apply(String s) {
      return toConnection(s);
    }
  });

  private HttpURLConnection toConnection(String url) {
    this.calledEndpoint = url;
    return conn;
  }

  @Before
  public void setUp() throws Exception {
    when(conn.getOutputStream()).thenReturn(os);
  }

  @Test
  public void testSimpleGetCall() throws Exception {
    String url = "https://localhost:8080";

    client.send(Request.builder()
      .url(url)
      .method("GET")
      .build());

    verify(conn).setRequestMethod("GET");
    assertEquals(url, calledEndpoint);
    verify(conn, never()).getOutputStream();
  }

  @Test
  public void testHeaders() throws Exception {
    client.send(Request.builder()
      .method("GET")
      .url("https://localhost:8080")
      .header("Host", "some.url")
      .header("User-Agent", "curl/7.1.2")
      .header("Accept", "text/plain")
      .header("Key1", "value1")
      .header("Key2", "value2")
      .build());

    verify(conn).setRequestProperty("Host", "some.url");
    verify(conn, never()).setRequestProperty("Host", "localhost:8080");
    verify(conn).setRequestProperty("User-Agent", "curl/7.1.2");
    verify(conn, never()).setRequestProperty("User-Agent", "httpClient/1.0.0");
    verify(conn).setRequestProperty("Accept", "text/plain");
    verify(conn, never()).setRequestProperty("Accept", "*/*");
    verify(conn).setRequestProperty("Key1", "value1");
    verify(conn).setRequestProperty("Key2", "value2");
  }

  @Test
  public void testRequestBody() throws Exception {
    client.send(Request.builder()
      .method("POST")
      .url("https://localhost")
      .body("test request")
      .build());

    verify(conn).setRequestProperty("Content-Length", String.valueOf("test request".length()));
    verify(conn).setRequestProperty("Host", "localhost");
    verify(conn).setRequestProperty("User-Agent", "httpClient/1.0.0");
    verify(conn).setRequestProperty("Accept", "*/*");
    verify(conn).setDoOutput(true);
    assertEquals(os.toString(), "test request");
  }

  @Test
  public void testRequestBodyForGetIsNotAllowed() throws Exception {
    client.send(Request.builder()
      .method("GET")
      .url("https://localhost:8080")
      .body("test request")
      .build());

    verify(conn, never()).setRequestProperty(eq("Content-Length"), anyString());
    verify(conn).setRequestProperty(eq("Host"), eq("localhost:8080"));
    verify(conn, never()).getOutputStream();
  }

  @Test
  public void testSuccessfulResponse() throws Exception {
    setResponse("response");
    setContentLength("response".length());
    Response response = client.send(Request.builder()
      .url("https://localhost:8080")
      .method("GET")
      .build());

    assertEquals("response", response.getBody());
  }

  @Test
  public void testInputIsReadWithoutContentLength() throws Exception {
    setResponse("response");

    Response response = client.send(Request.builder()
      .url("https://localhost:8080")
      .method("GET")
      .build());

    assertEquals("response", response.getBody());
  }

  @Test
  public void testFailureResponse() throws Exception {
    setErrorResponse(404, "error");
    setContentLength("error".length());

    Response response = client.send(Request.builder()
      .url("https://localhost:8080")
      .method("GET")
      .build());

    assertEquals("error", response.getBody());
  }

  @Test
  public void testRequestFilters() throws Exception {
    client.withRequestFilters(new HttpRequestFilter() {
      @Override
      public Request apply(Request request) {
        return Request.builder(request)
          .header("Some-Header", "someValue")
          .build();
      }
    });

    client.send(Request.builder()
      .url("https://localhost:8080")
      .method("GET")
      .build());

    verify(conn).setRequestProperty("Some-Header", "someValue");
  }

  @Test(expected = RuntimeException.class)
  public void testResponseFilters() throws Exception {
    client.withResponseFilters(new HttpResponseFilter() {
      @Override
      public Response apply(Response response) {
        if (response.getResponseCode() >= 400) {
          throw new RuntimeException("Response is above 400:\n" + response.getBody());
        }
        return response;
      }
    });

    setErrorResponse(400, "error");

    client.send(Request.builder()
      .url("https://localhost:8080")
      .method("GET")
      .build());
  }

  private void setResponse(String responseBody) throws IOException {
    setResponseStatus(200);

    when(conn.getDoInput()).thenReturn(true);
    when(conn.getInputStream()).thenReturn(new ByteArrayInputStream(responseBody.getBytes()));
  }


  private void setErrorResponse(int statusCode, String responseBody) throws IOException {
    setResponseStatus(statusCode);

    when(conn.getDoInput()).thenReturn(true);
    when(conn.getErrorStream()).thenReturn(new ByteArrayInputStream(responseBody.getBytes()));
  }

  private void setContentLength(int length) {
    when(conn.getHeaderField("Content-Length")).thenReturn(String.valueOf(length));
  }

  private void setResponseStatus(int status) throws IOException {
    when(conn.getResponseCode()).thenReturn(status);
  }
}