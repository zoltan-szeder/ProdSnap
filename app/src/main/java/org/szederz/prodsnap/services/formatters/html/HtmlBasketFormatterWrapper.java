package org.szederz.prodsnap.services.formatters.html;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.services.formatters.BasketFormatter;

public class HtmlBasketFormatterWrapper implements BasketFormatter {
  private final HtmlBasketFormatter basketItemFormatter;

  public HtmlBasketFormatterWrapper(HtmlBasketFormatter basketItemFormatter) {
    this.basketItemFormatter = basketItemFormatter;
  }

  @Override
  public String format(Basket basket) {
    return "<!DOCTYPE html>" +
      "<html>" +
      createHead() +
      createBody(basket) +
      "</html>";
  }

  private String createHead() {
    return "<head>" +
      "<meta charset=\"utf-8\">" +
      "<style type=\"text/css\" style=\"display:none\">" +
      basketItemFormatter.getCss() +
      "</style>" +
      "</head>";
  }

  private String createBody(Basket basket) {
    return "<body>" +
      basketItemFormatter.format(basket) +
      "</body>";
  }
}
