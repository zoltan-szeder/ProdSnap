package org.szederz.prodsnap.services;

import org.szederz.prodsnap.configuration.Configuration;
import org.szederz.prodsnap.utils.encoding.SimpleJson;
import org.szederz.prodsnap.utils.function.Consumer;
import org.szederz.prodsnap.utils.net.http.Request;
import org.szederz.prodsnap.utils.net.http.Response;
import org.szederz.prodsnap.utils.net.http.client.HttpClient;

import java.util.Map;

public class ApplicationConfigurationService {
  private final HttpClient client;
  private final Consumer<String> secretConsumer;
  private final Configuration configuration;

  public ApplicationConfigurationService(HttpClient client, Consumer<String> secretConsumer, Configuration configuration) {
    this.client = client;
    this.secretConsumer = secretConsumer;
    this.configuration = configuration;
  }

  public void fetchConfiguration(String host, String user, String password, String createdUser) {
    secretConsumer.accept(password);

    Response response = client.send(Request.builder()
      .method("POST")
      .url("https://" + host + "/configure")
      .header("Authorization-User", user)
      .header("Content-Type", "application/json")
      .body("{" +
        "\"id\":\"" + createdUser + "\"" +
        "}")
      .build());

    if (response.getResponseCode() != 200)
      throw new RuntimeException("Could not fetch configuration");

    Map<String, String> map = new SimpleJson().parsePlainText(response.getBody());

    for (Map.Entry<String, String> entry : map.entrySet()) {
      configuration.set(entry.getKey(), entry.getValue());
    }

    for (Map.Entry<String, String> entry : configuration.getAll().entrySet()) {
      System.err.println(entry.getKey() + ": " + entry.getValue());
    }
  }
}
