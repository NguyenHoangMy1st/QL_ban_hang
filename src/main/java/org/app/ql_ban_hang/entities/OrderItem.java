package org.app.ql_ban_hang.entities;

public class OrderItem {
    private int id;
    private Product product;
    private int quantity;
    private double priceAtPurchase;

    public OrderItem(int id, Product product, int quantity, double priceAtPurchase) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public OrderItem(Product product, int quantity, double priceAtPurchase) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public OrderItem(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPriceAtPurchase() { return priceAtPurchase; }

    public void setId(int id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPriceAtPurchase(double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
    public double getSubtotal() {
        if (priceAtPurchase == 0 || quantity < 0) {
            return 0;
        }
        return priceAtPurchase*quantity;
    }
}
