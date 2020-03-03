package org.szederz.prodsnap.services.formatters.html;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;

import java.util.List;

public class HtmlTableBasketFormatter implements HtmlBasketFormatter {
  @Override
  public String format(Basket basket) {
    return "<table class=\"basket\">" +
      formatItems(basket.getAllItem()) +
      "</table>";
  }

  public String formatItems(List<BasketItem> items) {
    StringBuilder builder = new StringBuilder();
    int i = 1;
    for (BasketItem item : items) {
      builder.append("<tr>")
        .append("<td>").append(i).append("</td>")
        .append("<td>").append(item.getDetails().getName()).append("</td>")
        .append("<td>").append(item.getId()).append("</td>")
        .append("<td>").append(item.getAmount()).append("</td>")
        .append("</tr>");
      i++;
    }
    return builder.toString();
  }

  @Override
  public String getCss() {
    return ".basket {\n" +
      "  border-collapse: collapse;\n" +
      "}\n" +
      ".basket td {\n" +
      "  border: 1px solid #B0B0B0;\n" +
      "  padding: 5px 10px;\n" +
      "}\n" +
      ".basket th {\n" +
      "  font-size: 14px;\n" +
      "  border: 1px solid #B0B0B0;\n" +
      "  padding: 5px 10px;\n" +
      "}\n" +
      ".basket tr:nth-of-type(even) {\n" +
      "  background-color: #E0E0E0;\n" +
      "}";
  }
}
