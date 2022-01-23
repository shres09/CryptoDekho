package com.cryptowatch.models;

import com.google.gson.annotations.SerializedName;

public class Cryptocurrency {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("symbol")
    private String symbol;

    public Cryptocurrency() {

    }

    public Cryptocurrency(int id, String name, String symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
