package org.szederz.prodsnap.utils.encoding;

import android.util.Base64;

public class Base64Util {
  public String encode(String authentication) {
    return encode(authentication.getBytes());
  }

  public String encode(byte[] bytes) {
    return Base64.encodeToString(bytes, Base64.DEFAULT).replaceAll("\\s", "");
  }
}
