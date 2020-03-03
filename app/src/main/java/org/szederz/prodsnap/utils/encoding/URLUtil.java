package org.szederz.prodsnap.utils.encoding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLUtil {
  public String encode(String str) {
    try {
      return URLEncoder.encode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
