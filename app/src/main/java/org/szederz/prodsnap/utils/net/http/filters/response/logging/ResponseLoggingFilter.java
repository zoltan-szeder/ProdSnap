package org.szederz.prodsnap.utils.net.http.filters.response.logging;

import org.szederz.prodsnap.utils.StringUtils;
import org.szederz.prodsnap.utils.net.http.HttpResponseFilter;
import org.szederz.prodsnap.utils.net.http.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseLoggingFilter implements HttpResponseFilter {
  @Override
  public Response apply(Response response) {
    System.err.println(format(response));
    return response;
  }

  public static String format(Response response) {
    List<String> lines = new ArrayList<>();

    for (Map.Entry<String, List<String>> entry : response.getHeaders().entrySet())
      for (String value : entry.getValue())
        if (entry.getKey() == null)
          lines.add(value);
        else
          lines.add(entry.getKey() + ": " + value);

    lines.add(response.getBody());

    return StringUtils.join("\n", lines);
  }
}
