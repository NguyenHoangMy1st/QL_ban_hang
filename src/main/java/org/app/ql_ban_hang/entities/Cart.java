package org.app.ql_ban_hang.entities;

public class Cart {
    private int id;
    private int quantity;
    private int status; // 0: outCart, 1: inCart
    private double priceTotal;
    private Product product;
    private User user;


    // thêm sản phẩm vào giỏ
    public Cart(int quantity, int status, double priceTotal, Product product, User user) {
        this.quantity = quantity;
        this.status = status;
        this.priceTotal = priceTotal;
        this.product = product;
        this.user = user;
    }

    // cập nhật giỏ hàng
    public Cart(int quantity, double priceTotal){
        this.quantity = quantity;
        this.priceTotal = priceTotal;
    }
    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStatus() {
        return status;
    }

    public Product getProduct() {
        return product;
    }

    public double getPriceTotal() {
        return priceTotal;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
