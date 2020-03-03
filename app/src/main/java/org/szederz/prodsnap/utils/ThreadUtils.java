package org.szederz.prodsnap.utils;

public class ThreadUtils {
  public static void async(Runnable target) {
    new Thread(target).start();
  }
}
