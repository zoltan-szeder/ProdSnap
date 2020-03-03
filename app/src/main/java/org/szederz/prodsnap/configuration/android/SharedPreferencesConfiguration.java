package org.szederz.prodsnap.configuration.android;

import android.content.SharedPreferences;

import org.szederz.prodsnap.configuration.Configuration;
import org.szederz.prodsnap.configuration.HttpItemDetailsStorageConfiguration;
import org.szederz.prodsnap.utils.Optional;

import java.util.Map;

import static org.szederz.prodsnap.configuration.Constants.CONF_STORAGE_DETAILS_HTTP_URL;

public class SharedPreferencesConfiguration implements
  Configuration,
  HttpItemDetailsStorageConfiguration {
  private static SharedPreferencesConfiguration INSTANCE;

  SharedPreferences preferences;

  public static SharedPreferencesConfiguration getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SharedPreferencesConfiguration();
    }

    return INSTANCE;
  }

  public SharedPreferencesConfiguration() {
  }

  public void set(String key, String value) {
    preferences.edit()
      .putString(key, value)
      .apply();
  }

  @Override
  public boolean contains(String key) {
    return preferences.contains(key);
  }

  @Override
  public Map<String, String> getAll() {
    return (Map<String, String>) preferences.getAll();
  }

  public Optional<String> get(String key) {
    return Optional.ofNullable(preferences.getString(key, null));
  }

  public String getBaseHttpUrl() {
    return getString(CONF_STORAGE_DETAILS_HTTP_URL, "https://192.168.1.104:5001");
  }

  private String getString(String key, String defValue) {
    if (preferences == null)
      return defValue;

    return preferences.getString(key, defValue);
  }

  public void setPreferences(SharedPreferences preferences) {
    this.preferences = preferences;
  }
}
