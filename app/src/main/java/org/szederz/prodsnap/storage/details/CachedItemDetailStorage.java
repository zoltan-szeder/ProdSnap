package org.szederz.prodsnap.storage.details;

import org.szederz.prodsnap.configuration.BarcodeCache;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.CachableItemDetailStorage;

import java.util.List;

import static org.szederz.prodsnap.utils.converters.ItemDetailsJsonConverter.parsed;
import static org.szederz.prodsnap.utils.converters.ItemDetailsJsonConverter.serialized;

public class CachedItemDetailStorage implements CachableItemDetailStorage {
  private final BarcodeCache cache;
  private final CachableItemDetailStorage storage;

  public CachedItemDetailStorage(CachableItemDetailStorage storage, BarcodeCache cache) {
    this.storage = storage;
    this.cache = cache;
  }

  @Override
  public ItemDetails findById(String id) {
    return parsed(cache.getBarcodeItem(id));
  }

  @Override
  public List<ItemDetails> findAll() {
    return persist(storage.findAll());
  }

  private List<ItemDetails> persist(List<ItemDetails> listOfDetails) {
    for (ItemDetails details : listOfDetails)
      persist(details);

    return listOfDetails;
  }

  private void persist(ItemDetails details) {
    cache.setBarcodeItem(details.getId(), serialized(details));
  }

  @Override
  public boolean hasId(String id) {
    return cache.getBarcodeItem(id) != null;
  }
}
