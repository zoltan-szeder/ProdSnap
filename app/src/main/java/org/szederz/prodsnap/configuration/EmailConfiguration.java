package org.szederz.prodsnap.configuration;

public class EmailConfiguration {
  private final Configuration conf;

  public EmailConfiguration(Configuration conf) {
    this.conf = conf;
  }

  public String getSmtpPassword() {
    return getString("transfer.smtp.password");
  }

  public String getSmtpUser() {
    return getString("transfer.smtp.user");
  }

  public String getSmtpHost() {
    return getString("transfer.smtp.host");
  }

  public String getSmtpPort() {
    return getString("transfer.smtp.port");
  }

  public String getSmtpSsl() {
    return getString("transfer.smtp.ssl", "true");
  }

  public String getSmtpTo() {
    return getString("transfer.smtp.to", getSmtpUser());
  }

  public String getSmtpFrom() {
    return getString("transfer.smtp.from", getSmtpUser());
  }

  public String getString(String key, String def) {
    if (conf == null)
      return def;

    return conf.get(key)
      .orElseGet(def);
  }

  public String getString(String key) {
    if (conf == null) {
      throw new RuntimeException("Configuration is null");
    }

    return conf.get(key)
      .orElseThrow(new RuntimeException("Could not find key: " + key));
  }
}
