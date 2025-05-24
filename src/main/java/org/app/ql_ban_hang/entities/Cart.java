package org.app.ql_ban_hang.entities;

public class Cart {
    private int id;
    private int quantity;
    private int status; // 0: outCart, 1: inCart
    private double price;
    private Product product;
    private User user;

    public Cart(int quantity, int status, double price, Product product, User user) {
        this.quantity = quantity;
        this.status = status;
        this.price = price;
        this.product = product;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public int getBuy_count() {
        return quantity;
    }

    public int getStatus() {
        return status;
    }

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBuy_count(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
