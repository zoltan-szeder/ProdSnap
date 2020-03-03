package org.szederz.prodsnap.storage.details.http;

import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.CachableItemDetailStorage;
import org.szederz.prodsnap.utils.converters.ItemDetailsJsonConverter;
import org.szederz.prodsnap.utils.function.Supplier;
import org.szederz.prodsnap.utils.net.http.Request;
import org.szederz.prodsnap.utils.net.http.Response;
import org.szederz.prodsnap.utils.net.http.client.HttpClient;

import java.util.List;

public class HttpItemDetailStorage implements CachableItemDetailStorage{

  private final Supplier<String> urlBase;
  private final String endpoint;
  private final HttpClient httpClient;

  public HttpItemDetailStorage(HttpClient httpClient, Supplier<String> urlBase, String endpoint) {
    this.httpClient = httpClient;
    this.urlBase = urlBase;
    this.endpoint = endpoint;
  }

  @Override
  public ItemDetails findById(final String id) {
    try {
      Response response = fetchItemDetails(id);

      if (response.getResponseCode() != 200)
        throw new RuntimeException("Could not find basket item with id: " + id);

      return ItemDetailsJsonConverter.parsed(response.getBody());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<ItemDetails> findAll() {
    try {
      Response response = fetchAllItemDetails();

      if (response.getResponseCode() != 200)
        throw new RuntimeException("Could not connect to item details");

      return ItemDetailsJsonConverter.allParsed(response.getBody());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Response fetchAllItemDetails() {
    return httpClient
        .send(Request.builder()
          .url(urlBase.get() + endpoint)
          .method("GET")
          .build());
  }

  private Response fetchItemDetails(String id) {
    return httpClient
      .send(Request.builder()
        .url(urlBase.get() + endpoint + "/" + id)
        .method("GET")
        .build());
  }

  @Override
  public boolean hasId(String id) {
    return false;
  }
}
