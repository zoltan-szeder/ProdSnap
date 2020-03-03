package org.szederz.prodsnap.services.formatters.html;

import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.services.transfer.BarcodeFormatter;

import java.util.Locale;

public class BasketItemFormatter {
  private final BarcodeFormatter barcodeFormatter;

  public BasketItemFormatter() {
    this(new CIDImageTagBarcodeFormatter());
  }

  public BasketItemFormatter(BarcodeFormatter barcodeFormatter) {
    this.barcodeFormatter = barcodeFormatter;
  }

  public String format(BasketItem item) {
    return "<div class=\"basket_item\">" +
      formatDetails(item) +
      barcodeFormatter.format(item.getId()) +
      "</div>";
  }

  private String formatDetails(BasketItem item) {
    return String.format(
      Locale.getDefault(),
      "<h1>%d db</h1><h2>%s</h2><h2>%s</h2>",
      item.getAmount(),
      item.getDetails().getName(),
      item.getDetails().getFormattedPrice()
    );
  }
}
