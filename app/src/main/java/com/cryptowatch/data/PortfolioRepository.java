package com.cryptowatch.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cryptowatch.api.CryptoCompareService;
import com.cryptowatch.models.Currency;
import com.cryptowatch.models.Value;
import com.cryptowatch.utilities.Constants;
import com.cryptowatch.utilities.CurrencyDeserializer;
import com.cryptowatch.utilities.ValueDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioRepository {
    private final Database database;

    private MutableLiveData<List<Currency>> portfolio;

    public PortfolioRepository(Database database) {
        this.database = database;
        this.portfolio = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<List<Currency>> getCurrenciesFromPortfolio() {
        CryptoCompareService currencyService = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(new TypeToken<List<Currency>>() {}.getType(), new CurrencyDeserializer())
                .create(CryptoCompareService.class);

        CryptoCompareService valueService = CryptoCompareService.RetrofitClientInstance
                .getRetrofitInstance(Value.class, new ValueDeserializer())
                .create(CryptoCompareService.class);


        List<String> currenciesId = getAllCurrencyId();
        for (String id : currenciesId) {
            getCurrencyById(id, currencyService, valueService);
        }

        return portfolio;
    }

    private void getCurrencyById(String id, CryptoCompareService currencyService, CryptoCompareService valueService) {
        Call<List<Currency>> call = currencyService.getCurrencySummary(id);

        call.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                getCurrencyValue(response.body().get(0), valueService); // FIXME: Chain better
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Log.d("getCurrencyById", t.getMessage());
            }
        });
    }

    private void getCurrencyValue(Currency currency, CryptoCompareService valueService) {
        Call<Value> call = valueService.getCurrencyValue(currency.getId(), Constants.CONVERSION_CURRENCY);

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                currency.setValue(response.body());
                portfolio.getValue().add(currency);
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.d("getCurrencyValue", t.getMessage());
            }
        });
    }

    // FIXME: split into two classes (api calls + sqlite)

    public void insertCurrency(Currency currency) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Currency.FIELD_ID, currency.getId());
        db.insert(Currency.TABLE_NAME, null, cv);
    }

    public int deleteCurrency(Currency currency) {
        SQLiteDatabase db = database.getReadableDatabase();
        return db.delete(Currency.TABLE_NAME, Currency.FIELD_ID + "=?", new String[] {currency.getId()});
    }

    @SuppressLint("Range")
    public boolean isCurrencyInserted(Currency currency) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = ?", Currency.TABLE_NAME, Currency.FIELD_ID);
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(currency.getId())});
        return cursor.moveToFirst();
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllCurrencyId() {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = String.format("SELECT * FROM %s", Currency.TABLE_NAME);
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(Currency.FIELD_ID)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}