package org.szederz.prodsnap.configuration;

import org.szederz.prodsnap.utils.Optional;

import java.util.Map;

public interface Configuration {
  Optional<String> get(String key);

  void set(String key, String value);

  boolean contains(String key);

  Map<String, String> getAll();
}
