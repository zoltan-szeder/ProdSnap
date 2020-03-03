package org.szederz.prodsnap.utils.net.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.szederz.prodsnap.utils.StringUtils.join;

public class Request {
  private final String method;
  private final String url;
  private final Map<String, List<String>> headers;
  private final String body;

  private Request(String method, String url, Map<String, List<String>> headers, String requestBody) {
    this.method = method;
    this.url = url;
    this.headers = headers;
    this.body = requestBody;
  }

  public String getMethod() {
    return method;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public String getBody() {
    return body;
  }

  public String getUrl() {
    return url;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(Request request) {
    return new Builder(request);
  }

  public static String toHeaderCase(String key) {
    ArrayList<String> newList = new ArrayList<>();

    for (String word : key.split("-"))
      if (word.isEmpty())
        newList.add(word);
      else
        newList.add(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());

    return join("-", newList);
  }

  public static class Builder {
    private String method;
    private String url = "http://localhost";
    private Map<String, List<String>> headers = new HashMap<>();
    private String body = null;

    private Builder() {
    }

    public Builder(Request request) {
      method = request.getMethod();
      url = request.getUrl();
      headers.putAll(request.getHeaders());
      body = request.getBody();
    }

    public Builder method(String method) {
      this.method = method;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder header(String key, String value) {
      key = toHeaderCase(key);
      if (!headers.containsKey(key)) {
        headers.put(key, new ArrayList<String>());
      }

      headers.get(key).add(value);

      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Request build() {
      return new Request(method, url, new HashMap<>(headers), body);
    }
  }
}
