package org.szederz.prodsnap.entities;

public class BasketItem {
  private String id;
  private ItemDetails details;
  private int amount = 0;

  public BasketItem() {
  }

  public BasketItem(String id, ItemDetails details, int amount) {
    this.id = id;
    this.details = details;
    this.amount = amount;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setDetails(ItemDetails details) {
    this.details = details;
  }

  public ItemDetails getDetails() {
    return details;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getAmount() {
    return this.amount;
  }

  public static class Builder {
    private String id;
    private ItemDetails details;
    private int amount = 0;


    public BasketItem build() {
      return new BasketItem(id, details, amount);
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder details(ItemDetails details) {
      this.details = details;
      return this;
    }

    public Builder amount(int amount) {
      this.amount = amount;
      return this;
    }
  }
}
