package org.szederz.prodsnap.services.formatters.html;

import org.szederz.prodsnap.services.transfer.BarcodeFormatter;

public class CIDImageTagBarcodeFormatter implements BarcodeFormatter {
  @Override
  public String format(String barcode) {
    return String.format(
      "<img src=\"cid:%s\" alt=\"%s\" />" +
        "<h2>%s</h2>",
      barcode, barcode, formatBarcode(barcode));
  }

  private String formatBarcode(String barcode) {
    return barcode.substring(0, 1) + " " +
      barcode.substring(1, 6) + " " +
      barcode.substring(6, 12) + " " +
      barcode.substring(12, 13);
  }
}
