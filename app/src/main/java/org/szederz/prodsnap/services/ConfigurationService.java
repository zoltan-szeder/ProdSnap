package org.szederz.prodsnap.services;

import org.szederz.prodsnap.configuration.Configuration;
import org.szederz.prodsnap.utils.Optional;

public class ConfigurationService {
  private final Configuration configuration;

  public ConfigurationService(Configuration configuration) {
    this.configuration = configuration;
  }

  public Optional<String> get(String key) {
    return configuration.get(key);
  }

  public void set(String key, String value) {
    configuration.set(key, value);
  }

  public boolean contains(String key) {
    return configuration.contains(key);
  }
}
