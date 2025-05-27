package org.app.ql_ban_hang.entities;

import java.util.List;

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
//    private String image;
    private int quantity;
    private Category category;
//    private int status;
//    private int sold;
//    private List<Review> reviews;


    public Product(String name, double price, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
//        this.image = "a";
        this.quantity = quantity;
    }
    public Product(int id, String name, double price, String description,  int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
//        this.image = "a";
        this.quantity = quantity;
    }

    public Product(int id, String name, double price, String description, Category category, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
//        this.image = "a";
        this.category = category;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

