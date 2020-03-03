package org.szederz.prodsnap.storage;

import org.junit.Test;
import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.storage.details.InMemoryItemDetailStorage;

import static org.junit.Assert.*;

public class ItemDetailStorageTest {
    InMemoryItemDetailStorage storage = new InMemoryItemDetailStorage();

    @Test
    public void testFindById() {
        ItemDetails detail = new ItemDetails("123456789");
        detail.setName("name");
        detail.setPrice(19895, "Ft");
        storage.addItemDetail(detail);

        ItemDetails response = storage.findById("123456789");
        assertEquals("123456789", response.getId());
        assertEquals("name", response.getName());
        assertTrue(response.getFormattedPrice().matches("19.895 Ft"));
    }
}