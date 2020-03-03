package org.szederz.prodsnap.entities;

public class Customer {
    private String name = "";
    private String customerId = "";
    private String details = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
