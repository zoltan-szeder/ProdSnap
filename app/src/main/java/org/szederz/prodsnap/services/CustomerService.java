package org.szederz.prodsnap.services;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.Customer;
import org.szederz.prodsnap.storage.BasketStorage;

public class CustomerService {
    private final BasketStorage storage;

    public CustomerService(BasketStorage storage) {
        this.storage = storage;
    }

    public void setCustomer(Customer customer) {
        Basket basket = storage.load();
        basket.setCustomer(customer);
        storage.save(basket);
    }

    public Customer getCustomer() {
        return storage.load().getCustomer();
    }
}
