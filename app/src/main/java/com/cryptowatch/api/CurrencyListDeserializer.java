package com.cryptowatch.api;

import com.cryptowatch.models.Currency;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CurrencyListDeserializer implements JsonDeserializer<List<Currency>> {
    @Override
    public List<Currency> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Currency> currencyList = new ArrayList<>();

        final JsonArray currencyArray = json.getAsJsonObject().getAsJsonArray("Data");

        for (JsonElement el : currencyArray) {
            final JsonObject currency = el.getAsJsonObject();
            final JsonObject coinInfo = currency.getAsJsonObject("CoinInfo");
            final JsonObject price = currency.getAsJsonObject("RAW").getAsJsonObject("EUR");

            currencyList.add(new Currency(
                    coinInfo.get("Name").getAsString(),
                    coinInfo.get("FullName").getAsString(),
                    coinInfo.get("ImageUrl").getAsString(),
                    price.get("PRICE").getAsDouble()
            ));
        }

        return currencyList;
    }
}