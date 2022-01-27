package com.cryptowatch.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class PriceDeserializer implements JsonDeserializer<String[]> {
    @Override
    public String[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = json.getAsJsonObject().getAsJsonObject("DISPLAY");

        JsonObject priceInfo = data.entrySet()
                .stream()
                .map(i -> i.getValue())
                .collect(Collectors.toList())
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("EUR"); // FIXME: currency shouldn't be hardcoded

        return new String[] {
                priceInfo.get("PRICE").getAsString(),
                priceInfo.get("MKTCAP").getAsString(),
                priceInfo.get("CHANGEPCT24HOUR").getAsString()
        };
    }
}