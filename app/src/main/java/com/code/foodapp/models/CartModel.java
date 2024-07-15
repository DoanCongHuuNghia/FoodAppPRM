package com.code.foodapp.models;

import net.sourceforge.jtds.jdbc.DateTime;

import java.time.LocalDateTime;
import java.util.Date;

public class CartModel {

    int id;
    int productId;
    int userId;
    String image;
    String name;
    int quantity;
    String price;
    String rating;

    Date createdAt;

    public CartModel(String image, String name, String price, String rating) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public CartModel(int id, int productId, int userId, int quantity, Date createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public CartModel(int id, int productId, int userId, String image, String name, int quantity, String price, String rating, Date createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
