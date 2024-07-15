package com.code.foodapp.models;

public class ProductModel {

    int id;
    int cId;
    int dailyMealId;
    String name;
    String image;
    String timing;
    String rating;
    String price;
    String favourite;
    String description;

    public ProductModel(int id, int cId, int dailyMealId, String name, String image, String timing, String rating, String price, String favourite, String description) {
        this.id = id;
        this.cId = cId;
        this.dailyMealId = dailyMealId;
        this.name = name;
        this.image = image;
        this.timing = timing;
        this.rating = rating;
        this.price = price;
        this.favourite = favourite;
        this.description = description;
    }

    public int getDailyMealId() {
        return dailyMealId;
    }

    public void setDailyMealId(int dailyMealId) {
        this.dailyMealId = dailyMealId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
