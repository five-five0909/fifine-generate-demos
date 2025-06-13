package com.toyshop.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Toy implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private int toyId;
    private String toyName;
    private String brand;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private String description;

    // Default constructor
    public Toy() {
    }

    // Parameterized constructor (optional, but can be useful)
    public Toy(int toyId, String toyName, String brand, String category, BigDecimal price, String imageUrl, String description) {
        this.toyId = toyId;
        this.toyName = toyName;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Parameterized constructor without toyId (for adding new toys)
    public Toy(String toyName, String brand, String category, BigDecimal price, String imageUrl, String description) {
        this.toyName = toyName;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }


    // Getters and Setters
    public int getToyId() {
        return toyId;
    }

    public void setToyId(int toyId) {
        this.toyId = toyId;
    }

    public String getToyName() {
        return toyName;
    }

    public void setToyName(String toyName) {
        this.toyName = toyName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString() method (optional, for debugging)
    @Override
    public String toString() {
        return "Toy{" +
                "toyId=" + toyId +
                ", toyName='" + toyName + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + (description != null ? description.substring(0, Math.min(description.length(), 50)) + "..." : "null") + '\'' +
                '}';
    }
}
