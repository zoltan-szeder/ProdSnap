package org.szederz.prodsnap.services;

import org.szederz.prodsnap.configuration.BarcodeCache;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.CachableItemDetailStorage;

import static org.szederz.prodsnap.utils.converters.ItemDetailsJsonConverter.serialized;

public class StorageCacheService {
  private final BarcodeCache barcodeCache;
  private CachableItemDetailStorage cachableItemDetailStorage;

  public StorageCacheService(
    CachableItemDetailStorage cachableItemDetailStorage,
    BarcodeCache barcodeCache
  ) {
    this.cachableItemDetailStorage = cachableItemDetailStorage;
    this.barcodeCache = barcodeCache;
  }


  public void rebuild() {
    if (barcodeCache.hasExpired())
      forceRebuild();
  }


  public void forceRebuild() {
    for (ItemDetails itemDetails : cachableItemDetailStorage.findAll())
      barcodeCache.setBarcodeItem(itemDetails.getId(), serialized(itemDetails));

    barcodeCache.commit();
  }
}
