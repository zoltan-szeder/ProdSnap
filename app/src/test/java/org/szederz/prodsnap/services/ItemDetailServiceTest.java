package org.szederz.prodsnap.services;

import org.junit.Test;
import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.BasketStorage;
import org.szederz.prodsnap.storage.details.InMemoryItemDetailStorage;

import static org.junit.Assert.*;

public class ItemDetailServiceTest {
    private BasketStorage basketStorage = new BasketStorage();
    private InMemoryItemDetailStorage itemDetailStorage = new InMemoryItemDetailStorage();
    private ItemDetailService service = new ItemDetailService(basketStorage, itemDetailStorage);

    @Test
    public void testDetailsAreFetched(){
        addPendingItemWithId("0123456789");
        addDetail("0123456789", "name", 123456789);

        ItemDetails response = service.load();

        assertEquals("0123456789", response.getId());
        assertEquals("name", response.getName());
        assertTrue(response.getFormattedPrice().matches("123.456.789 Ft"));
    }

    @Test
    public void testMissingDetails(){
        addPendingItemWithId("0123456789");

        ItemDetails response = service.load();

        assertNull(response);
    }

    private void addDetail(String id, String name, int price) {
        ItemDetails detail = new ItemDetails(id);
        detail.setName(name);
        detail.setPrice(price, "Ft");
        itemDetailStorage.addItemDetail(detail);
    }

    private void addPendingItemWithId(String id) {
        Basket basket = basketStorage.load();
        BasketItem item = new BasketItem();
        item.setId(id);
        basket.setPendingItem(item);
    }
}