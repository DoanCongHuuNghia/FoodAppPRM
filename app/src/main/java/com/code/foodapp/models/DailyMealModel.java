package com.code.foodapp.models;

public class DailyMealModel {

    int id;
    String image;
    String name;
    String discount;
    String type;
    String description;

    public DailyMealModel(int id,String image, String name, String discount, String description, String type) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.discount = discount;
        this.description = description;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
