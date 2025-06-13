package com.toyshop.bean;

import java.math.BigDecimal;

// 玩具实体类
public class Toy {
    private int toyId;             // 玩具ID
    private String toyName;        // 玩具名
    private String brand;          // 玩具品牌
    private String category;       // 玩具类别
    private BigDecimal price;      // 价格
    private String imageUrl;       // 图片地址
    private String description;    // 玩法介绍

    // Getter 和 Setter 方法
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
}
