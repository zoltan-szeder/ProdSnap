package org.szederz.prodsnap.utils.net.http;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.szederz.prodsnap.utils.ListUtils.sorted;
import static org.szederz.prodsnap.utils.MapUtils.keyComparatorFor;
import static org.szederz.prodsnap.utils.StringUtils.join;

public class RequestFormatter {
  public static String format(Request request) {
    try {
      List<String> lines = new ArrayList<>();
      lines.add(request.getMethod() + " " + new URL(request.getUrl()).getPath());
      addHeadersTo(lines, request);
      addBodyTo(lines, request);
      return join("\n", lines);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void addBodyTo(List<String> lines, Request request) {
    String body = request.getBody();

    if (body != null) {
      lines.add("");
      lines.add(body);
    }
  }

  private static void addHeadersTo(List<String> lines, Request request) {
    Map<String, List<String>> headers = request.getHeaders();
    for (Map.Entry<String, List<String>> header : sorted(headers.entrySet(), keyComparatorFor(headers))) {
      for (String value : sorted(header.getValue())) {
        lines.add(header.getKey() + ": " + value);
      }
    }
  }
}
