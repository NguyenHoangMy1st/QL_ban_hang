package org.app.ql_ban_hang.entities;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private String name;
    private String address;
    private String phone;
    private int paymentMethod;
    private int status;
    private double totalMoney;

    private List<OrderItem> orderItems;
    private User user;

    public Order(int id, String name, String address, String phone, int paymentMethod, int status, double totalMoney, User user, List<OrderItem> orderItems) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.totalMoney = totalMoney;
        this.user = user;
        this.orderItems = orderItems;
    }
    public Order(String name, String address, String phone, int paymentMethod, int status, double totalMoney, User user) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.totalMoney = totalMoney;
        this.user = user;
        this.orderItems = new ArrayList<>();
    }
    public Order(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public int getPaymentMethod() { return paymentMethod; }
    public int getStatus() { return status; }
    public double getTotalMoney() { return totalMoney; }
    public User getUser() { return user; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPaymentMethod(int paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setStatus(int status) { this.status = status; }
    public void setTotalMoney(double totalMoney) { this.totalMoney = totalMoney; }
    public void setUser(User user) { this.user = user; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    public void addOrderItem(OrderItem item) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(item);
    }
}
