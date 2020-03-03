package org.szederz.prodsnap.services.transfer;

import org.junit.Test;
import org.szederz.prodsnap.configuration.EmailConfiguration;
import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.entities.Customer;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.services.formatters.barcode.EmailSessionFactory;
import org.szederz.prodsnap.services.formatters.html.BasketItemFormatter;
import org.szederz.prodsnap.services.formatters.html.HtmlBasketFormatterWrapper;
import org.szederz.prodsnap.services.formatters.html.HtmlDivBasketFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import androidx.arch.core.util.Function;

import static org.junit.Assert.assertTrue;

public class BasketMimeMessageFormatterTest {
  private BasketMimeMessageFormatter formatter = new BasketMimeMessageFormatter(
    new Function<String, ByteArrayOutputStream>() {
      @Override
      public ByteArrayOutputStream apply(String s) {
        try {
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          os.write(String.format("Bitmap[%s]", s).getBytes());
          return os;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    },
    new EmailSessionFactory(new EmailConfiguration(null)),
    new HtmlBasketFormatterWrapper(
      new HtmlDivBasketFormatter(
        new BasketItemFormatter())),
    new EmailConfiguration(null));

  @Test
  public void testBasicHtmlElements() throws Exception {
    MimeMessage message = formatter.format(new Basket());
    String body = getStringContent(getPart(message, 0));
    assertTrue(body.startsWith("<!DOCTYPE html><html><head>"));
    assertContains(body, "<meta charset=\"utf-8\">");
    assertContains(body, "<style type=\"text/css\" style=\"display:none\">");
    assertContains(body, "</style>");
    assertContains(body, "</head><body>");
    assertTrue(body.endsWith("</body></html>"));
  }

  @Test
  public void testImagesAreIncluded() throws Exception {
    Basket basket = new Basket();
    basket.setCustomer(customer("John Doe", "Some detail"));
    basket.addItem(
      item(
        "0123456789012",
        15,
        detail("0123456789012", 2000, "Item1")));

    MimeMessage message = formatter.format(basket);
    String body = getStringContent(getPart(message, 0));
    assertContains(body, "<h1>15 db</h1>");
    assertContains(body, "<h2>Item1</h2>");
    assertContains(body, "<img src=\"cid:0123456789012\"");
  }

  private void assertContains(String body, String str) {
    assertTrue(body.contains(str));
  }

  private Customer customer(String name, String detail) {
    Customer customer = new Customer();
    customer.setName(name);
    customer.setDetails(detail);
    return customer;
  }

  private ItemDetails detail(String id, int price, String itemName) {
    ItemDetails details = new ItemDetails();
    details.setId(id);
    details.setPrice(price, "Ft");
    details.setName(itemName);
    return details;
  }

  private BasketItem item(String id, int amount, ItemDetails details) {
    BasketItem item = new BasketItem();
    item.setDetails(details);
    item.setAmount(amount);
    item.setId(id);
    return item;
  }

  private String getStringContent(BodyPart part) throws IOException, MessagingException {
    return (String) part.getContent();
  }

  private BodyPart getPart(MimeMessage message, int index) throws MessagingException, IOException {
    return ((MimeMultipart) message.getContent()).getBodyPart(index);
  }

}