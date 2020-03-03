package org.szederz.prodsnap.entities;

import java.util.ArrayList;
import java.util.List;

public class Basket {
  private Customer customer = new Customer();
  private BasketItem pendingItem = new BasketItem();
  private List<BasketItem> items = new ArrayList<>();


  public Basket() {
  }

  public Basket(Customer customer, BasketItem basketItem, List<BasketItem> basketItems) {
    this.customer = customer;
    this.pendingItem = basketItem;
    this.items = basketItems;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public BasketItem getPendingItem() {
    return pendingItem;
  }

  public void setPendingItem(BasketItem item) {
    this.pendingItem = item;
  }

  public int size() {
    return items.size();
  }

  public BasketItem getItemLike(int position) {
    return items.get(position);
  }

  public List<BasketItem> getAllItem() {
    return items;
  }

  public void addItem(BasketItem item) {
    items.add(0, item);
  }

  public BasketItem getItemLike(BasketItem item) {
    for (BasketItem savedItem : items)
      if (savedItem.getId().equals(item.getId()))
        return savedItem;

    return null;
  }

  public void update(BasketItem item) {
  }

  public void remove(int position) {
    items.remove(position);
  }

  public static class Builder {
    private Customer customer;
    private BasketItem pendingItem;
    private List<BasketItem> items;

    public Basket build() {
      return new Basket(customer, pendingItem, items);
    }

    public Builder customer(Customer customer) {
      this.customer = customer;
      return this;
    }

    public Builder pendingItem(BasketItem pendingItem) {
      this.pendingItem = pendingItem;
      return this;
    }

    public Builder items(List<BasketItem> items) {
      this.items = items;
      return this;
    }
  }
}
