package org.szederz.prodsnap.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;

public class ListUtils {
  public static <T extends Comparable<T>> List<T> sorted(Collection<T> collection) {
    ArrayList<T> ts = new ArrayList<>(collection);
    sort(ts);
    return ts;
  }

  public static <T> List<T> sorted(Collection<T> collection, Comparator<T> comparator) {
    ArrayList<T> ts = new ArrayList<>(collection);
    sort(ts, comparator);
    return ts;
  }
}
