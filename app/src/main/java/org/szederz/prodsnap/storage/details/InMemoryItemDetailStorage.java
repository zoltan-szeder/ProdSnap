package org.szederz.prodsnap.storage.details;

import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.ItemDetailStorage;

import java.util.HashMap;
import java.util.Map;

public class InMemoryItemDetailStorage implements ItemDetailStorage {
    private Map<String, ItemDetails> details = new HashMap<>();

    public void addItemDetail(ItemDetails detail) {
        details.put(detail.getId(), detail);
    }

    @Override
    public ItemDetails findById(String id) {
        return details.get(id);
    }

    @Override
    public boolean hasId(String id) {
        return details.containsKey(id);
    }
}
