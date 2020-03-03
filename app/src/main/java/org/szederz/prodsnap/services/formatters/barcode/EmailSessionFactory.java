package org.szederz.prodsnap.services.formatters.barcode;

import org.szederz.prodsnap.configuration.EmailConfiguration;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class EmailSessionFactory {
  private final EmailConfiguration conf;

  public EmailSessionFactory(EmailConfiguration configuration) {
    this.conf = configuration;
  }

  public Session getSession() {
    return Session.getDefaultInstance(smtpOverSSLProperties(), new PasswordAuthenticator(conf.getSmtpUser(), conf.getSmtpPassword()));
  }

  private Properties smtpOverSSLProperties() {
    Properties properties = new Properties();

    if (Boolean.parseBoolean(conf.getSmtpSsl())) {
      properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }

    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.host", conf.getSmtpHost());
    properties.setProperty("mail.smtp.socketFactory.port", conf.getSmtpPort());
    properties.setProperty("mail.smtp.port", conf.getSmtpPort());
    return properties;
  }

  public static class PasswordAuthenticator extends Authenticator {
    private final String userName;
    private final String password;

    public PasswordAuthenticator(String userName, String password) {
      this.userName = userName;
      this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(userName, password);
    }
  }
}
