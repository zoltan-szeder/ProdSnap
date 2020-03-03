package org.szederz.prodsnap.storage;

import org.szederz.prodsnap.entities.ItemDetails;

import java.util.List;

public interface CachableItemDetailStorage extends ItemDetailStorage {
  List<ItemDetails> findAll();
}
