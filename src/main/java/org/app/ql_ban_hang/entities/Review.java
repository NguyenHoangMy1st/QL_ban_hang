package org.app.ql_ban_hang.entities;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int productId;
    private int userId;
    private double rating;
    private String commentText;
    private LocalDateTime createdAt;

    // Constructors
    public Review(int productId, int userId, double rating, String commentText) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.commentText = commentText;
        this.createdAt = LocalDateTime.now();
    }

    public Review(int id, int productId, int userId,  double rating, String commentText, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getUserId() {
        return userId;
    }


    public double getRating() {
        return rating;
    }

    public String getCommentText() {
        return commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}