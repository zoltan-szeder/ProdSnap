package org.szederz.prodsnap.entities;

import java.text.DecimalFormat;

public class ItemDetails {
  private String id;
  private String name;
  private long price;
  private String currency;

  public ItemDetails(String id) {
    this.id = id;
  }

  public static Builder builder(){
    return new Builder();
  }

  public ItemDetails() {
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(long price, String currency) {
    this.price = price;
    this.currency = currency;
  }

  public String getFormattedPrice() {
    return new DecimalFormat("###,###.###").format(price) + " " + currency;
  }

  public long getPrice() {
    return price;
  }

  public static class Builder {
    private final ItemDetails detail;

    public Builder() {
      this.detail = new ItemDetails();
    }

    public Builder name(String name) {
      this.detail.setName(name);
      return this;
    }

    public Builder id(String id) {
      detail.setId(id);
      return this;
    }

    public Builder price(long price, String currency) {
      detail.setPrice(price, currency);
      return this;
    }

    public ItemDetails build() {
      return detail;
    }
  }
}
