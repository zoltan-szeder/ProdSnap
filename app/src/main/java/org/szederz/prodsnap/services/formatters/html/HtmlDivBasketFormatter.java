package org.szederz.prodsnap.services.formatters.html;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;

import java.util.Locale;

public class HtmlDivBasketFormatter implements HtmlBasketFormatter {
  private final BasketItemFormatter basketItemFormatter;

  public HtmlDivBasketFormatter(BasketItemFormatter basketItemFormatter) {
    this.basketItemFormatter = basketItemFormatter;
  }

  @Override
  public String getCss() {
    return ".basket_item {" +
      " text-align: center;" +
      " padding: 20px 0px 20px 0px;" +
      " border-bottom-style: solid;" +
      " border-bottom-width: 1px;" +
      "}" +
      "img {" +
      " width: 50%;" +
      "}";
  }

  @Override
  public String format(Basket basket) {
    StringBuilder stringBuilder = new StringBuilder();

    for (String commentLine : basket.getCustomer().getDetails().split("\n")) {
      stringBuilder.append("<p>").append(commentLine).append("</p>");
    }

    for (BasketItem item : basket.getAllItem()) {
      stringBuilder.append(basketItemFormatter.format(item));
    }

    stringBuilder.append(
      String.format(
        Locale.getDefault(),
        "<h2 class=\"summary\">Összesen %d Ft</h2>",
        getAmount(basket)
      ));

    return stringBuilder.toString();
  }

  private int getAmount(Basket basket) {
    int sum  = 0;

    for (BasketItem item : basket.getAllItem()) {
      sum += item.getDetails().getPrice() * item.getAmount();
    }

    return sum;
  }
}
