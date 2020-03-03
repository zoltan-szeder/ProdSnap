package org.szederz.prodsnap.configuration;

import java.util.Calendar;
import java.util.Locale;

public class BarcodeCache {
  private final String BARCODE_PREFIX = "item.details.";
  private final String LAST_UPDATE = "item.last-updated";

  private final Configuration conf;

  public BarcodeCache(Configuration conf) {
    this.conf = conf;
  }

  public String getBarcodeItem(String barcode) {
    return conf.get(BARCODE_PREFIX + barcode)
      .orElseGet(null);
  }

  public void setBarcodeItem(String barcode, String item) {
    conf.set(BARCODE_PREFIX + barcode, item);
  }

  public boolean hasExpired() {
    return !conf.get(LAST_UPDATE)
      .orElseGet("1970-01-01")
      .equals(formatCalendar(Calendar.getInstance()));
  }

  public void commit() {
    conf.set(LAST_UPDATE, formatCalendar(Calendar.getInstance()));
  }

  private String formatCalendar(Calendar calendar) {
    return String.format(Locale.getDefault(), "%d-%d-%d",
      calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.DAY_OF_MONTH));
  }
}
