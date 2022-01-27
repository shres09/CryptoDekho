package com.cryptowatch.models;

public class Currency {
    public static final String TABLE_NAME = "currency";
    public static final String FIELD_ID = "id";

    private String id;
    private String name;
    private String image;
    private String price;
    private String marketCap;
    private String percentChange;
    private boolean isInPortfolio;

    public Currency() {

    }

    public Currency(String id, String name, String image, String price, String marketCap, String percentChange, boolean isInPortfolio) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.marketCap = marketCap;
        this.percentChange = percentChange;
        this.isInPortfolio = isInPortfolio;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public boolean isInPortfolio() {
        return isInPortfolio;
    }

    public void setInPortfolio(boolean inPortfolio) {
        isInPortfolio = inPortfolio;
    }
}