package com.cryptowatch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CryptocurrencyList {

    @SerializedName("data")
    private List<Cryptocurrency> data;

    public CryptocurrencyList() {

    }

    public CryptocurrencyList(List<Cryptocurrency> data) {
        this.data = data;
    }

    public List<Cryptocurrency> getData() {
        return data;
    }

    public void setData(List<Cryptocurrency> data) {
        this.data = data;
    }

}
