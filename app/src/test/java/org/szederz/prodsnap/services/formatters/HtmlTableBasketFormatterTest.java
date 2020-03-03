package org.szederz.prodsnap.services.formatters;

import org.junit.Test;
import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.services.formatters.html.HtmlTableBasketFormatter;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HtmlTableBasketFormatterTest {
  private HtmlTableBasketFormatter formatter = new HtmlTableBasketFormatter();

  @Test
  public void testTable() {
    assertEquals(
      "<table class=\"basket\">" +
        "</table>",
      formatter.format(
        Basket.builder()
          .items(new ArrayList<BasketItem>())
          .build()));
  }

  @Test
  public void testOneItem() {
    String actual = formatter.format(Basket.builder()
      .items(asList(
        createItem("Some name", "0123456789", 5)))
      .build());
    assertContains(createExpectedRow(1, "Some name", "0123456789", 5), actual);
  }

  @Test
  public void testTwoItem() {
    String actual = formatter.format(Basket.builder()
      .items(asList(
        createItem("Other name", "9876543210", 3),
        createItem("Some name", "0123456789", 5)))
      .build());

    assertContains(createExpectedRow(2, "Some name", "0123456789", 5), actual);
    assertContains(createExpectedRow(1, "Other name", "9876543210", 3), actual);
  }

  private String createExpectedRow(int index, String name, String barcode, int amount) {
    return "<tr>" +
      "<td>" + index + "</td>" +
      "<td>" + name + "</td>" +
      "<td>" + barcode + "</td>" +
      "<td>" + amount + "</td>" +
      "</tr>";
  }

  private BasketItem createItem(String name, String barcode, int amount) {
    return BasketItem.builder()
      .id(barcode)
      .amount(amount)
      .details(ItemDetails.builder()
        .id(barcode)
        .name(name)
        .price(100, "Ft")
        .build())
      .build();
  }

  private void assertContains(String expected, String actual) {
    assertTrue(
      "Could not find sequence:\n" +
        "<" + expected + ">\n" +
        "In\n" +
        "<" + actual + ">",
      actual.contains(expected));
  }

}