package org.szederz.prodsnap.utils.net.http.filters.request;

import org.szederz.prodsnap.utils.function.Supplier;
import org.szederz.prodsnap.utils.net.http.HttpRequestFilter;
import org.szederz.prodsnap.utils.net.http.Request;

import java.util.Map;

public class HeaderFilter implements HttpRequestFilter {
  private final Map<String, Supplier<String>> map;

  public HeaderFilter(Map<String, Supplier<String>> map) {
    this.map = map;
  }

  @Override
  public Request apply(Request request) {
    Request.Builder builder = Request.builder(request);

    for (Map.Entry<String, Supplier<String>> entry : map.entrySet()) {
      builder.header(entry.getKey(), entry.getValue().get());
    }

    return builder.build();
  }
}
