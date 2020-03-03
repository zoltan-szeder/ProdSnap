package org.szederz.prodsnap.services;

import org.junit.Test;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.storage.BasketStorage;

import static org.junit.Assert.assertEquals;

public class BasketServiceTest {
    private BasketStorage storage = new BasketStorage();
    private BasketService service = new BasketService(storage);

    @Test
    public void testBasket(){
        BasketItem item = new BasketItem();
        item.setId("599123456789");
        service.addPending(item);

        assertEquals("599123456789", storage.load().getPendingItem().getId());
    }

}