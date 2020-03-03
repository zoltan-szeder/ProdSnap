package org.szederz.prodsnap.utils.net.http.client;

import org.szederz.prodsnap.utils.net.http.HttpRequestFilter;
import org.szederz.prodsnap.utils.net.http.Request;

import java.net.URL;
import java.util.List;

class RequiredHeadersRequestFilter implements HttpRequestFilter {
  @Override
  public Request apply(Request request) {
    return new RequestHeaderBuilder(request, Request.builder(request))
      .addHeaderIfNotExists("Host", getHost(request))
      .addHeaderIfNotExists("Accept-Encoding", "gzip")
      .addHeaderIfNotExists("Connection", "Keep-Alive")
      .addHeaderIfNotExists("User-Agent", "httpClient/1.0.0")
      .addHeaderIfNotExists("Accept", "*/*")
      .addContentLength()
      .build();
  }

  private String getHost(Request request) {
    try {
      URL url = new URL(request.getUrl());

      if (url.getPort() < 0)
        return url.getHost();

      return url.getHost() + ":" + url.getPort();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static class RequestHeaderBuilder {
    private Request request;
    private Request.Builder builder;

    public RequestHeaderBuilder(Request request, Request.Builder builder) {
      this.request = request;
      this.builder = builder;
    }

    public RequestHeaderBuilder addHeaderIfNotExists(String name, String value) {
      if (!containsHeader(name))
        builder.header(name, value);

      return this;
    }

    private boolean containsHeader(String header) {
      List<String> host = request.getHeaders().get(header);
      return host != null && !host.isEmpty();
    }

    public Request build() {
      return builder.build();
    }

    public RequestHeaderBuilder addContentLength() {
      if (bodyShouldNotBeSent())
        return this;

      this.builder.header("Content-Length", String.valueOf(request.getBody().length()));

      return this;
    }

    private boolean bodyShouldNotBeSent() {
      return request.getBody() == null || "GET".equals(request.getMethod());
    }
  }
}
