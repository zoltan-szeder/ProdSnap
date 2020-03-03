package org.szederz.prodsnap.services;

import org.szederz.prodsnap.entities.Basket;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.storage.BasketStorage;

public class BasketService {
    private final BasketStorage storage;

    public BasketService(
      BasketStorage storage
    ) {
        this.storage = storage;
    }

    public void addPending(BasketItem item) {
        Basket basket = storage.load();
        basket.setPendingItem(item);
        storage.save(basket);
    }

    public void dropBasket() {
        storage.save(new Basket());
    }

    public int getBasketSize() {
        return storage.load().size();
    }

    public BasketItem getBasketItemAt(int position) {
        return storage.load().getItemLike(position);
    }

    public int getBasketItemCount() {
        int sum = 0;

        for (BasketItem item : storage.load().getAllItem()) {
            sum += item.getAmount();
        }

        return sum;
    }

    public void saveAmountOfPendingItem(int amount) {
        Basket basket = storage.load();
        BasketItem item = basket.getPendingItem();
        BasketItem savedItem = basket.getItemLike(item);
        if (savedItem != null) {
            savedItem.setAmount(savedItem.getAmount() + amount);
            basket.update(savedItem);
        } else {
            item.setAmount(amount);
            basket.addItem(item);
        }
        basket.setPendingItem(new BasketItem());
        storage.save(basket);

    }

    public void dropItem(int position) {
        Basket basket = this.storage.load();
        basket.remove(position);
    }

    public Basket getBasket() {
        return this.storage.load();
    }
}
