package org.szederz.prodsnap.services.formatters;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.utils.function.Supplier;

import java.util.Map;
import java.util.logging.Logger;

public class SwitchableBasketFormatter implements BasketFormatter {
  private final Supplier<String> conf;
  private final Map<String, BasketFormatter> formatters;

  SwitchableBasketFormatter(
    Supplier<String> conf,
    Map<String, BasketFormatter> formatters
  ) {
    this.conf = conf;
    this.formatters = formatters;
  }

  @Override
  public String format(Basket basket) {
    BasketFormatter formatter = formatters.get(conf.get());
    if (formatter == null) {
      Logger.getGlobal().warning("Unspecified formatter");
      return "";
    }
    return formatter.format(basket);
  }
}
