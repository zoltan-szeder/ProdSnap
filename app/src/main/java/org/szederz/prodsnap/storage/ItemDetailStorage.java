package org.szederz.prodsnap.storage;

import org.szederz.prodsnap.entities.ItemDetails;

public interface ItemDetailStorage {
  ItemDetails findById(String id);
  boolean hasId(String id);
}
