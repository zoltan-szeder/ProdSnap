package org.szederz.prodsnap.services;

import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.BasketStorage;
import org.szederz.prodsnap.storage.ItemDetailStorage;

public class ItemDetailService {
    private final BasketStorage basketStorage;
    private final ItemDetailStorage itemDetailStorage;

    public ItemDetailService(BasketStorage basketStorage, ItemDetailStorage itemDetailStorage) {
        this.basketStorage = basketStorage;
        this.itemDetailStorage = itemDetailStorage;
    }

    public ItemDetails load() {
        BasketItem basketItem = basketStorage.load().getPendingItem();
        ItemDetails details = findDetailsFor(basketItem);
        basketItem.setDetails(details);
        return details;
    }

    private ItemDetails findDetailsFor(BasketItem item) {
        return itemDetailStorage.findById(item.getId());
    }

    public boolean hasId(String id) {
        return itemDetailStorage.hasId(id);
    }
}
