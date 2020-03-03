package org.szederz.prodsnap.utils;

import java.util.List;

public class StringUtils {
  public static String join(String delimiter, List<String> values) {
    return join(delimiter, values.toArray(new String[0]));
  }

  public static String join(String delimiter, String... values) {
    if (values.length == 0)
      return "";

    StringBuilder builder = new StringBuilder();
    builder.append(values[0]);

    for (int i = 1; i < values.length; i++)
      builder.append(delimiter)
        .append(values[i]);

    return builder.toString();
  }
}
