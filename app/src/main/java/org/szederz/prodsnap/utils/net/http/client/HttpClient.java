package org.szederz.prodsnap.utils.net.http.client;

import org.szederz.prodsnap.utils.function.Function;
import org.szederz.prodsnap.utils.net.http.HttpRequestFilter;
import org.szederz.prodsnap.utils.net.http.HttpResponseFilter;
import org.szederz.prodsnap.utils.net.http.Request;
import org.szederz.prodsnap.utils.net.http.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HttpClient {
  private final Function<String, HttpURLConnection> connectionBuilder;
  private List<HttpRequestFilter> requestFilters = new ArrayList<>(asList(
    (HttpRequestFilter) new RequiredHeadersRequestFilter()
  ));
  private List<HttpResponseFilter> responseFilters = new ArrayList<>();

  public HttpClient() {
    this(new Function<String, HttpURLConnection>() {
      @Override
      public HttpURLConnection apply(String s) {
        return createConnectionFrom(s);
      }
    });
  }

  public HttpClient(Function<String, HttpURLConnection> connectionBuilder) {
    this.connectionBuilder = connectionBuilder;
  }

  private static HttpURLConnection createConnectionFrom(String url) {
    try {
      return (HttpURLConnection) new URL(url).openConnection();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Response send(Request request) {
    try {
      return applyResponseFilters(
        sendInner(
          applyRequestFilters(request)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Response applyResponseFilters(Response response) {
    for (HttpResponseFilter filter : responseFilters) {
      response = filter.apply(response);
    }
    return response;
  }

  private Request applyRequestFilters(Request request) {
    Request filteredRequest = request;
    for (HttpRequestFilter filter : requestFilters) {
      filteredRequest = filter.apply(filteredRequest);
    }
    return filteredRequest;
  }

  private Response sendInner(Request request) throws IOException {
    return new VolatileHttpClient(connectionBuilder.apply(request.getUrl()))
      .send(request);
  }

  public HttpClient withRequestFilters(HttpRequestFilter... filters) {
    this.requestFilters.addAll(asList(filters));
    return this;
  }

  public HttpClient withResponseFilters(HttpResponseFilter... filters) {
    this.responseFilters.addAll(asList(filters));
    return this;
  }
}
