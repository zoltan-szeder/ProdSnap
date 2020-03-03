package org.szederz.prodsnap.utils.net.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
  private final int responseCode;
  private final Map<String, List<String>> headers;
  private final String body;

  public Response(int responseCode, Map<String, List<String>> headers, String response) {
    this.responseCode = responseCode;
    this.headers = headers;
    this.body = response;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public Map<String, List<String>> getHeaders() {
    return new HashMap<>(headers);
  }

  public String getBody() {
    return body;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static Builder builder(Response response){
    return new Builder(response);
  }


  public static class Builder {
    private int status = 200;
    private Map<String, List<String>> headers = new HashMap<>();
    private String body = "";

    public Builder() {
    }

    public Builder(Response response) {
      status = response.getResponseCode();
      headers.putAll(response.getHeaders());
      body = response.body;
    }

    public Builder status(int status) {
      this.status = status;
      return this;
    }

    public Builder header(String key, List<String> values) {
      if (!headers.containsKey(key))
        headers.put(key, new ArrayList<String>());

      headers.get(key).addAll(values);

      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Response build() {
      return new Response(status, headers, body);
    }

  }
}
