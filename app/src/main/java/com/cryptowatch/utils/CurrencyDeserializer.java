package com.cryptowatch.utils;

import com.cryptowatch.models.Currency;
import com.cryptowatch.models.Value;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class CurrencyDeserializer implements JsonDeserializer<Currency> {
    @Override
    public Currency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = json.getAsJsonObject().getAsJsonObject("Data");

        JsonObject currency = data.entrySet()
                .stream()
                .map(i -> i.getValue())
                .collect(Collectors.toList())
                .get(0)
                .getAsJsonObject();

        return new Currency(
                currency.get("Symbol").getAsString(),
                currency.get("CoinName").getAsString(),
                currency.get("ImageUrl").getAsString(),
                null,
                new Value(),
                false
        );
    }
}