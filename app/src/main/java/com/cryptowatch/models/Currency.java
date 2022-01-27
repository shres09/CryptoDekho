package com.cryptowatch.models;

public class Currency {
    public static final String TABLE_NAME = "currency";
    public static final String FIELD_ID = "id";

    private String id;
    private String name;
    private String image;
    private double price;
    private double percentChange;

    public Currency() {

    }

    public Currency(String id, String name, String image, double price, double percentChange) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.percentChange = percentChange;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }
}