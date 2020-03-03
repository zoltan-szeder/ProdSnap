package org.szederz.prodsnap.configuration;

import org.szederz.prodsnap.utils.function.Supplier;

public class ConfigurationSupplier implements Supplier<String> {
  private final Configuration conf;
  private final String key;

  public ConfigurationSupplier(Configuration conf, String key) {
    this.conf = conf;
    this.key = key;
  }

  @Override
  public String get() {
    return conf.get(key)
      .orElseThrow(new RuntimeException("Configuration is not present: " + key));
  }
}
