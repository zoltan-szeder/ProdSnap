package org.szederz.prodsnap.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
  public static <T, U> Map<T, U> asMap(Object... args) {
    HashMap<T, U> map = new HashMap<>();

    int i = 0;
    int j = 1;
    while (j < args.length) {
      map.put((T) args[i], (U) args[j]);
      i += 2;
      j += 2;
    }

    return map;
  }

  public static Map<String, String> asFlatMap(String... args) {
    HashMap<String, String> map = new HashMap<>();

    int i = 0;
    int j = 1;
    while (j < args.length) {
      map.put(args[i], args[j]);
      i += 2;
      j += 2;
    }

    return map;
  }


  public static <T extends Comparable<T>, U> Comparator<Map.Entry<T, U>> keyComparatorFor(Map<T, U> map) {
    return new Comparator<Map.Entry<T, U>>() {
      @Override
      public int compare(Map.Entry<T, U> o1, Map.Entry<T, U> o2) {
        return o1.getKey().compareTo(o2.getKey());
      }
    };
  }
}
