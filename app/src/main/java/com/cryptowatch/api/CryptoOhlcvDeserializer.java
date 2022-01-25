package com.cryptowatch.api;

import com.cryptowatch.models.CryptoOhlcv;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CryptoOhlcvDeserializer implements JsonDeserializer<List<CryptoOhlcv>> {
    @Override
    public List<CryptoOhlcv> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<CryptoOhlcv> cryptoOhlcv = new ArrayList<>();

        final JsonArray ohlcvArray = json.getAsJsonObject().getAsJsonObject("Data").getAsJsonArray("Data");

        for (JsonElement ohlcvElement : ohlcvArray) {
            final JsonObject ohlcv = ohlcvElement.getAsJsonObject();

            cryptoOhlcv.add(new CryptoOhlcv(
                    ohlcv.get("time").getAsLong(),
                    ohlcv.get("open").getAsDouble(),
                    ohlcv.get("high").getAsDouble(),
                    ohlcv.get("low").getAsDouble(),
                    ohlcv.get("close").getAsDouble()
            ));
        }

        return cryptoOhlcv;
    }
}
