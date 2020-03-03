package org.szederz.prodsnap.services.transfer;

import org.szederz.prodsnap.configuration.EmailConfiguration;
import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.services.formatters.BasketFormatter;
import org.szederz.prodsnap.services.formatters.barcode.BarcodeBitmapGenerator;
import org.szederz.prodsnap.services.formatters.barcode.EmailSessionFactory;
import org.szederz.prodsnap.services.formatters.barcode.QuickBarcodeBitmapGenerator;

import java.io.ByteArrayOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import androidx.arch.core.util.Function;

import static javax.mail.Message.RecipientType.TO;
import static org.szederz.prodsnap.services.formatters.barcode.ImageConverters.toPNG;

public class BasketMimeMessageFormatter {
  private final Function<String, ByteArrayOutputStream> pngConverter;
  private final EmailConfiguration config;
  private final EmailSessionFactory emailSessionFactory;
  private final BasketFormatter formatter;

  public BasketMimeMessageFormatter(
    EmailSessionFactory emailSessionFactory,
    BasketFormatter formatter,
    EmailConfiguration config
  ) {
    this(
      new BarcodeStringToPngConverter(),
      emailSessionFactory,
      formatter,
      config
    );
  }

  public BasketMimeMessageFormatter(
    Function<String, ByteArrayOutputStream> pngConverter,
    EmailSessionFactory emailSessionFactory,
    BasketFormatter formatter,
    EmailConfiguration config) {
    this.emailSessionFactory = emailSessionFactory;
    this.formatter = formatter;
    this.pngConverter = pngConverter;
    this.config = config;
  }

  public MimeMessage format(Basket basket) throws Exception {
    MimeMessage email = new MimeMessage(emailSessionFactory.getSession());

    email.setFrom(new InternetAddress(config.getSmtpFrom()));
    email.setRecipient(TO, new InternetAddress(config.getSmtpTo()));
    email.setSubject(basket.getCustomer().getDetails().split("\n")[0]);
    email.setContent(createMultiparts(basket));
    return email;
  }

  private MimeMultipart createMultiparts(Basket basket) throws Exception {
    MimeMultipart multipart = new MimeMultipart();

    multipart.addBodyPart(createHtmlContent(basket));
    for (BasketItem item : basket.getAllItem()) {
      multipart.addBodyPart(createImageAttachment(item));
    }

    return multipart;
  }

  private MimeBodyPart createHtmlContent(Basket basket) throws MessagingException {
    MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setContent(formatter.format(basket), "text/html");
    return bodyPart;
  }

  private MimeBodyPart createImageAttachment(BasketItem item) throws MessagingException {
    MimeBodyPart bodyPart = new MimeBodyPart();

    DataSource source = new ByteArrayDataSource(
      pngConverter.apply(item.getId()).toByteArray(),
      "image/png");
    bodyPart.setDataHandler(new DataHandler(source));
    bodyPart.setContentID(String.format("<%s>", item.getId()));
    bodyPart.setFileName(item.getId() + ".png");

    return bodyPart;
  }


  public static class BarcodeStringToPngConverter implements Function<String, ByteArrayOutputStream> {
    private BarcodeBitmapGenerator barcodeBitmapGenerator = new QuickBarcodeBitmapGenerator();


    @Override
    public ByteArrayOutputStream apply(String input) {
      try {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(toPNG(barcodeBitmapGenerator.createBitmap(input)));
        return os;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
