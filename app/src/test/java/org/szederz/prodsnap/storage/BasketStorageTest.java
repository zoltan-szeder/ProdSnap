package org.szederz.prodsnap.storage;

import org.junit.Test;
import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.Customer;

import static org.junit.Assert.assertEquals;

public class BasketStorageTest {
    BasketStorage storage = new BasketStorage();

    @Test
    public void testStorageAlreadyContainsEmptyBasket(){
        storage.load();
    }
    @Test
    public void testStorageSave(){
        Basket basket = new Basket();
        Customer customer = new Customer();
        customer.setName("John Doe");
        basket.setCustomer(customer);
        storage.save(basket);
        Basket loadedBasket = storage.load();
        assertEquals("John Doe", loadedBasket.getCustomer().getName());
    }

}