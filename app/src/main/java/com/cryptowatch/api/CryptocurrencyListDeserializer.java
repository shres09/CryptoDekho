package com.cryptowatch.api;

import com.cryptowatch.models.Cryptocurrency;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CryptocurrencyListDeserializer implements JsonDeserializer<List<Cryptocurrency>> {
    @Override
    public List<Cryptocurrency> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Cryptocurrency> cryptocurrencies = new ArrayList<>();

        final JsonArray cryptocurrencyArray = json.getAsJsonObject().getAsJsonArray("Data");

        for (JsonElement cryptocurrencyElement : cryptocurrencyArray) {
            final JsonObject cryptocurrencyObject = cryptocurrencyElement.getAsJsonObject();
            final JsonObject coinInfo = cryptocurrencyObject.getAsJsonObject("CoinInfo");
            final JsonObject price = cryptocurrencyObject.getAsJsonObject("RAW").getAsJsonObject("EUR");

            cryptocurrencies.add(new Cryptocurrency(
                    coinInfo.get("Name").getAsString(),
                    coinInfo.get("FullName").getAsString(),
                    coinInfo.get("ImageUrl").getAsString(),
                    price.get("PRICE").getAsDouble()
            ));
        }

        return cryptocurrencies;
    }
}