package org.szederz.prodsnap.utils.net.http.filters.request.logging;

import org.szederz.prodsnap.utils.net.http.HttpRequestFilter;
import org.szederz.prodsnap.utils.net.http.Request;

import static org.szederz.prodsnap.utils.net.http.RequestFormatter.format;

public class RequestLoggingFilter implements HttpRequestFilter {
  @Override
  public Request apply(Request request) {
    System.err.println(format(request));
    return request;
  }
}
