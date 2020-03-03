package org.szederz.prodsnap.services.transfer;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.services.BasketTransferService;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Transport;

public class EmailBasketTransferService implements BasketTransferService {
  private final BasketMimeMessageFormatter emailMessageFormatter;

  public EmailBasketTransferService(BasketMimeMessageFormatter formatter) {
    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");

    this.emailMessageFormatter = formatter;
  }

  @Override
  public void send(Basket basket) {
    try {
      Transport.send(emailMessageFormatter.format(basket));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
