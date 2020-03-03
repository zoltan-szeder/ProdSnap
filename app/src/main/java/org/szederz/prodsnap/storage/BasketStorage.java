package org.szederz.prodsnap.storage;

import org.szederz.prodsnap.entities.Basket;

public class BasketStorage {
    private Basket basket = new Basket();

    public void save(Basket basket) {
        this.basket = basket;
    }

    public Basket load() {
        return this.basket;
    }
}
